package net.vowed.clans.requests;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.members.IClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 8/16/2016.
 */
public abstract class SingleRequest extends AbstractRequest
{
    private IClanPlayer requested;
    private boolean accepted = false;
    private boolean voted = false;

    protected SingleRequest(IClan requester, IClanPlayer requested)
    {
        super(requester);
        this.requested = requested;
    }

    public IClanPlayer getRequested()
    {
        return requested;
    }

    @Override
    public boolean isRequested(IClanPlayer clanPlayer)
    {
        return requested.getUUID().equals(clanPlayer.getUUID());
    }

    @Override
    public void accept(IClanPlayer voter)
    {
        accepted = true;
        voted = true;
    }

    @Override
    public void deny(IClanPlayer voter)
    {
        accepted = false;
        voted = true;
    }

    @Override
    public boolean hasVoted(IClanPlayer clanPlayer)
    {
        return voted;
    }

    @Override
    public boolean hasEveryoneVoted()
    {
        return voted;
    }

    @Override
    public boolean checkRequest()
    {
        return accepted;
    }

    @Override
    public void announceMessage(String message)
    {
        Player requestedPlayer = Bukkit.getPlayer(requested.getUUID());

        if (requestedPlayer != null)
        {
            requestedPlayer.sendMessage(message);
        }
        for (IClanPlayer clanPlayer : requester.getMembers())
        {
            Player player = Bukkit.getPlayer(clanPlayer.getUUID());

            if (player != null)
                player.sendMessage(message);
        }
    }

    @Override
    public void announceToHead(String message)
    {
        for (IClanPlayer clanPlayer : requester.getMembers())
        {
            if (clanPlayer.getRank() == Rank.OFFICER || clanPlayer.getRank() == Rank.LEADER)
            {
                Player player = Bukkit.getPlayer(clanPlayer.getUUID());

                if (player != null)
                    player.sendMessage(message);
            }
        }
    }
}
