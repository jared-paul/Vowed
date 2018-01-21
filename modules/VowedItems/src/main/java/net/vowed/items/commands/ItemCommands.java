package net.vowed.items.commands;

import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import net.vowed.items.menu.generalGUI.ParentItemMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 2017-02-06.
 */
public class ItemCommands implements CommandClass
{
    @Command(
            aliases = {"vowed item"},
            usage = "/vowed item create",
            description = "create an item",
            initializer = "create",
            minArgs = 0,
            maxArgs = 0
    )
    public void createArmour(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;

        ParentItemMenu parentItemMenu = new ParentItemMenu();
        parentItemMenu.showToPlayer(player);
    }
}
