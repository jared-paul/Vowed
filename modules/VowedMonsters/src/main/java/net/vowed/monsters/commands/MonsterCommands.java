package net.vowed.monsters.commands;

import net.minecraft.server.v1_12_R1.World;
import net.vowed.api.items.Tier;
import net.vowed.api.mobs.monsters.MonsterType;
import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import net.vowed.core.util.Preconditions;
import net.vowed.core.util.strings.Strings;
import net.vowed.monsters.types.AbstractMonster;
import net.vowed.monsters.types.MonsterCreeper;
import net.vowed.monsters.types.MonsterSkeleton;
import net.vowed.monsters.types.MonsterZombie;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by JPaul on 2017-02-13.
 */
public class MonsterCommands implements CommandClass
{
    @Command(
            aliases = {"vowed monster"},
            usage = "/vowed monster spawn <type> <tier>",
            description = "spawn a monster",
            initializer = "spawn",
            minArgs = 2,
            maxArgs = 2
    )
    public void spawnMonster(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;
        MonsterType monsterType = MonsterType.getMonsterAlias(args.getString(0));
        Tier tier = Tier.getTierAlias(args.getString(1));

        if (Preconditions.checkNotNull(monsterType, sender, Strings.handleError("That monster type does not exist, sorry", "monster type", "not exist")))
            return;
        if (Preconditions.checkNotNull(tier, sender, Strings.handleError("That tier alias does not exist, sorry", "tier", "not exist")))
            return;

        World nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
        Location location = player.getTargetBlock((Set<Material>) null, 10).getLocation().clone().add(0, 1, 0);
        AbstractMonster monster = null;

        assert monsterType != null;
        switch (monsterType)
        {
            case ZOMBIE:
                monster = new MonsterZombie(nmsWorld, tier);
                break;
            case SKELETON:
                monster = new MonsterSkeleton(nmsWorld, tier);
                break;
            case CREEPER:
                monster = new MonsterCreeper(nmsWorld, tier);
        }

        monster.spawn(location);
    }
}
