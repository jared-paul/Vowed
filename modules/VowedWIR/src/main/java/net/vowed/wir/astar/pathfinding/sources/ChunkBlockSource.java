package net.vowed.wir.astar.pathfinding.sources;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by JPaul on 6/21/2016.
 */
public class ChunkBlockSource extends ChunkBlockSourceCache<Chunk>
{
    public ChunkBlockSource(Location location, float radius)
    {
        super(location, radius);
    }

    protected ChunkBlockSource(World world, int x, int z, float radius)
    {
        super(world, x, z, radius);
    }

    @Override
    protected Chunk getChunkObject(int x, int z)
    {
        return world.getChunkAt(x, z);
    }

    @Override
    protected int getID(Chunk chunk, int x, int y, int z)
    {
        return chunk.getBlock(x, y, z).getTypeId();
    }

    @Override
    protected int getLightLevel(Chunk chunk, int x, int y, int z)
    {
        return chunk.getBlock(x, y, z).getLightLevel();
    }
}
