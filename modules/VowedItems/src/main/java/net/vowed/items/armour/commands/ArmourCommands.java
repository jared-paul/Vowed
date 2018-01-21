package net.vowed.items.armour.commands;

import com.google.common.collect.Lists;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.armour.ArmourType;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import net.vowed.core.util.Preconditions;
import net.vowed.core.util.strings.Strings;
import net.vowed.items.menu.armour.ArmourMenu;
import net.vowed.items.menu.armour.ArmourMenuController;
import net.vowed.items.menu.rarity.RarityMenu;
import net.vowed.items.menu.tier.TierMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 2017-02-03.
 */
public class ArmourCommands implements CommandClass
{
    @Command(
            aliases = {"vowed armour"},
            usage = "/vowed armour create",
            description = "create a piece of armour",
            initializer = "create",
            minArgs = 0,
            maxArgs = 0
    )
    public void createArmour(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        ArmourMenuController armourMenuController = new ArmourMenuController(Lists.newArrayList(new TierMenu(), new ArmourMenu(), new RarityMenu()));
        armourMenuController.showFirstMenu(player);
    }

    @Command(
            aliases = {"vowed armour"},
            usage = "/vowed armour give <player> <type> <tier> <rarity>",
            description = "give someone a piece of armour",
            initializer = "give",
            minArgs = 4,
            maxArgs = 4
    )
    public void giveArmour(CommandContext args, CommandSender sender)
    {
        Player target = Bukkit.getPlayer(args.getString(0));

        if (Preconditions.checkNotNull(target, sender, Strings.handleError("The target player is not online at the moment", "target player", "not online")))
            return;

        ArmourType armourType = ArmourType.getTypeAlias(args.getString(1));
        Tier tier = Tier.getTierAlias(args.getString(2));
        Rarity rarity = Rarity.getRarityAlias(args.getString(3));

        if (Preconditions.checkNotNull(armourType, sender, Strings.handleError("That armour type alias does not exist, sorry", "armour type", "not exist")))
            return;
        if (Preconditions.checkNotNull(tier, sender, Strings.handleError("That tier alias does not exist, sorry", "tier", "not exist")))
            return;
        if (Preconditions.checkNotNull(rarity, sender, Strings.handleError("That rarity alias does not exist, sorry", "not exist")))
            return;

        ItemStack armour = Vowed.getItemFactory().createArmour(rarity, tier, armourType);
        target.getInventory().addItem(armour);
    }
}
