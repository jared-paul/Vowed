package net.vowed.wir.generation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.wir.Tuple;
import net.vowed.wir.RoomComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by JPaul on 9/8/2016.
 */
public class HallwayLoader implements Loader
{
    RoomLoaderSync loaderSync;
    List<RoomComponent> rooms;

    boolean isProcessing = false;
    boolean isDone = false;

    List<Location> fullHallway = Lists.newArrayList();

    public HallwayLoader(RoomLoaderSync loaderSync)
    {
        this.loaderSync = loaderSync;
        this.rooms = loaderSync.rooms;
    }

    @Override
    public void load()
    {
        isProcessing = true;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                BlockingQueue<List<Location>> queue = new ArrayBlockingQueue<>(1024);
                HallwayProducer hallwayProducer = new HallwayProducer(queue, rooms);
                HallwayConsumer hallwayConsumer = new HallwayConsumer(queue, hallwayProducer);

                Bukkit.getScheduler().runTaskAsynchronously(Vowed.getPlugin(), hallwayProducer);
                Bukkit.getScheduler().runTaskAsynchronously(Vowed.getPlugin(), hallwayConsumer);

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if (hallwayProducer.isDone)
                        {
                            cancel();
                            isDone = true;
                            isProcessing = false;
                        }
                    }
                }.runTaskTimerAsynchronously(Vowed.getPlugin(), 100, 40);
            }
        }.runTask(Vowed.getPlugin());
    }

    public void doShit(RoomComponent closest, RoomComponent room, List<Location> insideLocations, List<Location> allLocations, Map<RoomComponent, List<Location>> hallways, List<Location> finalInsideLocations)
    {
        List<Location> locations = runPathing(room.getLocation(), closest.getLocation());


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

                            if (isLocationFine(rooms, block.getLocation()) == null)
                            {
                                if ((direction == BlockFace.NORTH || direction == BlockFace.SOUTH || direction == BlockFace.WEST || direction == BlockFace.EAST))
                                {
                                    insideLocations.add(block.getLocation());
                                }

                                allLocations.add(block.getLocation());
                                fullHallway.add(block.getLocation());
                            }
                        }
                    }

                    Bukkit.getScheduler().runTask(Vowed.getPlugin(), () -> location.getBlock().setType(Material.BEACON));
                }

                Location wallLocation = getWallLocation(location);
                RoomComponent roomBeside = getRoomBeside(wallLocation);
                BlockFace direction = getDirection(wallLocation, roomBeside);

                if (wallLocation != null && isLocationFine(rooms, location) == null && roomBeside != null)
                {
                    if (!roomBeside.hasDoorways())
                    {
                        caveOutDoorway(wallLocation);
                        roomBeside.addDoorway(direction, wallLocation);
                        roomBeside.setModified(true);
                    }
                    else
                    {
                        if (canFit(roomBeside, direction, wallLocation))
                        {
                            caveOutDoorway(wallLocation);

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

        for (Location location : allLocations)
        {
            if (!insideLocations.contains(location))
            {
                if (!hallways.containsKey(room))
                    hallways.put(room, Lists.newArrayList());

                hallways.get(room).add(location);
            }
        }

        insideLocations.forEach(finalInsideLocations::add);

        room.setHasHallwayTo(closest);
        closest.setHasHallwayTo(room);
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
                    location.getBlock().setType(Material.WOOD);
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


    @Override
    public boolean isProcessing()
    {
        return isProcessing;
    }

    @Override
    public boolean isDone()
    {
        return isDone;
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

    private final Set<BlockFace> DOOR_DIRECTIONS = EnumSet.of(BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SELF);

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

    private List<Location> runPathing(final Location start, final Location end)
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

        return locations;
    }
}
