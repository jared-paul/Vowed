package net.vowed.wir.generation;

import com.google.common.collect.Lists;
import net.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * Created by JPaul on 10/1/2016.
 */
public class HallwayConsumer implements Runnable
{
    BlockingQueue<List<Location>> queue;
    HallwayProducer producer;

    public HallwayConsumer(BlockingQueue<List<Location>> queue, HallwayProducer producer)
    {
        this.queue = queue;
        this.producer = producer;
    }

    @Override
    public void run()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (producer.isDone)
                    {
                        cancel();
                    }

                    List<Location> locations = queue.take();

                    List<Location> locationCopy = Lists.newArrayList(locations);

                    for (Location location : locationCopy)
                    {
                        Bukkit.getScheduler().runTask(Vowed.getPlugin(), () -> location.getBlock().setType(Material.COBBLESTONE));
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(Vowed.getPlugin(), 20, 20);
    }
}
