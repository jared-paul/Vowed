package net.vowed.wir.astar.pathfinding.sources;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Created by JPaul on 6/20/2016.
 */
public abstract class BlockSource
{
    public abstract int getBlockID(int x, int y, int z);

    public Material getBlockType(int x, int y, int z)
    {
        return Material.getMaterial(getBlockID(x, y, z));
    }

    public Material getBlockType(Vector location)
    {
        return getBlockType(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public abstract World getWorld();
}
