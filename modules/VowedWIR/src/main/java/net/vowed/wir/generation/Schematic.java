package net.vowed.wir.generation;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Map;

/**
 * Created by JPaul on 9/28/2016.
 */
public class Schematic
{
    private Map<Location, Material> blockData;
    private int width;
    private int length;
    private int height;

    public Schematic(Map<Location, Material> blockData, int width, int length, int height)
    {
        this.blockData = blockData;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public Map<Location, Material> getBlockData()
    {
        return blockData;
    }

    public int getWidth()
    {
        return width;
    }

    public int getLength()
    {
        return length;
    }

    public int getHeight()
    {
        return height;
    }
}
