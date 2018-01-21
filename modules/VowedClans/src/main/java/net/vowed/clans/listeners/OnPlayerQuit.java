package net.vowed.clans.listeners;

import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.plugin.Vowed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by JPaul on 2017-05-15.
 */
public class OnPlayerQuit implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent quitEvent)
    {
        Player player = quitEvent.getPlayer();

        IClanPlayer clanPlayer = Vowed.getClanPlayerRegistry().getClanPlayer(player.getUniqueId());

    }
}
