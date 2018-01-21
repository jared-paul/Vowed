package net.vowed.clans.requests;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.members.IClanPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by JPaul on 8/16/2016.
 */
public abstract class MultiRequest extends AbstractRequest
{
    private final Set<IClanPlayer> voters;
    private int accepts = 0;
    private int denies = 0;

    private Vote vote;

    protected MultiRequest(IClan requester, Set<IClanPlayer> voters)
    {
        super(requester);
        this.voters = voters;
        this.vote = new Vote();
    }

    public Vote getVote()
    {
        return vote;
    }

    @Override
    public void accept(IClanPlayer voter)
    {
        vote.addAccept(voter);
    }

    @Override
    public void deny(IClanPlayer voter)
    {
        vote.addDeny(voter);
    }

    @Override
    public boolean hasVoted(IClanPlayer clanPlayer)
    {
        return vote.getPeopleVoted().contains(clanPlayer);
    }

    @Override
    public boolean hasEveryoneVoted()
    {
        return vote.getTotalVotes() >= voters.size() - 1;
    }

    @Override
    public boolean checkRequest()
    {
        return vote.getAccepts() * 2 >= voters.size() - 1;
    }

    @Override
    public void announceMessage(String message)
    {
        for (IClanPlayer clanPlayer : voters)
        {
            Player player = Bukkit.getPlayer(clanPlayer.getUUID());

            if (player != null)
                player.sendMessage(message);
        }
    }

    @Override
    public void announceToHead(String message)
    {
        for (IClanPlayer clanPlayer : voters)
        {
            if (clanPlayer.getRank() == Rank.LEADER || clanPlayer.getRank() == Rank.OFFICER)
            {
                Player player = Bukkit.getPlayer(clanPlayer.getUUID());

                if (player != null)
                    player.sendMessage(message);
            }
        }
    }
}
