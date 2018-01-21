package net.vowed.items.weapon.listeners;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.vowed.api.items.Tier;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.api.mobs.monsters.IAbstractMonster;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.items.util.ItemUtil;
import net.vowed.core.items.util.weapon.WeaponStats;
import net.vowed.core.util.math.Integers;
import net.vowed.core.util.strings.Strings;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 2/7/2016.
 */
public class WeaponListener implements Listener
{
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent damageEvent)
    {
        Entity damager = damageEvent.getDamager();
        Entity entity = damageEvent.getEntity();

        if (damager instanceof LivingEntity && entity instanceof LivingEntity && !damageEvent.isCancelled())
        {
            LivingEntity attacker = (LivingEntity) damager;
            LivingEntity target = (LivingEntity) entity;
            net.minecraft.server.v1_12_R1.Entity nmsTarget = ((CraftEntity) entity).getHandle();
            ItemStack weapon = attacker.getEquipment().getItemInMainHand();
            WeaponStats weaponStats = ItemUtil.WeaponUtil.getWeaponStats(weapon);

            int damage = ThreadLocalRandom.current().nextInt(weaponStats.getDamageLOW() + 1, weaponStats.getDamageHIGH() + 2);

            if (nmsTarget instanceof IAbstractMonster)
            {
                damageEvent.setDamage(0);

                IAbstractMonster monster = (IAbstractMonster) nmsTarget;

                monster.setCurrentHP(monster.getCurrentHP() - damage);

                if (monster.getCurrentHP() <= 0)
                {
                    ((EntityLiving) monster.getEntity()).setHealth(0);
                    return;
                }

                target.setCustomName(ChatColor.RESET + barArray()[Integers.roundUpPositiveWithMax((((double) monster.getCurrentHP() / (double) monster.getMaxHP()) * 20.0), 20)]);
            }
        }

        //TODO add player interaction
    }

    @EventHandler
    public void onWeaponDurabilityDamage(PlayerItemDamageEvent itemDamageEvent)
    {
        itemDamageEvent.setCancelled(true);

        int damage = 0;

        ItemStack weapon = itemDamageEvent.getItem();

        if (WeaponType.getTypeFromMat(itemDamageEvent.getItem().getType()) != null)
        {
            switch (Tier.getTierFromWeapon(itemDamageEvent.getItem().getType()))
            {
                case TIER1:
                    damage = 2;
                    break;
                case TIER2:
                    damage = 4;
                    break;
                case TIER3:
                    damage = 7;
                    break;
                case TIER4:
                    damage = 11;
                    break;
                case TIER5:
                    damage = 15;
                    break;
                default:
                    Vowed.LOG.warning(Strings.handleError(itemDamageEvent.getPlayer().getName() + "'s item in hand (weapon) can't have its durability lowered", "generalGUI in hand", "can't", "durability lowered"));
            }

            ItemUtil.WeaponUtil.subtractDurability(weapon, damage);

            String durability = ChatColor.BOLD + "Durability: " + (ChatColor.RESET + barArray()[Integers.roundUpPositiveWithMax((((double) ItemUtil.WeaponUtil.getDurability(weapon) / (double) ItemUtil.WeaponUtil.getMaxDurability(weapon)) * 20.0), 20)]) +
                    " " +
                    ItemUtil.WeaponUtil.getDurability(weapon) + "/" + ItemUtil.WeaponUtil.getMaxDurability(weapon);

            sendActionBar(itemDamageEvent.getPlayer(), durability);
        }
    }

    public static void sendActionBar(Player player, String message)
    {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        p.getHandle().playerConnection.sendPacket(ppoc);
    }

    public String[] barArray()
    {
        String[] barArray = new String[21];

        ChatColor red = ChatColor.RED;
        ChatColor yellow = ChatColor.YELLOW;
        ChatColor green = ChatColor.GREEN;
        ChatColor grey = ChatColor.GRAY;

        //cross platform compatibility
        String halfBlock = StringEscapeUtils.unescapeHtml("&#9612;");
        String fullBlock = StringEscapeUtils.unescapeHtml("&#9608;");

        barArray[0] = red + halfBlock + "                  ";
        barArray[1] = red + halfBlock + "                   ";
        barArray[2] = red + fullBlock + "                  ";
        barArray[3] = red + fullBlock + halfBlock + "                 ";
        barArray[4] = red + fullBlock + fullBlock + "                ";
        barArray[5] = red + fullBlock + fullBlock + halfBlock + "               ";
        barArray[6] = red + fullBlock + fullBlock + fullBlock + "              ";
        barArray[7] = yellow + fullBlock + fullBlock + fullBlock + halfBlock + "             ";
        barArray[8] = yellow + fullBlock + fullBlock + fullBlock + fullBlock + "            ";
        barArray[9] = yellow + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "           ";
        barArray[10] = yellow + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "          ";
        barArray[11] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "         ";
        barArray[12] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "        ";
        barArray[13] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "       ";
        barArray[14] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "      ";
        barArray[15] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "     ";
        barArray[16] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "    ";
        barArray[17] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "   ";
        barArray[18] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "  ";
        barArray[19] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + " ";
        barArray[20] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock;

        return barArray;
    }
}
