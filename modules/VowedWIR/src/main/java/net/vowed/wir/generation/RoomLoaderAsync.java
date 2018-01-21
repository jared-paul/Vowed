package net.vowed.wir.generation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.wir.Tuple;
import net.vowed.wir.ComponentTuple;
import net.vowed.wir.Map;
import net.vowed.wir.RoomComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 9/8/2016.
 */
public class RoomLoaderAsync implements Loader
{
    Map map;

    List<RoomComponent> rooms = Lists.newArrayList();
    boolean isDone = false;
    boolean isProcessing = false;

    public RoomLoaderAsync(Map map)
    {
        this.map = map;
    }

    @Override
    public void load()
    {
        isProcessing = true;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                List<Location> triedLocations = Lists.newArrayList();

                isProcessing = true;

                int tickCounter = 0;
                int i = 0;

                RoomComponent start = makeComponent(map.getMinimumPoint(), 17, "1");
                start.setDoneProcessing(true);
                map.setStart(start);

                RoomComponent end = makeComponent(map.getMaximumPoint(), 17, "1");
                end.setDoneProcessing(true);
                map.setEnd(end);


                rooms.add(start);

                while (i < 15)
                {
                    if (tickCounter >= 200)
                    {
                        tickCounter = 0;
                    }

                    RoomComponent previousRoom = rooms.get(i);

                    int xBound = previousRoom.getLocation().getBlockX() + (previousRoom.getRadius() * 3);
                    int zBound = previousRoom.getLocation().getBlockZ() + (previousRoom.getRadius() * 3);

                    int randomX = random.nextInt(Math.abs(xBound - previousRoom.getLocation().getBlockX()));
                    int randomZ = random.nextInt(Math.abs(zBound - previousRoom.getLocation().getBlockZ()));

                    Location location = new Location(map.getMinimumPoint().getWorld(), previousRoom.getLocation().getBlockX() + randomX, previousRoom.getFloorLevel(), previousRoom.getLocation().getBlockZ() + randomZ);

                    /*double distance = location.distanceSquared(previousRoom.getLocation());

                    if (i != 0)
                    {
                        while (distance <= previousRoom.getRadius() * 2 || distance >= (previousRoom.getRadius() * 2) + 10)
                        {
                            randomX = random.nextInt(Math.abs(pre - map.getMinimumPoint().getBlockX()));
                            randomZ = random.nextInt(Math.abs(map.getMaximumPoint().getBlockZ() - map.getMinimumPoint().getBlockZ()));

                            location = new Location(map.getMinimumPoint().getWorld(), map.getMinimumPoint().getBlockX() + randomX, map.getMinimumPoint().getBlockY(), map.getMinimumPoint().getBlockZ() + randomZ);
                        }
                    }
                    */

                    RoomComponent room = makeComponent(location, getBiasedRandom(16, 13, 16), "1");

                    room.setDoneProcessing(true);

                    rooms.add(room);
                    map.addComponent(room);

                    Vowed.LOG.debug(i);

                    i++;

                    tickCounter++;
                }

                rooms.add(end);

                isDone = true;
                isProcessing = false;
            }
        }.runTaskAsynchronously(Vowed.getPlugin());
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

    public void separateRooms(List<RoomComponent> rooms)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (RoomComponent room : rooms)
        {
            Location location;

            while (!room.canFit(rooms))
            {
                int randomX = random.nextInt(Math.abs(map.getMaximumPoint().getBlockX() - map.getMinimumPoint().getBlockX()));
                int randomZ = random.nextInt(Math.abs(map.getMaximumPoint().getBlockZ() - map.getMinimumPoint().getBlockZ()));

                location = new Location(map.getMinimumPoint().getWorld(), map.getMinimumPoint().getBlockX() + randomX, map.getMinimumPoint().getBlockY(), map.getMinimumPoint().getBlockZ() + randomZ);
                room = makeComponent(location, room.getRadius());
            }
        }
    }

    public Location getSuitableLocation(RoomComponent previousRoom)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int radius = previousRoom.getRadius();
        int biasedRandom = getBiasedRandom(16, 13, 16);

        int randomRadiusX = (radius * (random.nextBoolean() ? 1 : -1)) * 2;
        int randomRadiusZ = (radius * (random.nextBoolean() ? 1 : -1)) * 2;

        int modifierX = radius + (random.nextInt(radius - 2) * (random.nextBoolean() ? 1 : -1));
        int modifierZ = radius + (random.nextInt(radius - 2) * (random.nextBoolean() ? 1 : -1));

        Location location;

        boolean bool = random.nextBoolean();

        if (bool)
        {
            location = new Location(map.getMinimumPoint().getWorld(), previousRoom.getLocation().getBlockX() + randomRadiusX, previousRoom.getLocation().getBlockY(), previousRoom.getLocation().getBlockZ() + modifierZ);
        }
        else
        {
            location = new Location(map.getMinimumPoint().getWorld(), previousRoom.getLocation().getBlockX() + modifierX, previousRoom.getLocation().getBlockY(), previousRoom.getLocation().getBlockZ() + randomRadiusZ);
        }

        return location;
    }

    public int getBiasedRandom(int bias, int min, int max)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        int chance = random.nextInt(100);

        int randomNumber;

        if (chance > 30)
        {
            randomNumber = bias;
        }
        else
        {
            randomNumber = random.nextInt(min, max);
        }

        return randomNumber;
    }

    public RoomComponent makeComponent(Location center, int randomSize)
    {
        return makeComponent(center, randomSize, "");
    }

    public RoomComponent makeComponent(Location center, int radius, String test)
    {
        radius = 16;

        ComponentTuple componentInfo = createSquare(center, radius);

        RoomComponent room = new RoomComponent(map, "room", componentInfo.locations, componentInfo.max, componentInfo.min, center, componentInfo.radius, componentInfo.length, componentInfo.width, componentInfo.height);

        List<Location> locations = RoomUtil.getOuterRing(room);

        for (Location location : locations)
        {
            for (int y = 0; y < room.getHeight() + 1; y++)
            {
                room.addWallLocation(location.clone().add(0, y, 0));
            }
        }

        return room;
    }

    private ComponentTuple createSquare(Location center, int radius)
    {
        java.util.Map<Location, Tuple<Material, Byte>> locations = Maps.newHashMap();
        List<Location> outerRing = RoomUtil.getRing(center, radius);
        List<Location> innerOuterRing = RoomUtil.getRing(center, radius - 1);

        int blockX = center.getBlockX();
        int blockY = center.getBlockY();
        int blockZ = center.getBlockZ();

        Location min = new Location(center.getWorld(), blockX - radius, center.getBlockY(), blockZ - radius);
        Location max = new Location(center.getWorld(), blockX + radius, center.getBlockY() + 7, blockZ + radius);
        int height = 7;
        int length = radius * 2;
        int width = radius * 2;

        for (int x = blockX - radius; x <= blockX + radius; x++)
        {
            for (int y = blockY; y <= blockY + 7; y++)
            {
                for (int z = blockZ - radius; z <= blockZ + radius; z++)
                {
                    Location location = new Location(center.getWorld(), x, y, z);

                    if (outerRing.contains(location) || blockY == y)
                    {
                        ThreadLocalRandom random = ThreadLocalRandom.current();

                        int chance = random.nextInt(100);
                        byte data = 0;

                        if (chance > 70)
                        {
                            data = (byte) random.nextInt(4);
                        }

                        locations.put(location, new Tuple<>(Material.SMOOTH_BRICK, data));
                    }
                    else
                    {
                        locations.put(location, new Tuple<>(Material.AIR, (byte) 0));
                    }
                }
            }
        }

        return new ComponentTuple(min, max, center, radius, length, width, height, locations);
    }

    public boolean isLocationGood(RoomComponent roomComponent, List<RoomComponent> rooms)
    {
        return roomComponent.canFit(rooms);
    }
}
