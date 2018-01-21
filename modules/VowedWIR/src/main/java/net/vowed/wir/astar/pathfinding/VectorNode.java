package net.vowed.wir.astar.pathfinding;

import com.google.common.collect.Lists;
import net.vowed.wir.RoomComponent;
import net.vowed.wir.astar.pathfinding.examiners.BlockExaminer;
import net.vowed.wir.astar.pathfinding.examiners.MinecraftBlockExaminer;
import net.vowed.wir.astar.pathfinding.sources.BlockSource;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

/**
 * Created by JPaul on 6/20/2016.
 */
public class VectorNode implements Comparable<VectorNode>
{
    RoomComponent start, end;
    VectorGoal goal;
    Vector location;
    BlockSource blockSource;
    BlockExaminer[] blockExaminers;
    int blockCost = -1;
    List<VectorNode> parents;
    VectorNode parent;

    float cost;
    float estimateCost;
    float heuristic;

    public VectorNode(RoomComponent start, RoomComponent end, VectorGoal goal, Location location, BlockSource blockSource, BlockExaminer... blockExaminers)
    {
        this(start, end, goal, location.toVector(), blockSource, blockExaminers);
    }

    public VectorNode(RoomComponent start, RoomComponent end, VectorGoal goal, Vector location, BlockSource blockSource, BlockExaminer... blockExaminers)
    {
        this.start = start;
        this.end = end;
        this.goal = goal;
        this.location = location.setX(location.getBlockX()).setY(location.getBlockY()).setZ(location.getBlockZ());
        this.blockSource = blockSource;
        this.blockExaminers = blockExaminers == null ? new BlockExaminer[]{new MinecraftBlockExaminer()} : blockExaminers;
    }

    public Vector getVector()
    {
        return location.clone();
    }

    public void setVector(Vector vector)
    {
        this.location = vector;
    }

    public int hashCode()
    {
        final int prime = 31;
        return prime + ((location == null) ? 0 : location.hashCode());
    }

    public Location getLocation(World world)
    {
        return new Location(world, location.getX(), location.getY(), location.getZ());
    }

    public float heuristicDistance(Vector goal)
    {
        return (float) (location.distance(goal) + getBlockCost()) * 1.001F;
    }

    public VectorNode getParent()
    {
        return parent;
    }

    public VectorGoal getGoal()
    {
        return goal;
    }

    public float distanceTo(VectorNode to)
    {
        return (float) location.distance(to.location);
    }

    public Path buildPath()
    {
        Iterable<VectorNode> parents = getParents();
        return new Path(parents);
    }

    private boolean isPassable(VectorNode node)
    {
        boolean passable = false;
        for (BlockExaminer blockExaminer : blockExaminers)
        {
            BlockExaminer.PassableState state = blockExaminer.isPassable(blockSource, node, start, end);
            if (state == BlockExaminer.PassableState.IGNORE)
                continue;
            passable |= state == BlockExaminer.PassableState.PASSABLE;
        }

        return passable;
    }

    public interface BooleanCallback
    {
        void onSuccess(Boolean bool);
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        VectorNode other = (VectorNode) object;

        if (location == null)
        {
            if (other.location != null)
            {
                return false;
            }
        }
        else if (!location.equals(other.location))
        {
            return false;
        }

        return true;
    }

    private float getBlockCost()
    {
        if (blockCost == -1)
        {
            blockCost = 0;
            for (BlockExaminer examiner : blockExaminers)
            {
                blockCost += examiner.getCost(blockSource, this);
            }
        }

        return blockCost;
    }

    private Iterable<VectorNode> getParents()
    {
        if (parents != null)
        {
            return parents;
        }
        parents = Lists.newArrayList();
        VectorNode start = this;
        while (start != null)
        {
            parents.add(start);
            start = start.parent;
        }
        Collections.reverse(parents);
        return parents;
    }

    public Iterable<VectorNode> getNeighbours()
    {
        List<VectorNode> nodes = Lists.newArrayList();
        for (int x = -1; x <= 1; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (x == 0 && y == 0 && z == 0)
                    {
                        continue;
                    }
                    if (x != 0 && z != 0)
                    {
                        continue;
                    }
                    Vector mod = location.clone().add(new Vector(x, y, z));
                    if (mod.equals(location))
                    {
                        continue;
                    }
                    VectorNode sub = getNewNode(mod);
                    if (!isPassable(sub))
                    {
                        continue;
                    }
                    nodes.add(sub);
                }
            }
        }

        return nodes;
    }

    private VectorNode getNewNode(Vector mod)
    {
        return new VectorNode(start, end, goal, mod, blockSource, blockExaminers);
    }

    @Override
    public int compareTo(VectorNode other)
    {
        return Float.compare(cost, other.cost);
    }
}
