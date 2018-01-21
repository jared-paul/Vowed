package net.vowed.wir.astar.pathfinding;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * Created by JPaul on 6/20/2016.
 */
public class AStarStorage
{
    private Map<VectorNode, Float> closed = Maps.newHashMap();
    private Map<VectorNode, Float> open = Maps.newHashMap();
    public Queue<VectorNode> queue = new PriorityQueue<>();

    public void close(VectorNode node)
    {
        open.remove(node);
        closed.put(node, node.cost);
    }

    public void open(VectorNode node)
    {
        queue.offer(node);
        open.put(node, node.cost);
        closed.remove(node);
    }

    public VectorNode getBestNode()
    {
        return queue.peek();
    }

    public VectorNode removeBestNode()
    {
        VectorNode node = queue.poll();
        return node;
    }

    public boolean shouldExamine(VectorNode neighbour)
    {
        Float openFloat = open.get(neighbour);
        if (openFloat != null && openFloat > neighbour.cost)
        {
            open.remove(neighbour);
            openFloat = null;
        }
        Float closedFloat = closed.get(neighbour);
        if (closedFloat != null && closedFloat > neighbour.cost)
        {
            closed.remove(neighbour);
            closedFloat = null;
        }

        return closedFloat == null && openFloat == null;
    }

    public static final Supplier<AStarStorage> FACTORY = AStarStorage::new;
}
