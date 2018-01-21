package net.vowed.player.listeners;

import net.vowed.api.plugin.Vowed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by JPaul on 8/15/2016.
 */
public class OnPlayerJoin implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();
        Vowed.getPlayerRegistry().registerVowedPlayer(player);

        player.setFlySpeed(0.5F);
    }
}
