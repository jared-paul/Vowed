package net.vowed.wir;

import net.vowed.api.wir.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Map;

/**
 * Created by JPaul on 9/4/2016.
 */
public class ComponentTuple
{
    public Location min;
    public Location max;
    public Location center;
    public int radius;
    public int length;
    public int width;
    public int height;
    public Map<Location, Tuple<Material, Byte>> locations;

    public ComponentTuple(Location min, Location max, Location center, int radius, int length, int width, int height, Map<Location, Tuple<Material, Byte>> locations)
    {
        this.min = min;
        this.max = max;
        this.center = center;
        this.radius = radius;
        this.length = length;
        this.width = width;
        this.height = height;
        this.locations = locations;
    }
}
