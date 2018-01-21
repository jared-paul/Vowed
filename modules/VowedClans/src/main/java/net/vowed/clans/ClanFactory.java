package net.vowed.clans;

import net.vowed.api.clans.members.IClanPlayer;

import java.util.Set;

/**
 * Created by JPaul on 8/12/2016.
 */
public class ClanFactory
{
    public static Clan createClan(String name)
    {
        return Clan.createClan(name);
    }

    public static Clan createClan(String name, IClanPlayer leader)
    {
        return Clan.createClan(name, leader);
    }

    public static Clan createClan(String name, IClanPlayer leader, Set<IClanPlayer> members)
    {
        Clan clan = createClan(name, leader);
        clan.setMembers(members);

        return clan;
    }
}
