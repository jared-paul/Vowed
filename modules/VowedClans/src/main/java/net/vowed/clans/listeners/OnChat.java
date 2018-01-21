package net.vowed.clans.listeners;

import net.vowed.api.clans.IClan;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.VowedColours;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by JPaul on 8/16/2016.
 */
public class OnChat implements Listener
{
    @EventHandler
    public void onChat(AsyncPlayerChatEvent chatEvent)
    {
        Player player = chatEvent.getPlayer();
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IClan clan = vowedPlayer.getClan();

        if (clan != null && clan.getTag() != null)
        {
            chatEvent.setMessage(VowedColours.CLAN_TAG + "[" + clan.getTag() + "]" + ChatColor.RESET + ": " + chatEvent.getMessage());
        }
    }
}
