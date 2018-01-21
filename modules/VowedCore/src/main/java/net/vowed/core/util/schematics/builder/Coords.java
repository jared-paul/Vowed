package net.vowed.core.util.schematics.builder;


import com.sk89q.worldedit.blocks.BaseBlock;

/**
 * Created by JPaul on 6/9/2016.
 */
public class Coords implements Comparable<Coords>
{
    public int x, y, z;
    public BaseBlock baseBlock;

    public Coords(int x, int y, int z, BaseBlock baseBlock)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.baseBlock = baseBlock;
    }

    @Override
    public int compareTo(Coords coords)
    {
        return y - coords.y;
    }
}
