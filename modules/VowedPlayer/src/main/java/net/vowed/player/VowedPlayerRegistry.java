package net.vowed.player;

import net.vowed.api.player.AbstractVowedPlayerRegistry;
import net.vowed.api.player.IVowedPlayer;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 8/15/2016.
 */
public class VowedPlayerRegistry extends AbstractVowedPlayerRegistry
{
    @Override
    public IVowedPlayer createVowedPlayer(Player player)
    {
        IVowedPlayer vowedPlayer = getVowedPlayer(player);

        if (vowedPlayer == null)
        {
            vowedPlayer = new VowedPlayer(player.getUniqueId());
        }

        return vowedPlayer;
    }
}
