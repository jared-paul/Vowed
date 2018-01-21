package net.vowed.wir;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

/**
 * Created by JPaul on 9/18/2016.
 */
public class Hallway
{
    List<Location> locations = Lists.newArrayList();
    java.util.Map<RoomComponent, List<Location>> intersectionPoints = Maps.newHashMap();

    public Hallway(List<Location> locations, java.util.Map<RoomComponent, List<Location>> intersectionPoints)
    {
        this.locations = locations;
        this.intersectionPoints = intersectionPoints;
    }

    public Hallway() {}

    public List<Location> getLocations()
    {
        return locations;
    }

    public void setLocations(List<Location> locations)
    {
        this.locations = locations;
    }

    public Map<RoomComponent, List<Location>> getIntersectionPoints()
    {
        return intersectionPoints;
    }

    public List<Location> getIntersectionPoints(RoomComponent room)
    {
        return intersectionPoints.get(room);
    }

    public void addIntersection(RoomComponent intersectingRoom, Location location)
    {
        if (intersectionPoints.get(intersectingRoom) == null)
        {
            intersectionPoints.put(intersectingRoom, Lists.newArrayList());
        }

        intersectionPoints.get(intersectingRoom).add(location);
    }

    public void removeIntersection(RoomComponent room, Location location)
    {
        if (intersectionPoints.get(room) != null)
        {
            intersectionPoints.get(room).remove(location);
        }
    }
}
