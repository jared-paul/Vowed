package net.vowed.clans.commands;

import com.google.common.collect.Lists;
import me.jpaul.menuapi.items.MenuItemFactory;
import me.jpaul.menuapi.types.Menu;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.clans.IClan;
import net.vowed.api.clans.Rank;
import net.vowed.api.clans.billboard.MessagePriority;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.requests.Request;
import net.vowed.api.requests.VoteResult;
import net.vowed.api.requests.exceptions.HasVotedException;
import net.vowed.api.requests.exceptions.NotInClanException;
import net.vowed.api.requests.exceptions.RequestNonExistentException;
import net.vowed.api.requests.exceptions.TooManyRequestsException;
import net.vowed.clans.Format;
import net.vowed.clans.StringUtil;
import net.vowed.clans.requests.InviteRequest;
import net.vowed.clans.requests.MultiRequest;
import net.vowed.clans.requests.PromoteRequest;
import net.vowed.clans.requests.Vote;
import net.vowed.core.VowedColours;
import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import net.vowed.core.util.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Created by JPaul on 8/12/2016.
 */
public class ClanCommands implements CommandClass
{
    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan invite <player>",
            description = "invite your friends",
            initializer = "invite",
            minArgs = 1,
            maxArgs = 1
    )
    public void invite(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;
        Player invitee = Bukkit.getPlayer(args.getString(0));
        IVowedPlayer vowedSender = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(invitee);
        IClan clan = vowedSender.getClan();

        if (Preconditions.checkNotNull(clan, sender, StringUtil.handleError("You do not have a clan!", "do not")))
            return;
        if (Preconditions.checkNotNull(invitee, sender, StringUtil.handleError("Sorry, that player does not exist or is not online!", "not exist", "not online")))
            return;
        if (!(vowedSender.getClanPlayer().isLeader() || vowedSender.getClanPlayer().isOfficer()))
        {
            sender.sendMessage(StringUtil.handleError("You must be an officer or leader to invite people", "must", "officer", "leader"));
            return;
        }

        Vowed.getRequestManager().createRequest(new InviteRequest(clan, vowedPlayer.getClanPlayer()));
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan accept <clan>",
            description = "accept an active invitation",
            initializer = "accept",
            minArgs = 1,
            maxArgs = 1
    )
    public void accept(CommandContext args, CommandSender sender)
    {
        Player playerSender = (Player) sender;
        IClan clan = Vowed.getClanHandler().getClan(args.getString(0));

        IVowedPlayer vowedRequested = Vowed.getPlayerRegistry().getVowedPlayer(playerSender);

        try
        {
            Vowed.getRequestManager().vote(clan, vowedRequested.getClanPlayer(), "invite", vowedRequested.getClanPlayer(), VoteResult.ACCEPT);
            clan.getBulletinBoard().addMessage(playerSender.getName() + " has joined the clan", MessagePriority.MINOR);
        }
        catch (RequestNonExistentException e)
        {
            playerSender.sendMessage(StringUtil.handleError("You do not have an invite request pending!", "do not", "request"));
            return;
        }
        catch (HasVotedException e)
        {
            playerSender.sendMessage(StringUtil.handleError("You have already denied/accepted this request!", "already denied/accepted"));
            return;
        }
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan create <name>",
            description = "Create a clan to play with all your friends",
            initializer = "create",
            minArgs = 1,
            maxArgs = 1
    )
    public void createClan(CommandContext args, CommandSender sender)
    {
        IClan clan = Vowed.getClanHandler().createClan(args.getString(0), Vowed.getClanPlayerRegistry().createClanLeader(((Player) sender).getUniqueId()));

        sender.sendMessage(clan.getName());
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan tag <tag>",
            description = "Set your clans tag",
            initializer = "tag",
            minArgs = 1,
            maxArgs = 1
    )
    public void setTag(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;
        String tag = args.getString(0);
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IClan clan = vowedPlayer.getClan();

        if (Preconditions.checkNotNull(clan, sender, StringUtil.handleError("Sorry, you do not have a clan!", "do not")))
            return;

        if (tag.length() > 5)
        {
            sender.sendMessage(StringUtil.handleError("Your tag must be 5 characters or less", "5 characters"));
            return;
        }
        if (tag.equals(clan.getTag()))
        {
            sender.sendMessage(StringUtil.handleError("You already have that tag set!", "already", "set"));
            return;
        }

        clan.setTag(tag);
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan allies",
            description = "Get a list of your clans allies",
            initializer = "allies",
            minArgs = 0,
            maxArgs = 0
    )
    public void allies(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IClan clan = vowedPlayer.getClan();

        if (Preconditions.checkNotNull(clan, player, StringUtil.handleError("Sorry, you do not have a clan!", "do not")))
            return;

        List<String> allyNames = Lists.newArrayList();
        clan.getAllies().forEach(ally -> allyNames.add(ally.getName()));
        Format.formatAllies(allyNames).forEach(sender::sendMessage);
    }


    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan ally add <clan>",
            description = "Add an ally",
            initializer = "ally add",
            minArgs = 1,
            maxArgs = 1
    )
    public void addAlly(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IClan clan = vowedPlayer.getClan();
        IClan allyToADD = Vowed.getClanHandler().getClan(args.getString(0));

        if (Preconditions.checkNotNull(clan, player, StringUtil.handleError("Sorry, you do not have a clan!", "do not")))
            return;
        if (Preconditions.checkNotNull(allyToADD, player, StringUtil.handleError("Sorry, the clan you specified does not exist", "not exist")))
            return;

        if (allyToADD.getUUID().equals(clan.getUUID()))
        {
            sender.sendMessage(StringUtil.handleError("You can't add your own clan as an ally!", "your own clan"));
            return;
        }
        if (!vowedPlayer.getClanPlayer().isLeader())
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you are not the leader of your clan!", "not the leader"));
            return;
        }
        if (clan.getAllies().contains(allyToADD))
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you are already allied to that clan!", "already allied"));
            return;
        }

        clan.addAlly(allyToADD);
        sender.sendMessage(VowedColours.SUCCESS + "Successfully added " + allyToADD.getName() + " as an ally!");
        clan.getBulletinBoard().addMessage(clan.getName() + " is now an ally", MessagePriority.URGENT);
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan ally remove <clan>",
            description = "Add an ally",
            initializer = "ally remove",
            minArgs = 1,
            maxArgs = 1
    )
    public void removeAlly(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IClan clan = vowedPlayer.getClan();
        IClan allyToREMOVE = Vowed.getClanHandler().getClan(args.getString(0));

        if (Preconditions.checkNotNull(clan, player, StringUtil.handleError("Sorry, you do not have a clan!", "do not")))
            return;
        if (Preconditions.checkNotNull(allyToREMOVE, player, StringUtil.handleError("Sorry, the clan you specified does not exist", "not exist")))
            return;

        if (allyToREMOVE.getUUID().equals(clan.getUUID()))
        {
            sender.sendMessage(StringUtil.handleError("You can't add your own clan as an ally!", "your own clan"));
            return;
        }
        if (!vowedPlayer.getClanPlayer().isLeader())
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you are not the leader of your clan!", "not the leader"));
            return;
        }
        if (!clan.getAllies().contains(allyToREMOVE))
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you are not allied to that clan!", "not allied"));
            return;
        }

        clan.removeAlly(allyToREMOVE);
        sender.sendMessage(VowedColours.SUCCESS + "Successfully removed " + allyToREMOVE.getName() + " as an ally!");
        clan.getBulletinBoard().addMessage(clan.getName() + " is no longer an ally", MessagePriority.URGENT);
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan enemy add <clan>",
            description = "Add an enemy clan",
            initializer = "enemy add",
            minArgs = 1,
            maxArgs = 1
    )
    public void addEnemy(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IClan clan = vowedPlayer.getClan();
        IClan enemyToADD = Vowed.getClanHandler().getClan(args.getString(0));

        if (Preconditions.checkNotNull(clan, player, StringUtil.handleError("Sorry, you do not have a clan!", "do not")))
            return;
        if (Preconditions.checkNotNull(enemyToADD, player, StringUtil.handleError("Sorry, the clan you specified does not exist", "not exist")))
            return;

        if (enemyToADD.getUUID().equals(clan.getUUID()))
        {
            sender.sendMessage(StringUtil.handleError("You can't add your own clan as an ally!", "your own clan"));
            return;
        }
        if (!vowedPlayer.getClanPlayer().isLeader())
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you are not the leader of your clan!", "not the leader"));
            return;
        }
        if (clan.getEnemies().contains(enemyToADD))
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you are already enemies with that clan!", "already enemies"));
            return;
        }

        clan.addEnemy(enemyToADD);
        sender.sendMessage(VowedColours.SUCCESS + "Successfully added " + enemyToADD.getName() + " as an enemy!");
        clan.getBulletinBoard().addMessage(clan.getName() + " is now an enemy", MessagePriority.URGENT);
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan enemy remove <clan>",
            description = "Add an enemy clan",
            initializer = "enemy remove",
            minArgs = 1,
            maxArgs = 1
    )
    public void removeEnemy(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IClan clan = vowedPlayer.getClan();
        IClan enemyToREMOVE = Vowed.getClanHandler().getClan(args.getString(0));

        if (Preconditions.checkNotNull(clan, player, StringUtil.handleError("Sorry, you do not have a clan!", "do not")))
            return;
        if (Preconditions.checkNotNull(enemyToREMOVE, player, StringUtil.handleError("Sorry, the clan you specified does not exist", "not exist")))
            return;

        if (enemyToREMOVE.getUUID().equals(clan.getUUID()))
        {
            sender.sendMessage(StringUtil.handleError("You can't add your own clan as an ally!", "your own clan"));
            return;
        }
        if (!vowedPlayer.getClanPlayer().isLeader())
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you are not the leader of your clan!", "not the leader"));
            return;
        }
        if (!clan.getEnemies().contains(enemyToREMOVE))
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you are not enemies with that clan!", "not enemies"));
            return;
        }

        clan.removeEnemy(enemyToREMOVE);
        sender.sendMessage(VowedColours.SUCCESS + "Successfully removed " + enemyToREMOVE.getName() + " as an enemy!");
        clan.getBulletinBoard().addMessage(clan.getName() + " is no longer an enemy", MessagePriority.URGENT);
    }

    /**
     * REQUESTS
     */

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan progress <clanplayer> <requestname>",
            description = "Check a members vote process",
            initializer = "progress",
            minArgs = 0,
            maxArgs = 2
    )
    public void checkProgress(CommandContext args, CommandSender sender)
    {
        Player playerSender = (Player) sender;
        Player requested = null;
        String requestName = null;

        IVowedPlayer vowedSender = Vowed.getPlayerRegistry().getVowedPlayer(playerSender);

        if (args.argsLength() > 0)
        {
            requested = Bukkit.getPlayer(args.getString(0));
        }
        if (args.argsLength() > 1)
        {
            requestName = args.getString(1);
        }

        if (requested == null)
        {
            if (requestName == null)
            {
                try
                {
                    Request request = Vowed.getRequestManager().getOnlyRequest(vowedSender.getClan(), vowedSender.getClanPlayer());

                    if (request instanceof MultiRequest)
                    {
                        if (((MultiRequest) request).getVote().getTotalVotes() == 0)
                        {
                            playerSender.sendMessage(StringUtil.handleError("No one has voted on this request yet!", "No one", "voted"));
                            return;
                        }

                        sendBarGraphInventory((MultiRequest) request, playerSender);
                    }
                    else
                    {
                        playerSender.sendMessage(StringUtil.handleError("You do not have a voting request present!", "do not have"));
                        return;
                    }
                }
                catch (TooManyRequestsException tooManyRequestsException)
                {
                    playerSender.sendMessage(StringUtil.handleError("You have more than one request! Please specify which request you'd like to check", "more than one", "specify which request"));
                    return;
                }
                catch (NotInClanException e)
                {
                    playerSender.sendMessage(StringUtil.handleError("You are not in a clan!", "not in"));
                    return;
                }
                catch (RequestNonExistentException e)
                {
                    playerSender.sendMessage(StringUtil.handleError("You do not have a request present!", "do not have"));
                    return;
                }
            }
        }
        else
        {
            IVowedPlayer vowedRequested = Vowed.getPlayerRegistry().getVowedPlayer(requested);

            try
            {
                Request request = Vowed.getRequestManager().getOnlyRequest(vowedSender.getClan(), vowedRequested.getClanPlayer());

                if (request instanceof MultiRequest)
                {
                    if (((MultiRequest) request).getVote().getTotalVotes() == 0)
                    {
                        playerSender.sendMessage(StringUtil.handleError("No one has voted on this request yet!", "No one", "voted"));
                        return;
                    }

                    sendBarGraphInventory((MultiRequest) request, playerSender);
                }
                else
                {
                    playerSender.sendMessage(StringUtil.handleError(requested.getName() + " does not have a voting request present!", "does not have"));
                    return;
                }
            }
            catch (TooManyRequestsException e)
            {
                playerSender.sendMessage(StringUtil.handleError(requested.getName() + " has more than one request! Please specify which request you'd like to check", "more than one", "specify which request"));
                return;
            }
            catch (NotInClanException e)
            {
                playerSender.sendMessage(StringUtil.handleError("You are not in the same clan as " + requested.getName() + "!", "not in", "same", requested.getName()));
                return;
            }
            catch (RequestNonExistentException e)
            {
                playerSender.sendMessage(StringUtil.handleError(requested.getName() + " does not have a request present!", "does not have"));
                return;
            }
        }
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan vote <clanplayer> <requestname> <vote>",
            description = "vote on a members request",
            initializer = "vote",
            minArgs = 2,
            maxArgs = 3
    )
    public void vote(CommandContext args, CommandSender sender)
    {
        Player playerSender = (Player) sender;
        Player requested = Bukkit.getPlayer(args.getString(0));
        VoteResult voteResult;
        String requestName = null;

        IVowedPlayer vowedSender = Vowed.getPlayerRegistry().getVowedPlayer(playerSender);
        IVowedPlayer vowedRequested = Vowed.getPlayerRegistry().getVowedPlayer(requested);

        if (Preconditions.checkNotNull(requested, playerSender, StringUtil.handleError("The specified player does not exist or is not online!", "not exist", "not online")))
            return;

        if (args.argsLength() < 3)
        {
            voteResult = VoteResult.fromString(args.getString(1));
        }
        else
        {
            requestName = args.getString(1);
            voteResult = VoteResult.fromString(args.getString(2));
        }

        if (playerSender.getUniqueId().equals(requested.getUniqueId()))
        {
            playerSender.sendMessage(StringUtil.handleError("You can not vote for yourself!", "can not", "yourself"));
            return;
        }

        if (requestName == null)
        {
            try
            {
                Vowed.getRequestManager().vote(vowedSender.getClan(), vowedRequested.getClanPlayer(), vowedSender.getClanPlayer(), voteResult);
            }
            catch (TooManyRequestsException e)
            {
                playerSender.sendMessage(StringUtil.handleError(requested.getName() + " has more than one request! Please specify which request you'd like to check", "more than one", "specify which request"));
                return;
            }
            catch (NotInClanException e)
            {
                playerSender.sendMessage(StringUtil.handleError("You are not in the same clan as " + requested.getName() + "!", "not in", "same", requested.getName()));
                return;
            }
            catch (RequestNonExistentException e)
            {
                playerSender.sendMessage(StringUtil.handleError(requested.getName() + " does not have a request present!", "does not have"));
                return;
            }
            catch (HasVotedException e)
            {
                playerSender.sendMessage(StringUtil.handleError("You have already voted on " + requested.getName() + "'s request!", "already voted", requested.getName()));
                return;
            }
        }
        else
        {
            try
            {
                Vowed.getRequestManager().vote(vowedSender.getClan(), vowedRequested.getClanPlayer(), requestName, vowedSender.getClanPlayer(), voteResult);

            }
            catch (RequestNonExistentException e)
            {
                playerSender.sendMessage(StringUtil.handleError(requested.getName() + " does not have a request present!", "does not have"));
                return;
            }
            catch (HasVotedException e)
            {
                playerSender.sendMessage(StringUtil.handleError("You have already voted on " + requested.getName() + "'s request!", "already voted", requested.getName()));
                return;
            }
        }

        if (voteResult == VoteResult.ACCEPT)
        {
            playerSender.sendMessage(VowedColours.SUCCESS + "You accepted " + requested.getName() + "'s request!");
        }
        else if (voteResult == VoteResult.DENY)
        {
            playerSender.sendMessage(VowedColours.SUCCESS + "You denied " + requested.getName() + "'s request!");
        }
    }

    @Command(
            aliases = {"vowed clan"},
            usage = "/vowed clan promote <member> <rank>",
            description = "Promote a clan member to the specified rank",
            initializer = "promote",
            minArgs = 2,
            maxArgs = 2
    )
    public void promote(CommandContext args, CommandSender sender)
    {

        Player player = Bukkit.getPlayer(args.getString(0));
        Rank rank = Rank.fromString(args.getString(1));
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        IVowedPlayer vowedSender = Vowed.getPlayerRegistry().getVowedPlayer((Player) sender);

        if (Preconditions.checkNotNull(rank, sender, StringUtil.handleError("Sorry, that rank does not exist!", "not exist")))
            return;
        if (Preconditions.checkNotNull(player, sender, StringUtil.handleError("Sorry, that player does not exist or is not online!", "not exist", "not online")))
            return;

        if (vowedPlayer.getClan() == null || !vowedPlayer.getClan().equals(vowedPlayer.getClan()))
        {
            sender.sendMessage(StringUtil.handleError("You must request to promote someone that is in your clan!", "in your clan"));
            return;
        }
        if (vowedSender.equals(vowedPlayer))
        {
            sender.sendMessage(StringUtil.handleError("You can't promote yourself!", "yourself"));
            return;
        }
        if (vowedPlayer.getClanPlayer().getRank().priority <= rank.priority)
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you must give a rank higher than the one the player has!", "higher", "player has"));
            return;
        }
        if (!vowedSender.getClanPlayer().getRank().allowedPromotions().contains(rank))
        {
            sender.sendMessage(StringUtil.handleError("Sorry, you must have a higher rank to promote a member to " + StringUtil.capitalizeFirst(rank.name().toLowerCase()), "higher rank", StringUtil.capitalizeFirst(rank.name().toLowerCase())));
            return;
        }
        if (vowedSender.getClanPlayer().getRank() == Rank.LEADER && rank == Rank.LEADER)
        {
            vowedSender.getClanPlayer().setRank(Rank.OFFICER);
            sender.sendMessage(VowedColours.CONCERNED + "You have been demoted to officer because you promoted someone else to the leader position");
        }

        Vowed.getRequestManager().createRequest(new PromoteRequest(vowedSender.getClan(), vowedPlayer.getClanPlayer(), vowedSender.getClan().getMembers(), rank));
    }


    public void sendBarGraph(MultiRequest request, Player player)
    {

        Vote vote = request.getVote();

        Map<VoteResult, List<String>> barGraph = vote.toBarGraph();

        List<String> acceptBar = barGraph.get(VoteResult.ACCEPT);
        List<String> denyBar = barGraph.get(VoteResult.DENY);

        List<String> bothBars = Lists.newArrayList();

        for (int i = 0; i < vote.getTotalVotes(); i++)
        {
            StringBuilder combinedBlocks = new StringBuilder();

            combinedBlocks.append("     ");

            try
            {
                if (i == vote.getAccepts() / 2)
                {
                    combinedBlocks
                            .append(VowedColours.ACCEPTS)
                            .append(acceptBar.get(i))
                            .append(" ")
                            .append(vote.getAccepts())
                            .append("/")
                            .append(vote.getTotalVotes())
                            .append("     ");
                }
                else
                {
                    combinedBlocks
                            .append(VowedColours.ACCEPTS)
                            .append(acceptBar.get(i))
                            .append("          ");
                }
            }
            catch (Exception ignored)
            {
            }

            try
            {
                if (i == vote.getDenies() / 2)
                {
                    combinedBlocks
                            .append(VowedColours.DENIES)
                            .append(denyBar.get(i))
                            .append(" ")
                            .append(vote.getDenies())
                            .append("/")
                            .append(vote.getTotalVotes());
                }
                else
                {
                    combinedBlocks
                            .append(VowedColours.DENIES)
                            .append(denyBar.get(i));
                }
            }
            catch (Exception ignored)
            {
            }

            bothBars.add(combinedBlocks.toString());
        }

        for (String blockLine : bothBars)
        {
            player.sendMessage(blockLine);
        }
    }

    public void sendBarGraphInventory(MultiRequest request, Player player)
    {

        Vote vote = request.getVote();

        Map<VoteResult, List<ItemStack>> barGraph = vote.toBarGraphInventory();

        List<ItemStack> acceptBar = barGraph.get(VoteResult.ACCEPT);
        List<ItemStack> denyBar = barGraph.get(VoteResult.DENY);

        BarGraphMenu graphMenu = new BarGraphMenu();

        for (int i = 0; i < vote.getTotalVotes(); i++)
        {
            ItemStack acceptItem = new ItemBuilder(acceptBar.get(i))
                    .setWoolColour(DyeColor.GREEN)
                    .setName(VowedColours.ACCEPTS.toString() + vote.getAccepts() + "/" + vote.getTotalVotes())
                    .getItem();

            graphMenu.addItem(45 - ((i + 1) * 9 - 2), MenuItemFactory.createItem(acceptItem, inventoryClickEvent -> {
            }));


            ItemStack denyItem = new ItemBuilder(denyBar.get(i))
                    .setWoolColour(DyeColor.RED)
                    .setName(VowedColours.DENIES.toString() + vote.getDenies() + "/" + vote.getTotalVotes())
                    .getItem();

            graphMenu.addItem(45 - ((i + 1) * 9 - 5), MenuItemFactory.createItem(denyItem, inventoryClickEvent -> {
            }));
        }

        graphMenu.showToPlayer(player);
    }

    public class BarGraphMenu extends Menu
    {
        public BarGraphMenu()
        {
            super(45, "Votes");
            setCancelClick(true);
        }
    }
}
