package net.vowed.wir;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.slikey.effectlib.util.ParticleEffect;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.wir.IMapComponent;
import net.vowed.api.wir.Tuple;
import net.vowed.wir.generation.RoomUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 8/24/2016.
 */
public class RoomComponent extends AbstractMapComponent implements Comparable<RoomComponent>
{
    List<RoomComponent> hasHallway = Lists.newArrayList();
    List<Location> walls = Lists.newArrayList();
    Map<BlockFace, List<Location>> doorways = Maps.newHashMap();
    Map<BlockFace, List<Location>> cornerDoorways = Maps.newHashMap();
    net.vowed.wir.Map map;

    boolean modified = false;
    boolean hasSpawnedMobs = false;

    public RoomComponent(net.vowed.wir.Map map, String name, Map<Location, Tuple<Material, Byte>> locations, Location max, Location min, Location center, int radius, int length, int width, int height)
    {
        super(name, locations, max, min, center, radius, length, width, height);
        this.map = map;
    }

    @Override
    public IMapComponent newInstance()
    {
        return new RoomComponent(map, name, locations, max, min, center, radius, length, width, height);
    }

    public void addDoorway(BlockFace direction, Location doorway)
    {
        if (doorways.get(direction) == null)
            doorways.put(direction, Lists.newArrayList());

        doorways.get(direction).add(doorway);
    }

    public void addCornerDoorway(BlockFace direction, Location doorway)
    {
        if (cornerDoorways.get(direction) == null)
            cornerDoorways.put(direction, Lists.newArrayList());

        cornerDoorways.get(direction).add(doorway);
    }

    public List<Location> getCornerDoorways(BlockFace direction)
    {
        if (cornerDoorways.get(direction) == null)
            cornerDoorways.put(direction, Lists.newArrayList());

        return cornerDoorways.get(direction);
    }

    public List<Location> getDoorways(BlockFace direction)
    {
        if (doorways.get(direction) == null)
            doorways.put(direction, Lists.newArrayList());

        return doorways.get(direction);
    }

    public List<Location> getAllCornerDoorways()
    {
        List<Location> doorways = Lists.newArrayList();

        this.cornerDoorways.values().forEach(doorwayLocations -> doorwayLocations.forEach(doorways::add));

        return doorways;
    }

    public List<Location> getAllDoorways()
    {
        List<Location> doorways = Lists.newArrayList();

        this.doorways.values().forEach(doorwayLocations -> doorwayLocations.forEach(doorways::add));

        return doorways;
    }

    public boolean hasDoorways()
    {
        return !doorways.isEmpty();
    }

    public List<Location> getWallLocations()
    {
        return walls;
    }

    public void addWallLocation(Location wallLocation)
    {
        walls.add(wallLocation);
    }

    public void setHasHallwayTo(RoomComponent roomComponent)
    {
        this.hasHallway.add(roomComponent);
    }

    public boolean hasHallway(RoomComponent roomComponent)
    {
        return hasHallway.contains(roomComponent);
    }

    public boolean hasHallways()
    {
        return !hasHallway.isEmpty();
    }

    public boolean hasBeenModified()
    {
        return modified;
    }

    public void setModified(boolean modified)
    {
        this.modified = modified;
    }

    public boolean hasSpawnedMobs()
    {
        return hasSpawnedMobs;
    }

    public void spawnMobs()
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        List<Location> innerOuterRing = RoomUtil.getInnerOuterRing(this);
        innerOuterRing.forEach(location -> location.add(0, 1, 0));

        List<Location> outerRing = RoomUtil.getOuterRing(this);
        outerRing.forEach(location -> location.add(0, 1, 0));

        int mobSize = radius / 2;
        int counter = 0;
        while (counter < mobSize)
        {
            int randomX = random.nextInt(Math.abs(max.getBlockX() - min.getBlockX()));
            int randomZ = random.nextInt(Math.abs(max.getBlockZ() - min.getBlockZ()));

            Location randomLocation = new Location(center.getWorld(), min.getBlockX() + randomX, getFloorLevel() + 1, min.getBlockZ() + randomZ);

            while (isNearWall(randomLocation))
            {
                randomX = random.nextInt(Math.abs(max.getBlockX() - min.getBlockX()));
                randomZ = random.nextInt(Math.abs(max.getBlockZ() - min.getBlockZ()));

                randomLocation = new Location(center.getWorld(), min.getBlockX() + randomX + 0.5, getFloorLevel() + 1, min.getBlockZ() + randomZ + 0.5);
            }

            final Location finalRandomLocation = randomLocation;

            new BukkitRunnable()
            {
                int timerCounter = 0;

                @Override
                public void run()
                {
                    if (timerCounter >= 3)
                    {
                        cancel();
                        return;
                    }

                    for (Location location : getCircle(finalRandomLocation, 1, 20))
                    {
                        ParticleEffect.FLAME.display(0, 0, 0, 0, 1, location.clone().add(0, 0.2, 0), 64);
                    }

                    timerCounter++;
                }
            }.runTaskTimer(Vowed.getPlugin(), 0, 20);

            Bukkit.getScheduler().runTaskLater(Vowed.getPlugin(), () ->
            {
                Zombie zombie = (Zombie) finalRandomLocation.getWorld().spawnEntity(finalRandomLocation, EntityType.ZOMBIE);
            }, 60);

            counter++;
        }

