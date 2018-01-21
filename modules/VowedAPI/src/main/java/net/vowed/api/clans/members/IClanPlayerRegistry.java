package net.vowed.api.clans.members;

import net.vowed.api.TaskChain;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by JPaul on 8/15/2016.
 */
public interface IClanPlayerRegistry
{
    IClanPlayer createClanLeader(UUID uuid);

    IClanPlayer createClanPlayer(UUID uuid);

    IClanPlayer getClanPlayer(UUID uuid);

    void saveClanPlayer(IClanPlayer clanPlayer);

    TaskChain<IClanPlayer> loadClanPlayer(UUID playerUUID);
}
