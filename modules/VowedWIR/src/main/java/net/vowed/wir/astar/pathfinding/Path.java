package net.vowed.wir.astar.pathfinding;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Created by JPaul on 6/21/2016.
 */
public class Path
{
    private int index = 0;
    private VectorNode[] path;

    public Path(Iterable<VectorNode> nodes)
    {
        this.path = toArray(nodes);
    }

    private VectorNode[] toArray(Iterable<VectorNode> nodes)
    {
        List<VectorNode> nodeList = Lists.newArrayList();
        for (VectorNode node : nodes)
        {
            nodeList.add(node);
        }
        return nodeList.toArray(new VectorNode[nodeList.size()]);
    }

    public Vector getCurrentVector()
    {
        return path[index].getVector();
    }

    public boolean isComplete()
    {
        return index >= path.length;
    }

    public void update()
    {
        if (!isComplete())
        {
            ++index;
        }
    }

    public VectorNode[] getPath()
    {
        return path;
    }

    public List<Location> getPathLocations(World world)
    {
        List<Location> locations = Lists.newArrayList();

        for (VectorNode node : path)
        {
            locations.add(node.getLocation(world));
        }

        return locations;
    }
}
