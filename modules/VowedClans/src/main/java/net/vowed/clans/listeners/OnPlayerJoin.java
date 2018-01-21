package net.vowed.clans.listeners;

import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.plugin.Vowed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by JPaul on 8/13/2016.
 */
public class OnPlayerJoin implements Listener
{

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();
        Vowed.getClanPlayerRegistry().createClanPlayer(player.getUniqueId());

        IClanPlayer clanPlayer = Vowed.getClanPlayerRegistry().getClanPlayer(player.getUniqueId());

        if (clanPlayer.getClan() != null)
        {
            clanPlayer.getClan().getBulletinBoard().displayMessages(player);
        }
    }
}