        hasSpawnedMobs = true;
    }

    public boolean isNearWall(Location location)
    {
        List<Location> innerOuterRing = RoomUtil.getInnerOuterRing(this);

        for (Location wallLocation : innerOuterRing)
        {
            if (wallLocation.distanceSquared(location) <= 4)
            {
                return true;
            }
        }

        return false;
    }

    public List<Location> getCircle(Location center, double radius, int amount)
    {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        List<Location> locations = Lists.newArrayList();
        for (int i = 0; i < amount; i++)
        {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }

        return locations;
    }

    @Override
    public void updateVirtualBlocks()
    {
        super.updateVirtualBlocks();

        for (Location wallLocation : walls)
        {
            locations.put(wallLocation, new Tuple<>(wallLocation.getBlock().getType(), wallLocation.getBlock().getData()));
        }
    }

    @Override
    public void loadInWorld()
    {
        this.isDoneProcessing = true;

        for (Map.Entry<Location, Tuple<Material, Byte>> entry : locations.entrySet())
        {
            Material material = entry.getValue().getA();
            byte data = entry.getValue().getB();

            if (material != Material.AIR)
            {
                if (data != 0)
                {
                    entry.getKey().getBlock().setTypeIdAndData(material.getId(), data, true);
                }
                else
                {
                    entry.getKey().getBlock().setType(material);
                }
            }
        }
        for (Location location : walls)
        {
            ThreadLocalRandom random = ThreadLocalRandom.current();

            int chance = random.nextInt(100);
            byte data = 0;

            if (chance > 70)
            {
                data = (byte) random.nextInt(4);
            }

            location.getBlock().setTypeIdAndData(Material.SMOOTH_BRICK.getId(), data, true);
        }

        placed = true;
    }

    public boolean canFit(List<RoomComponent> components)
    {
        for (RoomComponent component : components)
        {
            if (!component.getUUID().equals(this.uuid))
            {
                for (Location location : component.getVirtualBlocks().keySet())
                {
                    if (!getWallLocations().contains(location) && !component.getWallLocations().contains(location))
                    {
                        if (locations.containsKey(location))
                        {
                            Vowed.LOG.warning(location.toString());

                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean isBesideWall(Location location)
    {
        for (Location wallLocation : walls)
        {
            if (wallLocation.distanceSquared(location) <= 0.5)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isBesideDoorway(Location location)
    {
        for (List<Location> doorways : this.doorways.values())
        {
            for (Location doorway : doorways)
            {
                if (location.distanceSquared(doorway) < 4)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public List<Location> getCornerPillars()
    {
        List<Location> pillars = Lists.newArrayList();

        for (Location location : getCorners())
        {
            for (int i = 0; i < height; i++)
            {
                pillars.add(location);
            }
        }

        return pillars;
    }

    public List<Location> getCorners()
    {
        int radius = getRadius() - 1;

        Location corner1 = new Location(center.getWorld(), center.getBlockX() + radius, center.getBlockY(), center.getBlockZ() + radius);
        Location corner2 = new Location(center.getWorld(), center.getBlockX() + radius, center.getBlockY(), center.getBlockZ() - radius);
        Location corner3 = new Location(center.getWorld(), center.getBlockX() - radius, center.getBlockY(), center.getBlockZ() - radius);
        Location corner4 = new Location(center.getWorld(), center.getBlockX() - radius, center.getBlockY(), center.getBlockZ() + radius);

        return Lists.newArrayList(corner1, corner2, corner3, corner4);
    }

    public boolean isNearCorner(Location location)
    {
        List<Boolean> booleans = Lists.newArrayList();

        for (Location corner : getCornerPillars())
        {
            if (corner.distanceSquared(location) < 4)
                booleans.add(true);
            else
                booleans.add(false);
        }

        return booleans.contains(true);
    }

    public void clear()
    {
        for (Location location : locations.keySet())
        {
            location.getBlock().setType(Material.AIR);
        }
        for (Location location : walls)
        {
            location.getBlock().setType(Material.AIR);
        }
    }

    private final Set<BlockFace> DOOR_DIRECTIONS = EnumSet.of(BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SELF);

    @Override
    public int compareTo(RoomComponent o)
    {
        return (int) map.getMinimumPoint().distanceSquared(o.getLocation());
    }
}
