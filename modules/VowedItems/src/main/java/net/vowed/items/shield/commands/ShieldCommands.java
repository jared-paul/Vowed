package net.vowed.items.shield.commands;

import com.google.common.collect.Lists;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import net.vowed.core.util.Preconditions;
import net.vowed.core.util.strings.Strings;
import net.vowed.items.menu.rarity.RarityMenu;
import net.vowed.items.menu.shield.ShieldMenuController;
import net.vowed.items.menu.tier.TierMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 2017-02-03.
 */
public class ShieldCommands implements CommandClass
{
    @Command(
            aliases = {"vowed shield"},
            usage = "/vowed shield create",
            description = "create a shield",
            initializer = "create",
            minArgs = 0,
            maxArgs = 0
    )
    public void createShield(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        ShieldMenuController shieldMenuController = new ShieldMenuController(Lists.newArrayList(new TierMenu(), new RarityMenu()));
        shieldMenuController.showFirstMenu(player);
    }

    @Command(
            aliases = {"vowed shield"},
            usage = "/vowed shield give <player> <tier> <rarity>",
            description = "give someone a shield",
            initializer = "give",
            minArgs = 3,
            maxArgs = 3
    )
    public void giveShield(CommandContext args, CommandSender sender)
    {
        Player target = Bukkit.getPlayer(args.getString(0));

        if (Preconditions.checkNotNull(target, sender, Strings.handleError("The target player is not online at the moment", "target player", "not online")))
            return;

        Tier tier = Tier.getTierAlias(args.getString(1));
        Rarity rarity = Rarity.getRarityAlias(args.getString(2));

        if (Preconditions.checkNotNull(tier, sender, Strings.handleError("That tier alias does not exist, sorry", "tier", "not exist")))
            return;
        if (Preconditions.checkNotNull(rarity, sender, Strings.handleError("That rarity alias does not exist, sorry", "not exist")))
            return;

        ItemStack shield = Vowed.getItemFactory().createShield(rarity, tier);
        target.getInventory().addItem(shield);
    }
}
