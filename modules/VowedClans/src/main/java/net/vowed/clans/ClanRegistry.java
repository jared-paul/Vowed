package net.vowed.clans;

import com.google.common.collect.Maps;
import net.vowed.api.clans.IClan;
import net.vowed.api.clans.IClanRegistry;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by JPaul on 8/13/2016.
 */
public class ClanRegistry implements IClanRegistry
{
    private Map<String, IClan> clanList = Maps.newHashMap();

    public ClanRegistry()
    {
        loadClans();
    }

    @Override
    public IClan getClan(String name)
    {
        return clanList.get(name);
    }

    @Override
    public IClan getClan(UUID playerUUID)
    {
        for (IClan clan : clanList.values())
        {
            for (IClanPlayer clanPlayer : clan.getMembers())
            {
                if (clanPlayer.getUUID().equals(playerUUID))
                {
                    return clan;
                }
            }
        }

        return null;
    }

    public IClan createClan(String name)
    {
        IClan clan = ClanFactory.createClan(name);
        clanList.put(name, clan);

        return clan;
    }

    @Override
    public IClan createClan(String name, IClanPlayer leader)
    {
        IClan clan = ClanFactory.createClan(name, leader);
        clanList.put(name, clan);
        clan.setLeader(leader);
        leader.setClan(clan);

        return clan;
    }

    @Override
    public IClan createClan(String name, IClanPlayer leader, Set<IClanPlayer> members)
    {
        IClan clan = ClanFactory.createClan(name, leader, members);
        clanList.put(name, clan);
        clan.setLeader(leader);
        leader.setClan(clan);

        return clan;
    }

    @Override
    public void saveClans() throws IOException
    {
        for (IClan clan : clanList.values())
        {
            clan.save();
        }
    }

    @Override
    public void loadClans()
    {
        Vowed.getSQLStorage().executeAsyncQuery("SELECT clan_uuid FROM clans", new SQLStorage.Callback<ResultSet>()
        {
            @Override
            public void onSuccess(ResultSet resultSet) throws SQLException
            {
                while (resultSet.next())
                {
                    UUID uuid = UUID.fromString(resultSet.getString("clan_uuid"));

                    loadClanFromStorage(uuid);
                }
            }

            @Override
            public void onFailure(Throwable cause)
            {
                cause.printStackTrace();
            }
        });
    }

    @Override
    public void loadClanFromStorage(UUID uuid)
    {
        Vowed.getSQLStorage().executeAsyncQuery("SELECT * FROM clans WHERE clan_uuid = '" + uuid.toString() + "'", new SQLStorage.Callback<ResultSet>()
        {
            @Override
            public void onSuccess(ResultSet resultSet) throws SQLException
            {
                while (resultSet.next())
                {
                    String name = resultSet.getString("name");
                    String tag = resultSet.getString("tag");

                    IClan clan = createClan(name);
                    clan.setUUID(uuid);
                    clan.setTag(tag);

                    Vowed.getSQLStorage().executeAsyncQuery("SELECT * FROM members WHERE clan_uuid = '" + uuid.toString() + "'", new SQLStorage.Callback<ResultSet>()
                    {
                        @Override
                        public void onSuccess(ResultSet resultSet) throws SQLException
                        {
                            while (resultSet.next())
                            {
                                UUID memberUUID = UUID.fromString(resultSet.getString("member_uuid"));

                                Vowed.getClanPlayerRegistry().loadClanPlayer(memberUUID)
                                        .syncLast(clan::addMember)
                                        .execute();
                            }
                        }

                        @Override
                        public void onFailure(Throwable cause)
                        {
                            cause.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable cause)
            {
                cause.printStackTrace();
            }
        });
    }
}
