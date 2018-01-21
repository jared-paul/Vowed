package net.vowed.wir.test;

import net.vowed.api.plugin.Vowed;
import net.vowed.wir.Map;
import net.vowed.wir.generation.*;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by JPaul on 9/4/2016.
 */
public class DungeonSpawner
{
    public DungeonSpawner(Map map)
    {

        RoomLoaderAsync loaderAsync = new RoomLoaderAsync(map);
        RoomLoaderSync loaderSync = new RoomLoaderSync(loaderAsync);
        HallwayLoader loaderHallway = new HallwayLoader(loaderSync);
        IntersectingRoomLoader intersectingRoomLoader = new IntersectingRoomLoader(loaderHallway);
        DecorationLoader decorationLoader = new DecorationLoader(loaderHallway);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!loaderAsync.isProcessing() && !loaderAsync.isDone())
                {
                    loaderAsync.load();
                }

                if (loaderAsync.isDone() && !loaderSync.isProcessing() && !loaderSync.isDone())
                {
                    loaderSync.load();
                }

                /*
                if (loaderSync.isDone() && !loaderIntersecting.isProcessing() && !loaderIntersecting.isDone())
                {
                    loaderIntersecting.load();
                }
                */



                if (loaderSync.isDone() && !loaderHallway.isProcessing() && !loaderHallway.isDone())
                {
                    loaderHallway.load();
                }

                if (loaderHallway.isDone() && !intersectingRoomLoader.isProcessing() && !intersectingRoomLoader.isDone())
                {
                    intersectingRoomLoader.load();
                }

                if (intersectingRoomLoader.isDone() && !decorationLoader.isProcessing() && !decorationLoader.isDone())
                {
                    decorationLoader.load();
                }
            }
        }.runTaskTimer(Vowed.getPlugin(), 20, 20);

    }
}
