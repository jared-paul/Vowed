package net.vowed.wir;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.data.DataException;
import net.vowed.api.wir.IMapComponent;
import net.vowed.wir.test.DungeonSpawner;
import org.bukkit.Location;

import java.io.IOException;
import java.util.List;

/**
 * Created by JPaul on 8/24/2016.
 */
public class Map
{
    List<IMapComponent> components = Lists.newArrayList();

    int width;
    int length;
    Location corner;
    RoomComponent start;
    RoomComponent end;

    public Map(int width, int length, Location corner)
    {
        this.width = width;
        this.length = length;
        this.corner = corner;
    }

    public int getSize()
    {
        return width * length;
    }

    public Location getMinimumPoint()
    {
        return corner.clone();
    }

    public Location getMaximumPoint()
    {
        return corner.clone().add(width, 0, length);
    }

    public int getWidth()
    {
        return width;
    }

    public int getLength()
    {
        return length;
    }

    public RoomComponent getStart()
    {
        return start;
    }

    public void setStart(RoomComponent start)
    {
        this.start = start;
    }

    public RoomComponent getEnd()
    {
        return end;
    }

    public void setEnd(RoomComponent end)
    {
        this.end = end;
    }

    public List<IMapComponent> getComponents()
    {
        return components;
    }

    public List<RoomComponent> getRoomComponents()
    {
        List<RoomComponent> roomComponents = Lists.newArrayList();

        for (IMapComponent component : components)
        {
            if (component instanceof RoomComponent)
            {
                roomComponents.add((RoomComponent) component);
            }
        }

        return roomComponents;
    }

    public void setComponents(List<IMapComponent> components)
    {
        this.components = components;
    }

    public void addComponent(IMapComponent component)
    {
        components.add(component);
    }

    public void loadInWorld() throws IOException, DataException
    {
        new DungeonSpawner(this);

        /**
         * run the processing side asynchronously :
         * loop through components
         * pick out ones that are compatible with each other (can fit)
         * add them to compatibleComponents
         * loop through compatibleComponents and load them in synchronously
         */

        /*
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (int index = 0; index < components.size(); index++)
                {
                    IMapComponent component = components.get(index);

                    if (index == 0)
                    {
                        ThreadLocalRandom random = ThreadLocalRandom.current();
                        int x = random.nextInt(getWidth()) + 1;
                        int z = random.nextInt(getLength()) + 1;

                        Location location = new Location(corner.getWorld(), corner.getX() + x, corner.getY(), corner.getZ() + z);
                        component.setLocation(location);

                        component.virtuallyLoad(location);
                        component.addEndLocations();

                        compatibleComponents.add(component);
                        component.setDoneProcessing(true);
                    }
                    else
                    {
                        IMapComponent previousComponent = components.get(index - 1);

                        Vowed.LOG.severe(previousComponent.isDoneProcessing());

                        Location endLocation = previousComponent.getEndLocations().get(0).add(0, 2, 0);
                        component.virtuallyLoad(endLocation);

                        boolean failed = component.canFitFully(components, endLocation);

                        if (failed)
                        {
                            Vowed.LOG.warning("NOT FIALED");

                            component.addEndLocations();

                            component.setLocation(endLocation);
                            compatibleComponents.add(component);
                            component.setDoneProcessing(true);
                        }
                        else
                        {
                            IMapComponent nextCompatible = findNextCompatible(endLocation);

                            if (nextCompatible == null)
                            {
                                components.remove(component);
                                index--;
                            }
                            else
                            {
                                Collections.swap(components, components.indexOf(nextCompatible), components.indexOf(component));

                                nextCompatible.addEndLocations();

                                nextCompatible.setLocation(endLocation);
                                compatibleComponents.add(nextCompatible);
                                nextCompatible.setDoneProcessing(true);
                            }
                        }
                    }
                }

                isDone.set(true);
            }
        }.runTaskAsynchronously(Vowed.getPlugin());
        */

        /*
        IMapComponent start = Vowed.getMapComponentContainer().getComponent("room1");
        start.setup(corner);
        start.setLocation(corner);

        for (IMapComponent component : findCompatibleList(start))
        {
            componentQueue.offer(component);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                IMapComponent component = componentQueue.poll();

                if (!componentQueue.isEmpty())
                {
                    component.loadInWorld(component.getLocation());
                }
                else
                {
                    cancel();
                }

                component.getTempSchematic().delete();
            }
        }.runTaskTimer(Vowed.getPlugin(), 5, 20);
        */

        //Bukkit.getScheduler().runTaskLater(Vowed.getPlugin(), this::makeHallways, 40);
    }

    public interface ComponentCallback
    {
        void onSuccess(List<IMapComponent> components);
    }

