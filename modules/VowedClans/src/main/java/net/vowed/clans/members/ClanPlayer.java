package net.vowed.clans.members;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Created by JPaul on 8/12/2016.
 */
public class ClanPlayer implements IClanPlayer
{
    private transient IClan clan;
    private UUID playerUUID;
    private transient Rank rank;

    protected ClanPlayer(UUID playerUUID, Rank rank)
    {
        this.playerUUID = playerUUID;
        this.rank = rank;
    }

    @Override
    public IClan getClan()
    {
        return clan;
    }

    @Override
    public void setClan(IClan clan)
    {
        this.clan = clan;
    }

    @Override
    public UUID getUUID()
    {
        return playerUUID;
    }

    @Override
    public Rank getRank()
    {
        return rank;
    }

    @Override
    public void setRank(Rank rank)
    {
        this.rank = rank;
    }

    @Override
    public boolean isLeader()
    {
        return rank == Rank.LEADER;
    }

    @Override
    public boolean isOfficer()
    {
        return rank == Rank.OFFICER;
    }

    @Override
    public boolean isTrusted()
    {
        return rank == Rank.TRUSTED;
    }

    @Override
    public boolean isMember()
    {
        return rank == Rank.MEMBER;
    }

    @Override
    public IVowedPlayer getVowedPlayer()
    {
        return Vowed.getPlayerRegistry().getVowedPlayer(Bukkit.getPlayer(playerUUID));
    }
}
