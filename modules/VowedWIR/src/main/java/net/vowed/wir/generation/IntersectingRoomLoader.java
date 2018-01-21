package net.vowed.wir.generation;

import net.vowed.api.plugin.Vowed;
import net.vowed.wir.RoomComponent;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Created by JPaul on 9/8/2016.
 */
public class IntersectingRoomLoader implements Loader
{
    HallwayLoader hallwayLoader;
    List<RoomComponent> rooms;

    boolean isProcessing = false;
    boolean isDone = false;

    public IntersectingRoomLoader(HallwayLoader hallwayLoader)
    {
        this.hallwayLoader = hallwayLoader;
        this.rooms = hallwayLoader.rooms;
    }

    @Override
    public void load()
    {
        isProcessing = true;

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Vowed.LOG.debug("TEST");

                for (RoomComponent room : rooms)
                {
                    if (!room.hasBeenModified())
                    {
                        room.clear();
                    }

                    room.updateVirtualBlocks();
                }

                for (Location location : hallwayLoader.fullHallway)
                {
                }

                isDone = true;
                isProcessing = false;
            }
        }.runTaskLater(Vowed.getPlugin(), 200);

    }

    private final Set<BlockFace> DOOR_DIRECTIONS = EnumSet.of(BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.SELF);

    @Override
    public boolean isProcessing()
    {
        return isProcessing;
    }

    @Override
    public boolean isDone()
    {
        return isDone;
    }
}