    /*
    public void makeHallways()
    {
        Util util = new Util();

        List<RoomComponent> finalRooms = Lists.newCopyOnWriteArrayList(getRoomComponents());
        List<RoomComponent> finalRoomsCopy = Lists.newArrayList(finalRooms);

        java.util.Map<Location, List<Location>> walls = Maps.newHashMap();

        List<List<Location>> finalInsideBlocks = Lists.newArrayList();

        AtomicInteger isDone = new AtomicInteger(0);

        Vowed.LOG.warning(finalRooms.size());

        for (RoomComponent component : finalRooms)
        {
            int index = finalRooms.indexOf(component);

            if (index != finalRooms.size() - 1)
            {
                RoomComponent closest = finalRooms.get(index + 1);

                if (!(component.hasHallwayTo(closest)))
                {
                    for (Location endLocation : component.getEndLocations())
                    {
                        runPathing(endLocation, closest.getStart(), new AStarNavigation.LocationCallback()
                        {
                            @Override
                            public void onSuccess(List<Location> locations)
                            {
                                Bukkit.getScheduler().runTask(Vowed.getPlugin(), () ->
                                {
                                    List<Location> insideBlocks = Lists.newArrayList();
                                    List<Location> allBlocks = Lists.newArrayList();

                                    for (Location location : locations)
                                    {
                                        Block block = location.getBlock();

                                        allBlocks.add(location);

                                        if (block.getType() != Material.TNT && block.getType() != Material.BEDROCK)
                                        {
                                            util.setBlockFast(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), Material.COBBLESTONE.getId(), block.getData());

                                            for (BlockFace direction : BlockFace.values())
                                            {
                                                Location relative = block.getRelative(direction).getLocation();

                                                if (!component.intersects(relative) && !closest.intersects(relative) && direction != BlockFace.UP && direction != BlockFace.DOWN)
                                                {
                                                    util.setBlockFast(relative.getWorld(), relative.getBlockX(), relative.getBlockY(), relative.getBlockZ(), Material.COBBLESTONE.getId(), relative.getBlock().getData());

                                                    allBlocks.add(relative);

                                                    if ((direction == BlockFace.NORTH || direction == BlockFace.SOUTH || direction == BlockFace.WEST || direction == BlockFace.EAST))
                                                        insideBlocks.add(relative);
                                                }
                                            }
                                        }
                                    }

                                    List<Location> wall = Lists.newArrayList();

                                    for (Location location : allBlocks)
                                    {
                                        if (!insideBlocks.contains(location) && !location.equals(endLocation))
                                        {
                                            wall.add(location);
                                        }
                                    }

                                    finalInsideBlocks.add(insideBlocks);

                                    walls.put(endLocation, wall);
                                });

                                isDone.incrementAndGet();
                            }
                        });

                        component.setHallwayExists(closest, true);
                        closest.setHallwayExists(component, true);
                    }
                }
            }
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Vowed.LOG.debug(isDone.get());

                if (isDone.get() == finalRooms.size() - 1)
                {
                    Bukkit.getScheduler().runTask(Vowed.getPlugin(), () ->
                    {
                        for (java.util.Map.Entry<Location, List<Location>> wall : walls.entrySet())
                        {
                            List<Location> wallBlocks = wall.getValue();

                            for (Location wallBlock : wallBlocks)
                            {
                                Vowed.LOG.warning(wallBlock.getBlockY());

                                if (!intersects(wallBlock, finalInsideBlocks) && !intersects(wallBlock, finalRooms))
                                {
                                    Vowed.LOG.debug(wallBlock.getBlockY());

                                    int height = 0;
                                    while (height < 5)
                                    {
                                        height++;

                                        Block block = wallBlock.getBlock().getRelative(0, height, 0);

                                        block.setType(Material.COBBLESTONE);
                                    }
                                }
                            }
                        }
                    });
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(Vowed.getPlugin(), 10, 20);
    }


    public boolean intersects(Location location, Collection<List<Location>> locations)
    {
        for (List<Location> locationList : locations)
        {
            if (locationList.contains(location))
                return true;
        }

        return false;
    }

    public boolean intersects(Location location, List<RoomComponent> roomComponents)
    {
        for (RoomComponent roomComponent : roomComponents)
        {
            if (roomComponent.intersects(location))
                return true;
        }

        return false;
    }

    public RoomComponent findNextClosest(RoomComponent component, List<RoomComponent> roomComponents)
    {
        for (RoomComponent roomComponent : component.getNearestRooms(roomComponents))
        {
            if (!roomComponent.hasHallwayTo(component))
            {
                return roomComponent;
            }
        }

        return null;
    }

    public List<Block> getFarthest(Location center, List<Location> locations)
    {
        List<Block> possibleBlocks = Lists.newArrayList();

        double farthestDistance = findFarthestDistance(center, locations);

        for (Location location : locations)
        {
            double distanceTo = location.distanceSquared(center);

            if (distanceTo == farthestDistance)
            {
                possibleBlocks.add(location.getBlock());
            }
        }

        return possibleBlocks;
    }

    public double findFarthestDistance(Location center, List<Location> locations)
    {
        double distance = 0;

        for (Location location : locations)
        {
            double distanceTo = location.distanceSquared(center);

            if (distanceTo > distance)
            {
                distance = distanceTo;
            }
        }

        return distance;
    }

    public void runPathing(final Location start, final Location end, AStarNavigation.LocationCallback callback)
    {
        new AStarNavigation(end, start).createPath(start.getWorld(), callback);
    }

    public void deleteComponent(IMapComponent component)
    {
        if (component.isPlaced())
        {
            for (Block block : component.getBlocks())
            {
                block.setType(Material.AIR);
            }
        }

        components.remove(component);
    }

    Set<BlockFace> UNAPPLICABLE = EnumSet.of(BlockFace.WEST_NORTH_WEST,
                                             BlockFace.NORTH_NORTH_WEST,
                                             BlockFace.NORTH_NORTH_EAST,
                                             BlockFace.EAST_NORTH_EAST,
                                             BlockFace.EAST_SOUTH_EAST,
                                             BlockFace.SOUTH_SOUTH_EAST,
                                             BlockFace.SOUTH_SOUTH_WEST,
                                             BlockFace.WEST_SOUTH_WEST,
                                             BlockFace.SELF);
                                             */
}
