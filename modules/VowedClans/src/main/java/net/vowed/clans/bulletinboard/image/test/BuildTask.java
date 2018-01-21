package net.vowed.clans.bulletinboard.image.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import net.vowed.api.plugin.Vowed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BuildTask extends BukkitRunnable implements Listener
{
    private Map<UUID, Queue<Short>> status = new HashMap<>();
    private final Test plugin;
    private final int mapsPerRun;

    public BuildTask(Test plugin, int mapsPerSend)
    {
        this.plugin = plugin;
        this.mapsPerRun = mapsPerSend;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run()
    {
        if (plugin.getMaps().isEmpty())
            return;

        for (Player p : Vowed.getPlugin().getServer().getOnlinePlayers())
        {
            Queue<Short> state = getStatus(p);

            for (int i = 0; i < mapsPerRun && !state.isEmpty(); i++)
            {
                p.sendMap(Vowed.getPlugin().getServer().getMap(state.poll()));
            }
        }
    }

    private Queue<Short> getStatus(Player player)
    {
        if (!status.containsKey(player.getUniqueId()))
            status.put(player.getUniqueId(), new LinkedList<>(plugin.getMaps()));

        return status.get(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent joinEvent)
    {
        status.put(joinEvent.getPlayer().getUniqueId(), new LinkedList<>(plugin.getMaps()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent quitEvent)
    {
        status.remove(quitEvent.getPlayer().getUniqueId());
    }

    public void addToQueue(short mapId)
    {
        for (Queue<Short> queue : status.values())
        {
            queue.add(mapId);
        }
    }
}
