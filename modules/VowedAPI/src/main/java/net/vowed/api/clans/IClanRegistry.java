package net.vowed.api.clans;

import net.vowed.api.clans.members.IClanPlayer;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JPaul on 8/12/2016.
 */
public interface IClanRegistry
{
    IClan getClan(String name);

    IClan getClan(UUID playerUUID);

    IClan createClan(String name, IClanPlayer leader);

    IClan createClan(String name, IClanPlayer leader, Set<IClanPlayer> members);

    void saveClans() throws IOException;

    void loadClans();

    void loadClanFromStorage(UUID uuid);
}
