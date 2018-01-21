package net.vowed.wir;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.vowed.api.wir.IMapComponent;
import net.vowed.api.wir.Tuple;
import net.vowed.wir.generation.RoomUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.*;
import java.util.Map;

/**
 * Created by JPaul on 8/24/2016.
 */
public abstract class AbstractMapComponent implements IMapComponent
{
    UUID uuid;

    String name;
    boolean isDoneProcessing = false;
    boolean placed;
    Location center;
    Location max;
    Location min;

    int radius = -1;
    int length = -1;
    int width = -1;
    int height = -1;

    Map<Location, Tuple<Material, Byte>> locations = Maps.newHashMap();

    public AbstractMapComponent(String name, Map<Location, Tuple<Material, Byte>> locations, Location max, Location min, Location center, int radius, int length, int width, int height)
    {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.locations = locations;
        this.max = max;
        this.min = min;
        this.center = center;
        this.radius = radius;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public abstract IMapComponent newInstance();

    @Override
    public UUID getUUID()
    {
        return uuid;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isDoneProcessing()
    {
        return isDoneProcessing;
    }

    @Override
    public void setDoneProcessing(boolean doneProcessing)
    {
        isDoneProcessing = doneProcessing;
    }

    @Override
    public Location getMaximum()
    {
        return max;
    }

    @Override
    public Location getMinimum()
    {
        return min;
    }

    @Override
    public int getLength()
    {
        return length;
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public int getRadius()
    {
        return radius;
    }

    public int getFloorLevel()
    {
        return center.getBlockY();
    }

    @Override
    public Location getLocation()
    {
        return center;
    }

    @Override
    public void setLocation(Location center)
    {
        this.center = center;
    }

    @Override
    public Map<Location, Tuple<Material, Byte>> getVirtualBlocks()
    {
        return locations;
    }

    @Override
    public void setVirtualBlocks(Map<Location, Tuple<Material, Byte>> virtualBlocks)
    {
        this.locations = virtualBlocks;
    }

    @Override
    public void updateVirtualBlocks()
    {
        for (Location location : locations.keySet())
        {
            locations.put(location, new Tuple<>(location.getBlock().getType(), location.getBlock().getData()));
        }
    }

    @Override
    public boolean isPlaced()
    {
        return placed;
    }

    @Override
    public void loadInWorld()
    {
        this.isDoneProcessing = true;

        for (Map.Entry<Location, Tuple<Material, Byte>> entry : locations.entrySet())
        {
            entry.getKey().getBlock().setTypeIdAndData(entry.getValue().getA().getId(), entry.getValue().getB(), true);
        }

        placed = true;
    }

    public List<Location> getOuterRingWall(RoomComponent room)
    {
        List<Location> outerRingWall = Lists.newArrayList();

        List<Location> outerRing = RoomUtil.getOuterRing(room);
        for (Location location : outerRing)
        {
            int y = 0;
            while (y < height)
            {
                outerRingWall.add(location.clone().add(0, y, 0));

                y++;
            }
        }

        return outerRingWall;
    }

    private List<Location> getInnerOuterRing(RoomComponent room)
    {
        Location center = room.getLocation();
        int radius = (room.getLength() / 2) - 1;

        return getOuterRing(center, radius);
    }

    private List<Location> getOuterRing(Location center, int radius)
    {
        int blockX = center.getBlockX();
        int blockZ = center.getBlockZ();

        radius--;

        List<Location> outerRingDiscluded = Lists.newArrayList();

        for (int x = blockX - radius; x <= blockX + radius; x++)
        {
            for (int z = blockZ - radius; z <= blockZ + radius; z++)
            {
                Location location = new Location(center.getWorld(), x, center.getY(), z);
                outerRingDiscluded.add(location);
            }
        }

        List<Location> outerRingIncluded = Lists.newArrayList();

        radius++;

        for (int x = blockX - radius; x <= blockX + radius; x++)
        {
            for (int z = blockZ - radius; z <= blockZ + radius; z++)
            {
                Location location = new Location(center.getWorld(), x, center.getY(), z);
                outerRingIncluded.add(location);
            }
        }

        Iterator<Location> locationIterator = outerRingIncluded.iterator();
        while (locationIterator.hasNext())
        {
            Location location = locationIterator.next();

            if (outerRingDiscluded.contains(location))
            {
                locationIterator.remove();
            }
        }

        return outerRingIncluded;
    }

    private final List<BlockFace> DIRECTIONS = Arrays.asList(BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.EAST);
}
