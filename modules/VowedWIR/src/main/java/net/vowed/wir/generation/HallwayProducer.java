package net.vowed.wir.generation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.wir.Tuple;
import net.vowed.wir.RoomComponent;
import net.vowed.wir.astar.pathfinding.AStarNavigation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by JPaul on 10/1/2016.
 */
public class HallwayProducer implements Runnable
{
    boolean isDone = false;
    boolean isProcessing = false;

    protected BlockingQueue<List<Location>> queue;
    List<RoomComponent> rooms;

    Map<RoomComponent, List<Location>> roomHallways = Maps.newHashMap();
    List<Location> allHallwayLocations = Lists.newArrayList();
    List<Location> insideHallwayLocations = Lists.newArrayList();
    List<Location> allInsideHallwayLocations = Lists.newArrayList();

    List<Location> fullHallway = Lists.newArrayList();

    private final Set<BlockFace> DOOR_DIRECTIONS = EnumSet.of(BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SELF);

    public HallwayProducer(BlockingQueue<List<Location>> queue, List<RoomComponent> rooms)
    {
        this.queue = queue;
        this.rooms = rooms;
    }

    @Override
    public void run()
    {
        Queue<RoomComponent> hallwayQueue = Lists.newLinkedList();
        rooms.forEach(hallwayQueue::offer);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!hallwayQueue.isEmpty())
                {
                    if (!isProcessing)
                    {
                        Vowed.LOG.severe("LOADING HALLWAYS");

                        isProcessing = true;

                        final RoomComponent room = hallwayQueue.poll();
                        final RoomComponent closest = getClosest(room, rooms);

                        if (closest != null && room != null)
                        {
                            createHallway(closest, room, () ->
                            {
                                if (closest.hasHallways())
                                {
                                    RoomComponent newClosest = getClosest(room, rooms);

                                    if (newClosest != null)
                                    {
                                        createHallway(newClosest, room, () ->
                                        {
                                            Vowed.LOG.warning("adding to queue");

                                            queue.put(fullHallway);
                                            isProcessing = false;
                                        });
                                    }
                                }
                            });
                        }
                        else
                        {
                            Vowed.LOG.debug("NULL");
                        }
                    }
                }
                else
                {
                    if (!isProcessing)
                    {
                        List<Location> wallLocations = Lists.newArrayList();

                        for (RoomComponent room : rooms)
                        {
                            for (Location location : roomHallways.get(room))
                            {
                                if (!allInsideHallwayLocations.contains(location))
                                {
                                    int height = room.getHeight();
                                    int counter = 0;
                                    while (counter < height)
                                    {
                                        wallLocations.add(location.clone().add(0, counter, 0));

                                        counter++;
                                    }
                                }
                            }
                        }

                        try
                        {
                            queue.put(wallLocations);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        cancel();
                        Bukkit.getScheduler().runTaskLater(Vowed.getPlugin(), () -> isDone = true, 40);
                    }
                }
            }
        }.runTaskTimerAsynchronously(Vowed.getPlugin(), 20, 20);
    }

    private interface isDoneCallback
    {
        void isDone() throws InterruptedException;
    }

    public void createHallway(RoomComponent closest, RoomComponent room, isDoneCallback callback)
    {
        AStarNavigation navigation = new AStarNavigation(room, closest);

        navigation.createPath(closest.getLocation().getWorld(), locations ->
        {
            if (!room.hasHallway(closest))
            {
                RoomComponent intersectingRoom;

                for (Location location : locations)
                {
                    intersectingRoom = isLocationFine(rooms, location);

                    if (intersectingRoom == null)
                    {
                        for (BlockFace direction : BlockFace.values())
                        {
                            if (direction != BlockFace.UP && direction != BlockFace.DOWN)
                            {
                                Block block = location.getBlock().getRelative(direction);

                                RoomComponent interrupter = isLocationFine(rooms, block.getLocation());

                                if (interrupter == null)
                                {
                                    if ((direction == BlockFace.NORTH || direction == BlockFace.SOUTH || direction == BlockFace.WEST || direction == BlockFace.EAST))
                                    {
                                        insideHallwayLocations.add(block.getLocation());
                                    }

                                    allHallwayLocations.add(block.getLocation());
                                    fullHallway.add(block.getLocation());
                                }
                                else
                                {
                                    if (shouldFinish(closest, interrupter))
                                    {
                                        Vowed.LOG.debug("BREAKIGN LOOP");
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    Location wallLocation = getWallLocation(location);
                    RoomComponent roomBeside = getRoomBeside(wallLocation);
                    BlockFace direction = getDirection(wallLocation, roomBeside);

                    if (wallLocation != null && isLocationFine(rooms, location) == null && roomBeside != null)
                    {
                        if (!roomBeside.hasDoorways())
                        {
                            Bukkit.getScheduler().runTask(Vowed.getPlugin(), () -> caveOutDoorway(wallLocation));
                            roomBeside.addDoorway(direction, wallLocation);
                            roomBeside.setModified(true);
                        }
                        else
                        {
                            if (canFit(roomBeside, direction, wallLocation))
                            {
                                Bukkit.getScheduler().runTask(Vowed.getPlugin(), () -> caveOutDoorway(wallLocation));

                                if (roomBeside.isNearCorner(wallLocation))
                                {
                                    roomBeside.addCornerDoorway(direction, wallLocation);
                                }
                                else
                                {
                                    roomBeside.addDoorway(direction, wallLocation);
                                }

                                roomBeside.setModified(true);
                            }
                        }
                    }
                }
            }

            for (Location location : allHallwayLocations)
            {
                if (!insideHallwayLocations.contains(location))
                {
                    if (!roomHallways.containsKey(room))
                        roomHallways.put(room, Lists.newArrayList());

                    roomHallways.get(room).add(location);
                }
            }

            insideHallwayLocations.forEach(allInsideHallwayLocations::add);

            room.setHasHallwayTo(closest);
            closest.setHasHallwayTo(room);

            try
            {
                callback.isDone();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        });
    }

    public void caveOutDoorway(Location doorway)
    {
        if (doorway != null)
        {
            for (BlockFace direction : DOOR_DIRECTIONS)
            {
                if (direction != BlockFace.UP && direction != BlockFace.DOWN)
                {
                    for (int y = 1; y < 4; y++)
                    {
                        Location clone = doorway.clone().add(0, y, 0);

                        Block relative = clone.getBlock().getRelative(direction);

                        relative.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public RoomComponent getClosest(RoomComponent room, List<RoomComponent> rooms)
    {
        Map<Double, RoomComponent> distanceMap = Maps.newHashMap();
        double shortestDistance = Double.MAX_VALUE;

        for (RoomComponent roomComponent : rooms)
        {
            if (!roomComponent.getUUID().equals(room.getUUID()) && !roomComponent.hasHallway(room))
            {
                double distance = room.getLocation().distanceSquared(roomComponent.getLocation());

                distanceMap.put(distance, roomComponent);

                if (distance < shortestDistance)
                {
                    shortestDistance = distance;
                }
            }
        }

        return distanceMap.get(shortestDistance);
    }

    public BlockFace getDirection(Location doorway, RoomComponent room)
    {
        if (doorway == null)
            return null;

        Location addedX = doorway.clone().add(room.getRadius(), 0, 0);

        if (room.getLocation().distanceSquared(addedX) < (room.getRadius() + 1) * (room.getRadius() + 1))
        {
            return BlockFace.WEST;
        }

        Location subtractedX = doorway.clone().subtract(room.getRadius(), 0, 0);

        if (room.getLocation().distanceSquared(subtractedX) < (room.getRadius() + 1) * (room.getRadius() + 1))
        {
            return BlockFace.EAST;
        }

        Location addedZ = doorway.clone().add(0, 0, room.getRadius());

        if (room.getLocation().distanceSquared(addedZ) < (room.getRadius() + 1) * (room.getRadius() + 1))
        {
            return BlockFace.NORTH;
        }

        Location subtractedZ = doorway.clone().subtract(0, 0, room.getRadius());

        if (room.getLocation().distanceSquared(subtractedZ) < (room.getRadius() + 1) * (room.getRadius() + 1))
        {
            return BlockFace.SOUTH;
        }

        return null;
    }

    public Location getWallLocation(Location location)
    {
        for (BlockFace blockFace : DOOR_DIRECTIONS)
        {
            Block relative = location.getBlock().getRelative(blockFace);

            if (relative.getType() == Material.SMOOTH_BRICK)
                return relative.getLocation();
        }

        return null;
    }

    public boolean canFit(RoomComponent room, BlockFace direction, Location location)
    {
        if (direction == null)
            return false;

        List<Boolean> test = Lists.newArrayList();

        int counter = 0;

        for (Location doorway : room.getDoorways(direction))
        {
            if (room.isNearCorner(doorway))
                return true;

            if (counter >= 1)
            {
                if (counter >= 2)
                {
                    return false;
                }

                if (!room.isNearCorner(doorway) && doorway.distanceSquared(location) < 16)
                {
                    return false;
                }
            }

            if (doorway.distanceSquared(location) < 16)
            {
                test.add(false);
            }
            else
            {
                test.add(true);
            }

            if (!room.isNearCorner(doorway))
            {
                counter++;
            }
        }

        return !test.contains(false);
    }

    private RoomComponent getRoomBeside(Location location)
    {
        if (location != null)
        {
            for (BlockFace direction : DOOR_DIRECTIONS)
            {
                Block relative = location.getBlock().getRelative(direction);

                if (isLocationFine(rooms, relative.getLocation()) != null)
                {
                    return isLocationFine(rooms, relative.getLocation());
                }
            }
        }

        return null;
    }

    private boolean shouldFinish(RoomComponent destination, RoomComponent interrupter)
    {
        if (interrupter.hasHallway(destination))
            return true;

        return false;
    }

    private RoomComponent isLocationFine(List<RoomComponent> rooms, Location location)
    {
        for (RoomComponent room : rooms)
        {
            for (java.util.Map.Entry<Location, Tuple<Material, Byte>> entry : room.getVirtualBlocks().entrySet())
            {
                Location comparer = entry.getKey();
                Material material = entry.getValue().getA();

                if (material != Material.AIR && comparer.equals(location))
                {
                    return room;
                }
            }
        }

        return null;
    }

    private void runPathing(final Location start, final Location end, AStarNavigation.LocationCallback callback)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                List<Location> locations = Lists.newArrayList();

                if (start.getBlockX() - end.getBlockX() > 0)
                {
                    for (int x = start.getBlockX(); x > end.getBlockX(); x--)
                    {
                        locations.add(new Location(start.getWorld(), x, start.getBlockY(), start.getBlockZ()));
                    }
                }
                else
                {
                    for (int x = start.getBlockX(); x < end.getBlockX(); x++)
                    {
                        locations.add(new Location(start.getWorld(), x, start.getBlockY(), start.getBlockZ()));
                    }
                }

                if (start.getBlockZ() - end.getBlockZ() > 0)
                {
                    for (int z = start.getBlockZ(); z > end.getBlockZ(); z--)
                    {
                        Location location = new Location(start.getWorld(), end.getBlockX(), start.getBlockY(), z);

                        locations.add(location);
                    }
                }
                else
                {
                    for (int z = start.getBlockZ(); z < end.getBlockZ(); z++)
                    {
                        Location location = new Location(start.getWorld(), end.getBlockX(), start.getBlockY(), z);

                        locations.add(location);
                    }
                }

                callback.onSuccess(locations);
            }
        }.runTaskAsynchronously(Vowed.getPlugin());
    }
}
