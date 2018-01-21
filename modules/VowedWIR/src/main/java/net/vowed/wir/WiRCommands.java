package net.vowed.wir;

import com.sk89q.worldedit.data.DataException;
import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * Created by JPaul on 8/24/2016.
 */
public class WiRCommands implements CommandClass
{
    @Command(
            aliases = {"vowed rogue"},
            usage = "/vowed rogue create",
            description = "Create a WiR map",
            initializer = "create",
            minArgs = 0,
            maxArgs = -1
    )
    public void createMap(CommandContext context, CommandSender sender)
    {
        Player player = (Player) sender;

        try
        {
            Map map = new Map(150, 150, player.getLocation());
//15
            map.loadInWorld();

            PlayerManager.setMap(player, map);
        }
        catch (IOException | DataException e)
        {
            e.printStackTrace();
        }
    }
}
