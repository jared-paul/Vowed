package net.vowed.wir.astar.pathfinding;

import com.google.common.base.Preconditions;

import java.util.function.Supplier;

/**
 * Created by JPaul on 6/20/2016.
 */
public class AStarMachine
{
    private Supplier<AStarStorage> storageSupplier;

    public AStarMachine(Supplier<AStarStorage> storage)
    {
        this.storageSupplier = storage;
    }

    private void cost(VectorGoal goal, VectorNode node, VectorNode neighbour)
    {
        float g = node.estimateCost + goal.getDistance(node, neighbour);
        float h = goal.heuristicDistance(neighbour);

        neighbour.cost = g + h;
        neighbour.estimateCost = g;
        neighbour.heuristic = h;
    }

    public AStarStorage getStorage(VectorGoal goal, VectorNode start)
    {
        AStarStorage storage = storageSupplier.get();
        storage.open(start);

        start.cost = goal.getInitialCost(start);

        return storage;
    }

    public interface IsDone
    {
        void onSuccess();
    }

    public interface StorageCallback
    {
        void onSuccess(AStarStorage starStorage);
    }

    public void setStorageSupplier(Supplier<AStarStorage> storageSupplier)
    {
        this.storageSupplier = storageSupplier;
    }

    public static AStarMachine createWithDefaultStorage()
    {
        return createWithStorage(AStarStorage.FACTORY);
    }

    public static AStarMachine createWithStorage(Supplier<AStarStorage> storageSupplier)
    {
        return new AStarMachine(storageSupplier);
    }

    public class AStarState
    {
        private final VectorGoal goal;
        private final VectorNode start;
        private final AStarStorage storage;

        private AStarState(VectorGoal goal, VectorNode start, AStarStorage storage)
        {
            this.goal = goal;
            this.start = start;
            this.storage = storage;
        }

        public VectorNode getBestNode()
        {
            return storage.getBestNode();
        }
    }

    public Path runFully(VectorGoal goal, VectorNode start, int maxIterations)
    {
        return run(getStorage(goal, start), goal, start, maxIterations);
    }

    public interface PathCallback
    {
        void onSuccess(Path path);
    }

    private Path run(AStarStorage storage, VectorGoal goal, VectorNode start, int maxIterations)
    {
        Preconditions.checkNotNull(storage);
        Preconditions.checkNotNull(goal);
        Preconditions.checkNotNull(start);

        VectorNode node;
        int iterations = 0;

        while (true)
        {
            node = storage.removeBestNode();
            if (node == null)
            {

                return null;
            }
            if (goal.isFinished(node))
            {
                return node.buildPath();
            }
            storage.close(node);

            final VectorNode finalNode = node;

            for (VectorNode neighbour : finalNode.getNeighbours())
            {
                cost(goal, finalNode, neighbour);
                if (!storage.shouldExamine(neighbour))
                {
                    continue;
                }

                storage.open(neighbour);
                neighbour.parent = finalNode;
            }
            if (maxIterations >= 0 && iterations++ >= maxIterations)
            {
                return null;
            }
        }
    }
}
