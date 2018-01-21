package net.vowed.wir.astar.pathfinding.examiners;

import net.vowed.wir.RoomComponent;
import net.vowed.wir.astar.pathfinding.VectorNode;
import net.vowed.wir.astar.pathfinding.sources.BlockSource;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by JPaul on 6/21/2016.
 */
public class MinecraftBlockExaminer implements BlockExaminer
{
    private static final Vector UP = new Vector(0, 1, 0);
    private static final Vector DOWN = new Vector(0, -1, 0);
    private static final Set<Material> UNWALKABLE = EnumSet.of(Material.AIR, Material.LAVA, Material.STATIONARY_LAVA,
                                                               Material.CACTUS);
    private static final Set<Material> NOT_JUMPABLE = EnumSet.of(Material.FENCE, Material.IRON_FENCE,
                                                                 Material.NETHER_FENCE, Material.COBBLE_WALL, Material.SPRUCE_FENCE, Material.BIRCH_FENCE,
                                                                 Material.JUNGLE_FENCE, Material.ACACIA_FENCE, Material.DARK_OAK_FENCE);
    @Override
    public float getCost(BlockSource blockSource, VectorNode node)
    {
        /*
        Vector position = node.getVector();

        Material above = blockSource.getBlockType(position.clone().add(UP));
        Material below = blockSource.getBlockType(position.clone().add(DOWN));
        Material posMaterial = blockSource.getBlockType(position);

        if (above == Material.WEB || posMaterial == Material.WEB)
        {
            return 1F;
        }
        if (below == Material.SOUL_SAND || below == Material.ICE)
        {
            return 1F;
        }
        if (isLiquid(above, below, posMaterial))
        {
            return 0.5F;
        }
        */

        return 0.5F;
    }

    @Override
    public PassableState isPassable(BlockSource blockSource, VectorNode node, RoomComponent start, RoomComponent end)
    {
        Location location = node.getLocation(start.getLocation().getWorld());

        if (location.getBlockY() != start.getFloorLevel())
            return PassableState.UNPASSABLE;

        if (start.isNearCorner(location))
            return PassableState.UNPASSABLE;

        /*
        Vector position = node.getVector();
        Material below = blockSource.getBlockType(position.clone().add(DOWN));

        if (!below.isBlock() || !canStandOn(below))
        {
            return PassableState.UNPASSABLE;
        }
        if (!canJumpOn(below))
        {
            if (node.getParent() == null)
            {
                return PassableState.UNPASSABLE;
            }

            Vector parentPosition = node.getParent().getVector();
            if ((parentPosition.getX() != position.getX() || parentPosition.getZ() != position.getZ()) && position.clone().subtract(node.getParent().getVector()).getY() == 1)
            {
                return PassableState.UNPASSABLE;
            }
        }
        */

        return PassableState.PASSABLE;
    }

    public static Location findValidLocation(Location location, int radius)
    {
        Block base = location.getBlock();
        if (canStandIn(base.getType()) && canStandOn(base.getRelative(BlockFace.DOWN)))
            return location;
        for (int y = 0; y <= radius; y++)
        {
            for (int x = -radius; x <= radius; x++)
            {
                for (int z = -radius; z <= radius; z++)
                {
                    Block relative = base.getRelative(x, y, z);
                    if (canStandIn(relative.getRelative(BlockFace.UP).getType()) && canStandIn(relative.getType())
                            && canStandOn(base.getRelative(BlockFace.DOWN)))
                    {
                        return relative.getLocation();
                    }
                }
            }
        }
        return location;
    }

    public static boolean canJumpOn(Material material)
    {
        return !NOT_JUMPABLE.contains(material);
    }

    public static boolean canStandOn(Block block)
    {
        Block above = block.getRelative(BlockFace.UP);

        return canStandOn(block.getType()) && canStandIn(above.getType()) && canStandIn(above.getRelative(BlockFace.UP).getType());
    }

    public static boolean canStandOn(Material material)
    {
        return material == Material.AIR || !UNWALKABLE.contains(material) && material.isSolid();
    }

    public static boolean canStandIn(Material... mat)
    {
        boolean passable = true;
        for (Material m : mat)
        {
            passable &= !m.isSolid();
        }
        return passable;
    }

    public static boolean isLiquid(Material... materials)
    {
        return contains(materials, Material.WATER, Material.STATIONARY_WATER, Material.LAVA, Material.STATIONARY_LAVA);
    }

    private static boolean contains(Material[] search, Material... find)
    {
        for (Material haystack : search)
        {
            for (Material needle : find)
            {
                if (haystack == needle)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
