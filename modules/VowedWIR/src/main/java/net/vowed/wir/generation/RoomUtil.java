package net.vowed.wir.generation;

import com.google.common.collect.Lists;
import net.vowed.wir.Hallway;
import net.vowed.wir.RoomComponent;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JPaul on 9/8/2016.
 */
public class RoomUtil
{
    public static List<RoomComponent> getRooms(List<RoomComponent> rooms, Hallway hallway)
    {
        List<RoomComponent> intersectingRooms = Lists.newArrayList();

        for (RoomComponent room : rooms)
        {
            for (Location location : hallway.getLocations())
            {
                if (room.getVirtualBlocks().containsKey(location))
                {
                    intersectingRooms.add(room);
                }
            }
        }

        return intersectingRooms;
    }

    public static RoomComponent getRoom(List<RoomComponent> rooms, Location intersectingLocation)
    {
        for (RoomComponent room : rooms)
        {
            if (room.getVirtualBlocks().containsKey(intersectingLocation))
            {
                return room;
            }
        }

        return null;
    }

    public static List<Location> getInnerOuterRing(RoomComponent room)
    {
        Location center = room.getLocation();
        int radius = room.getRadius() - 1;

        return getRing(center, radius);
    }

    public static List<Location> getOuterRing(RoomComponent room)
    {
        return getRing(room.getLocation(), room.getRadius());
    }

    public static List<Location> getRing(Location center, int radius)
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

    public static List<RoomComponent> getMainRooms(Collection<RoomComponent> rooms)
    {
        List<RoomComponent> mainRooms = Lists.newArrayList();

        int widthMean = getWidthMean(rooms);
        int lengthMean = getLengthMean(rooms);

        for (RoomComponent room : rooms)
        {
            if (room.getLength() >= lengthMean && room.getWidth() >= widthMean)
            {
                mainRooms.add(room);
            }
        }

        return mainRooms;
    }

    public static int getWidthMean(Collection<RoomComponent> rooms)
    {
        List<Integer> widths = Lists.newArrayList();

        for (RoomComponent room : rooms)
        {
            widths.add(room.getWidth());
        }

        Integer[] widthArray = widths.toArray(new Integer[widths.size()]);

        return mean(ArrayUtils.toPrimitive(widthArray));
    }

    public static int getLengthMean(Collection<RoomComponent> rooms)
    {
        List<Integer> lengths = Lists.newArrayList();

        for (RoomComponent room : rooms)
        {
            lengths.add(room.getLength());
        }

        Integer[] widthArray = lengths.toArray(new Integer[lengths.size()]);

        return mean(ArrayUtils.toPrimitive(widthArray));
    }


    public static int mean(int[] m)
    {
        double sum = 0;
        for (int aM : m)
        {
            sum += aM;
        }
        return (int) (sum / m.length);
    }
}
