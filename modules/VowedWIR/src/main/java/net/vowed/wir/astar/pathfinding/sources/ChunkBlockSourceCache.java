package net.vowed.wir.astar.pathfinding.sources;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by JPaul on 6/20/2016.
 */
public abstract class ChunkBlockSourceCache<T> extends BlockSource
{
    private final Object[][] chunks;
    private final int chunkX;
    private final int chunkZ;
    protected final World world;

    protected ChunkBlockSourceCache(Location location, float radius)
    {
        this(location.getWorld(), location.getBlockX(), location.getBlockZ(), radius);
    }

    protected ChunkBlockSourceCache(World world, int x, int z, float radius)
    {
        this(world, (int) (x - radius), (int) (z - radius), (int) (x + radius), (int) (z + radius));
    }

    protected ChunkBlockSourceCache(World world, int minX, int minZ, int maxX, int maxZ)
    {
        this.world = world;
        this.chunkX = minX >> 4;
        this.chunkZ = minZ >> 4;

        int maxChunkX = maxX >> 4;
        int maxChunkZ = maxZ >> 4;

        chunks = new Object[maxChunkX - chunkX + 1][maxChunkZ - chunkZ + 1];

        for (int x = chunkX; x < maxChunkX; x++)
        {
            for (int z = chunkZ; z < maxChunkZ; z++)
            {
                chunks[x - chunkX][z - chunkZ] = getChunkObject(x, z);
            }
        }
    }

    protected abstract T getChunkObject(int x, int z);

    protected abstract int getID(T chunk, int x, int y, int z);

    protected abstract int getLightLevel(T chunk, int x, int y, int z);

    private T getSpecific(int x, int z)
    {
        int xx = (x >> 4) - chunkX;
        int zz = (z >> 4) - chunkZ;

        if (xx >= 0 && xx < chunks.length)
        {
            Object[] inner = chunks[xx];

            if (zz >= 0 && zz < inner.length)
            {
                return (T) inner[zz];
            }
        }

        return null;
    }

    @Override
    public int getBlockID(int x, int y, int z)
    {
        T chunk = getSpecific(x, z);

        if (chunk != null)
        {
            return getID(chunk, x, y, z);
        }

        return world.getBlockTypeIdAt(x, y, z);
    }

    @Override
    public World getWorld()
    {
        return world;
    }
}
