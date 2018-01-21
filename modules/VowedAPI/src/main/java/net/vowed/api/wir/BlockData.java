package net.vowed.api.wir;

import org.bukkit.Material;

/**
 * Created by JPaul on 8/30/2016.
 */
public class BlockData
{
    private Material material;
    private byte data;

    public BlockData(Material material, byte data)
    {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial()
    {
        return material;
    }

    public byte getData()
    {
        return data;
    }
}
