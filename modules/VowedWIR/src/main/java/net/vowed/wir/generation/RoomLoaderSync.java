package net.vowed.wir.generation;

import com.google.common.collect.Lists;
import net.vowed.api.plugin.Vowed;
import net.vowed.wir.RoomComponent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Queue;

/**
 * Created by JPaul on 9/8/2016.
 */
public class RoomLoaderSync implements Loader
{
    RoomLoaderAsync loaderAsync;
    boolean isDone = false;
    boolean isProcessing = false;

    List<RoomComponent> rooms;

    Queue<RoomComponent> loadInQueue = Lists.newLinkedList();

    public RoomLoaderSync(RoomLoaderAsync loaderAsync)
    {
        this.loaderAsync = loaderAsync;
        this.rooms = Lists.newArrayList(loaderAsync.rooms);
    }

    @Override
    public void load()
    {
        isProcessing = true;

        for (RoomComponent roomComponent : loaderAsync.rooms)
        {
            loadInQueue.offer(roomComponent);
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (loadInQueue.isEmpty())
                {
                    cancel();

                    isProcessing = false;
                    isDone = true;
                }
                else
                {
                    RoomComponent room = loadInQueue.poll();
                    room.loadInWorld();
                }
            }
        }.runTaskTimer(Vowed.getPlugin(), 20, 20);
    }

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
