package net.vowed.clans.requests;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.clans.StringUtil;
import net.vowed.core.VowedColours;
import net.vowed.core.util.fetchers.NameFetcher;
import net.vowed.core.util.strings.FancyMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 8/16/2016.
 */
public class InviteRequest extends SingleRequest
{
    public InviteRequest(IClan requester, IClanPlayer requested)
    {
        super(requester, requested);
    }

    @Override
    public String getName()
    {
        return "invite";
    }

    @Override
    public void onRequesting()
    {
        Player player = Bukkit.getPlayer(getRequested().getUUID());

        if (player != null)
        {
            new FancyMessageBuilder(VowedColours.COMMAND_RUN + "You have been to invited to " + requester.getName() + " [click here to accept]").runCommand("/vowed clan accept " + requester.getName()).sendTo(player);
        }
    }

    @Override
    public boolean onAccepted()
    {
        IClan clan = getRequester();

        if (clan == null || getRequester() == null)
            return false;

        IClanPlayer requested = getRequested();

        clan.addMember(requested);
        requested.setRank(Rank.MEMBER);

        return true;
    }

    @Override
    public void onDenied()
    {
        NameFetcher.getNameOfAsync(getRequested().getUUID())
                .syncLast(name -> announceToHead(StringUtil.handleError("Your invitation for " + name + " to join the clan has been denied")))
                .execute();
    }
}
