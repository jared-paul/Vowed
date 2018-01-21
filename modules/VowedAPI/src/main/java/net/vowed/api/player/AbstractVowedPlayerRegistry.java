package net.vowed.api.player;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JPaul on 8/15/2016.
 */
public abstract class AbstractVowedPlayerRegistry
{
    protected final Map<UUID, IVowedPlayer> onlinePlayers = new ConcurrentHashMap<>();

    public IVowedPlayer getVowedPlayer(Player player)
    {
        return getVowedPlayer(player.getUniqueId());
    }

    public IVowedPlayer getVowedPlayer(UUID playerUUID)
    {
        return onlinePlayers.get(playerUUID);
    }

    protected abstract IVowedPlayer createVowedPlayer(Player player);

    public IVowedPlayer registerVowedPlayer(Player player)
    {
        IVowedPlayer vowedPlayer = createVowedPlayer(player);
        onlinePlayers.put(player.getUniqueId(), vowedPlayer);
        return vowedPlayer;
    }

    public void unregisterVowedPlayer(Player player)
    {
        IVowedPlayer vowedPlayer = getVowedPlayer(player);

        if (vowedPlayer != null)
        {
            onlinePlayers.remove(player.getUniqueId());
        }
    }
}
