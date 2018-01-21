package net.vowed.wir.astar.pathfinding.sources;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by JPaul on 6/21/2016.
 */
public class ChunkSnapshotBlockSource extends ChunkBlockSourceCache<ChunkSnapshot>
{
    protected ChunkSnapshotBlockSource(Location location, float radius)
    {
        super(location, radius);
    }

    protected ChunkSnapshotBlockSource(World world, int x, int z, float radius)
    {
        super(world, x, z, radius);
    }

    @Override
    protected ChunkSnapshot getChunkObject(int x, int z)
    {
        return world.getChunkAt(x, z).getChunkSnapshot(false, false, false);
    }

    @Override
    protected int getID(ChunkSnapshot chunk, int x, int y, int z)
    {
        return chunk.getBlockTypeId(x, y, z);
    }

    @Override
    protected int getLightLevel(ChunkSnapshot chunk, int x, int y, int z)
    {
        return Math.min(15, chunk.getBlockSkyLight(x, y, z) + chunk.getBlockEmittedLight(x, y, z));
    }
}
