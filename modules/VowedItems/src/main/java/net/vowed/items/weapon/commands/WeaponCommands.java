package net.vowed.items.weapon.commands;

import com.google.common.collect.Lists;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import net.vowed.core.util.Preconditions;
import net.vowed.core.util.strings.Strings;
import net.vowed.items.menu.rarity.RarityMenu;
import net.vowed.items.menu.tier.TierMenu;
import net.vowed.items.menu.weapon.WeaponMenu;
import net.vowed.items.menu.weapon.WeaponMenuController;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 2017-02-03.
 */
public class WeaponCommands implements CommandClass
{
    @Command(
            aliases = {"vowed weapon"},
            usage = "/vowed weapon create",
            description = "create a weapon",
            initializer = "create",
            minArgs = 0,
            maxArgs = 0
    )
    public void createWeapon(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        WeaponMenuController weaponMenuController = new WeaponMenuController(Lists.newArrayList(new TierMenu(), new WeaponMenu(), new RarityMenu()));
        weaponMenuController.showFirstMenu(player);
    }

    @Command(
            aliases = {"vowed weapon"},
            usage = "/vowed weapon give <player> <type> <tier> <rarity>",
            description = "give someone a piece of weapon",
            initializer = "give",
            minArgs = 4,
            maxArgs = 4
    )
    public void giveWeapon(CommandContext args, CommandSender sender)
    {
        Player target = Bukkit.getPlayer(args.getString(0));

        if (Preconditions.checkNotNull(target, sender, Strings.handleError("The target player is not online at the moment", "target player", "not online")))
            return;

        WeaponType weaponType = WeaponType.getTypeAlias(args.getString(1));
        Tier tier = Tier.getTierAlias(args.getString(2));
        Rarity rarity = Rarity.getRarityAlias(args.getString(3));

        if (Preconditions.checkNotNull(weaponType, sender, Strings.handleError("That armour type alias does not exist, sorry", "armour type", "not exist")))
            return;
        if (Preconditions.checkNotNull(tier, sender, Strings.handleError("That tier alias does not exist, sorry", "tier", "not exist")))
            return;
        if (Preconditions.checkNotNull(rarity, sender, Strings.handleError("That rarity alias does not exist, sorry", "not exist")))
            return;

        ItemStack weapon = Vowed.getItemFactory().createWeapon(rarity, tier, weaponType);
        target.getInventory().addItem(weapon);
    }
}
