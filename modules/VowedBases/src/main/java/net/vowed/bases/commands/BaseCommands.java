package net.vowed.bases.commands;

import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.clans.IClan;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.plugin.Vowed;
import net.vowed.bases.Base;
import net.vowed.core.VowedColours;
import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import net.vowed.core.itempair.ItemPair;
import net.vowed.core.itempair.ItemSizeChecker;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 8/23/2016.
 */
public class BaseCommands implements CommandClass
{
    @Command(
            aliases = "vowed base",
            usage = "/vowed base create <type>",
            description = "Create a base for your clan",
            initializer = "create",
            minArgs = 1,
            maxArgs = 1
    )
    public void createBase(CommandContext args, CommandSender sender)
    {
        Player playerSender = (Player) sender;
        IVowedPlayer vowedSender = Vowed.getPlayerRegistry().getVowedPlayer(playerSender);
        IClan clan = vowedSender.getClan();

        ItemStack baseCreator = new ItemBuilder(Material.BLAZE_ROD)
                .setName(VowedColours.SUCCESS + "Base Creator")
                .setLore(VowedColours.LORE_DESC + "Create a base for your clan")
                .getItem();

        playerSender.getInventory().addItem(baseCreator);

        new ItemPair(baseCreator, (clickedBlock, action, player) ->
        {
            Vowed.LOG.debug("test");

            if (vowedSender.getUUID().equals(player.getUniqueId()))
            {
                Base base = new Base(clickedBlock, clan);
                clan.setBase(base);

                try
                {
                    base.create();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        new ItemSizeChecker(baseCreator, moveEvent -> {

        });
    }
}
