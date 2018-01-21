package net.vowed.player.listeners;

import net.vowed.api.plugin.Vowed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by JPaul on 8/15/2016.
 */
public class OnPlayerQuit implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent quitEvent)
    {
        Player player = quitEvent.getPlayer();
        Vowed.getPlayerRegistry().unregisterVowedPlayer(player);
    }
}
