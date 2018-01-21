package net.vowed.clans.members;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.vowed.api.TaskChain;
import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.clans.members.IClanPlayerRegistry;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.omg.CORBA.portable.ValueOutputStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by JPaul on 8/15/2016.
 */
public class ClanPlayerRegistry implements IClanPlayerRegistry
{
    private Map<UUID, IClanPlayer> clanPlayers = Maps.newConcurrentMap();

    @Override
    public IClanPlayer createClanLeader(UUID uuid)
    {
        IClanPlayer clanPlayer = createClanPlayer(uuid);
        clanPlayer.setRank(Rank.LEADER);

        return clanPlayer;
    }

    @Override
    public IClanPlayer createClanPlayer(UUID uuid)
    {
        IClanPlayer clanPlayerSearch = getClanPlayer(uuid);

        if (clanPlayerSearch == null)
        {
            IClanPlayer clanPlayer = new ClanPlayer(uuid, Rank.MEMBER);
            clanPlayers.put(uuid, clanPlayer);

            return clanPlayer;
        }

        return clanPlayerSearch;
    }

    @Override
    public IClanPlayer getClanPlayer(UUID uuid)
    {
        return clanPlayers.get(uuid);
    }

    @Override
    public void saveClanPlayer(IClanPlayer clanPlayer)
    {
        Vowed.getSQLStorage().updateAsyncQuery("members",
                new String[]{"member_uuid", "rank"},
                new String[]{clanPlayer.getUUID().toString(), clanPlayer.getRank().name()},
                new String[]{clanPlayer.getUUID().toString(), clanPlayer.getRank().name()},
                new SQLStorage.Callback<Integer>()
                {
                    @Override
                    public void onSuccess(Integer integer) throws SQLException
                    {

                    }

                    @Override
                    public void onFailure(Throwable cause)
                    {
                        cause.printStackTrace();
                    }
                });
    }

    @Override
    public TaskChain<IClanPlayer> loadClanPlayer(UUID playerUUID)
    {
        IClanPlayer clanPlayer = createClanPlayer(playerUUID);

        return TaskChain
                .newChain()
                .asyncFirst(() ->
                {
                    Vowed.getSQLStorage().executeQuery("SELECT * FROM members WHERE member_uuid = '" + playerUUID.toString() + "'", new SQLStorage.Callback<ResultSet>()
                    {
                        @Override
                        public void onSuccess(ResultSet resultSet) throws SQLException
                        {
                            if (resultSet.next())
                            {
                                UUID clanUUID = UUID.fromString(resultSet.getString("clan_uuid"));
                                Rank rank = Rank.fromString(resultSet.getString("rank"));

                                IClan clan = Vowed.getClanHandler().getClan(clanUUID);

                                clanPlayer.setRank(rank);
                                clanPlayer.setClan(clan);
                            }
                        }

                        @Override
                        public void onFailure(Throwable cause)
                        {
                            cause.printStackTrace();
                        }
                    });

                    return clanPlayer;
                });
    }
}
