package net.vowed.core.itempair;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Map;
import java.util.Set;

/**
 * Created by JPaul on 8/24/2016.
 */
public class ItemTuple
{
    private Map<Block, Material> blockMap;
    private Location center;

    public ItemTuple(Map<Block, Material> blockMap, Location center)
    {
        this.blockMap = blockMap;
        this.center = center;
    }

    public Set<Block> getNewBlocks()
    {
        return blockMap.keySet();
    }

    public Material getOldBlock(Block newBlock)
    {
        return blockMap.get(newBlock);
    }

    public Location getCenter()
    {
        return center;
    }
}
