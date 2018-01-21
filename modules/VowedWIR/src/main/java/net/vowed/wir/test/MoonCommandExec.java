
package net.vowed.wir.test;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoonCommandExec implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            player.teleport(getMoon().getSpawnLocation());
        } else {
            sender.sendMessage("I don't know who you are!");
        }

        return true;
    }

    static World moon;

    public static World getMoon() {
        if (moon == null) {
            WorldCreator worldCreator = new WorldCreator("moon");
            worldCreator.environment(World.Environment.NORMAL);
            worldCreator.generator(new MoonChunkGenerator());

            moon = Bukkit.getServer().createWorld(worldCreator);
        }

        return moon;
    }
}
