package net.vowed.wir.astar.pathfinding.examiners;

import net.vowed.wir.RoomComponent;
import net.vowed.wir.astar.pathfinding.VectorNode;
import net.vowed.wir.astar.pathfinding.sources.BlockSource;

/**
 * Created by JPaul on 6/20/2016.
 */
public interface BlockExaminer
{
    float getCost(BlockSource blockSource, VectorNode node);

    PassableState isPassable(BlockSource blockSource, VectorNode node, RoomComponent start, RoomComponent end);

    enum PassableState
    {
        IGNORE,
        PASSABLE,
        UNPASSABLE;

        public static PassableState fromBoolean(boolean flag)
        {
            return flag ? PASSABLE : UNPASSABLE;
        }
    }
}
