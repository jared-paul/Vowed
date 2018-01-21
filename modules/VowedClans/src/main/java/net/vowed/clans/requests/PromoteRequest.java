package net.vowed.clans.requests;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.core.VowedColours;
import net.vowed.core.util.fetchers.NameFetcher;
import org.bukkit.Bukkit;

import java.util.Set;

/**
 * Created by JPaul on 8/16/2016.
 */
public class PromoteRequest extends MultiRequest
{
    private IClanPlayer target;
    private Rank promotion;

    public PromoteRequest(IClan requester, IClanPlayer target, Set<IClanPlayer> voters, Rank promotion)
    {
        super(requester, voters);
        this.target = target;
        this.promotion = promotion;
    }

    @Override
    public void onRequesting()
    {
        Bukkit.getPlayer(target.getUUID()).sendMessage("You have been requested to be promoted");
    }

    @Override
    public String getName()
    {
        return "promote";
    }

    @Override
    public boolean isRequested(IClanPlayer clanPlayer)
    {
        return target.getUUID().equals(clanPlayer.getUUID());
    }

    @Override
    public IClanPlayer getRequested()
    {
        return target;
    }

    @Override
    public boolean onAccepted()
    {
        NameFetcher.getNameOfAsync(target.getUUID())
                .syncLast(name -> announceMessage(VowedColours.ERROR + "Promotion for " + name + " has been accepted by the voters"))
                .execute();

        target.setRank(promotion);

        return true;
    }

    @Override
    public void onDenied()
    {
        NameFetcher.getNameOfAsync(target.getUUID())
                .syncLast(name -> announceMessage(VowedColours.ERROR + "Promotion for " + name + " has been denied by the voters"))
                .execute();
    }
}
