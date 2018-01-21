package net.vowed.wir.astar.pathfinding;

import net.vowed.wir.astar.pathfinding.examiners.MinecraftBlockExaminer;
import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Created by JPaul on 6/21/2016.
 */
public class VectorGoal
{
    final Vector goal;
    private final float leeway;

    public VectorGoal(Location destination, float leeway)
    {
        if (!MinecraftBlockExaminer.canStandIn(destination.getBlock().getType()))
        {
            destination = MinecraftBlockExaminer.findValidLocation(destination, 1);
        }
        this.goal = destination.toVector();
        this.leeway = leeway;
        goal.setX(goal.getBlockX()).setY(goal.getBlockY()).setZ(goal.getBlockZ());
    }

    //g
    public float getDistance(VectorNode from, VectorNode to)
    {
        return from.distanceTo(to);
    }

    //f
    public float getInitialCost(VectorNode node)
    {
        return node.heuristicDistance(goal);
    }

    //h
    public float heuristicDistance(VectorNode from)
    {
        return from.heuristicDistance(goal);
    }

    public boolean isFinished(VectorNode node)
    {
        double distanceSquared = node.getVector().distanceSquared(goal);
        return goal.equals(node.location) || distanceSquared <= leeway;
    }
}
