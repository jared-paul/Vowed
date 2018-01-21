package net.vowed.wir.astar.pathfinding;

import net.vowed.wir.RoomComponent;
import net.vowed.wir.astar.pathfinding.examiners.MinecraftBlockExaminer;
import net.vowed.wir.astar.pathfinding.sources.ChunkBlockSource;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Created by JPaul on 6/22/2016.
 */
public class AStarNavigation
{
    private final RoomComponent destination;
    private final RoomComponent start;

    private final VectorGoal goal;
    private final VectorNode startNode;

    private Path path;
    private Vector nextDest;

    private final AStarMachine ASTAR = AStarMachine.createWithDefaultStorage();

    public AStarNavigation(RoomComponent start, RoomComponent destination)
    {
        this.start = start;
        this.destination = destination;

        this.goal = new VectorGoal(destination.getLocation(), 1);
        this.startNode = new VectorNode(start, destination, goal, start.getLocation(), new ChunkBlockSource(start.getLocation(), 64), new MinecraftBlockExaminer());
    }

    public void createPath(World world, LocationCallback locationCallback)
    {
        if (path != null)
            locationCallback.onSuccess(path.getPathLocations(world));


        path = ASTAR.runFully(goal, startNode, 500000);

        locationCallback.onSuccess(path.getPathLocations(world));
    }

    public RoomComponent getDestination()
    {
        return destination;
    }

    public void stop()
    {
        path = null;
    }

    public interface LocationCallback
    {
        void onSuccess(List<Location> locations);
    }
}
