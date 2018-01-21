package net.vowed.items.armour.listeners;

import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.player.PlayerStats;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.items.util.ItemUtil;
import net.vowed.core.items.util.armour.ArmourStats;
import net.vowed.core.util.math.Integers;
import net.vowed.items.armour.event.ArmourEquipEvent;
import net.vowed.items.armour.event.ArmourUnequipEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;


/**
 * Created by JPaul on 1/24/2016.
 */
public class ArmourListener implements Listener
{
    @EventHandler
    public void onEquip(ArmourEquipEvent equipEvent) throws NoSuchFieldException, IllegalAccessException
    {
        ItemStack newPiece = equipEvent.getItem();
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(equipEvent.getPlayer());
        Player player = vowedPlayer.getPlayer();

        if (newPiece != null && ItemUtil.ArmourUtil.isLeatherArmour(newPiece) && player != null)
        {
            ArmourStats armourStats = ItemUtil.ArmourUtil.getArmourStats(newPiece);

            applyStaticStats(player, armourStats);

            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);
        }
        else
        {
            equipEvent.setCancelled(true);
        }

        Vowed.LOG.warning(vowedPlayer.getStats().getMaxHealth());
    }

    @EventHandler
    public void onUnequip(ArmourUnequipEvent unequipEvent)
    {
        ItemStack oldPiece = unequipEvent.getItem();
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(unequipEvent.getPlayer());
        Player player = vowedPlayer.getPlayer();

        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1F, 1F);

        if (oldPiece != null && ItemUtil.ArmourUtil.isLeatherArmour(oldPiece))
        {
            removeStaticStats(player.getPlayer(), ItemUtil.ArmourUtil.getArmourStats(oldPiece));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent joinEvent)
    {
        Player player = joinEvent.getPlayer();

        Bukkit.getScheduler().runTaskLater(Vowed.getPlugin(), () ->
        {
            for (ItemStack armour : player.getInventory().getArmorContents())
            {
                ArmourStats armourStats = ItemUtil.ArmourUtil.getArmourStats(armour);
                applyStaticStats(player, armourStats);
            }
        }, 20);
    }

    private void applyStaticStats(Player player, ArmourStats armourStats)
    {
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        PlayerStats stats = vowedPlayer.getStats();
        float newSpeed = armourStats.getSpeed();
        int newHealth = armourStats.getHP();
        int newHealthRegen = armourStats.getHPRegen();
        int newEnergyRegen = armourStats.getEnergyRegen();
        int newArmourPercent = armourStats.getArmourPercent();
        int newBlock = armourStats.getBlock();
        int newDodge = armourStats.getDodge();

        if (newSpeed != 0)
        {
            double percentToADD = (0.2 * (newSpeed / 100));

            player.setWalkSpeed((float) Integers.round(player.getWalkSpeed() + percentToADD, 3));
        }
        if (armourStats.getHP() != 0)
        {
            stats.setMaxHealth(stats.getMaxHealth() + newHealth);
            vowedPlayer.updateHealth();
        }

        stats.setBlock(stats.getBlock() + newBlock);
        stats.setDodge(stats.getDodge() + newDodge);
        stats.setSpeed(stats.getSpeed() + newSpeed);
        stats.setArmourPercent(stats.getArmourPercent() + newArmourPercent);
        stats.setHealthRegen(stats.getHealthRegen() + newHealthRegen);
        stats.setEnergyRegen(stats.getEnergyRegen() + newEnergyRegen);
    }

    private void removeStaticStats(Player player, ArmourStats armourStats)
    {
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        PlayerStats stats = vowedPlayer.getStats();
        float oldSpeed = armourStats.getSpeed();
        int oldHealth = armourStats.getHP();
        int oldBlock = armourStats.getBlock();
        int oldDodge = armourStats.getDodge();
        int oldArmourPercent = armourStats.getArmourPercent();
        int oldHealthRegen = armourStats.getHPRegen();
        int oldEnergyRegen = armourStats.getEnergyRegen();

        if (oldSpeed != 0)
        {
            double percentToSubtract = (0.2 * (oldSpeed / 100));

            player.setWalkSpeed((float) Integers.round(player.getWalkSpeed() - percentToSubtract, 3));
        }
        if (oldHealth != 0)
        {
            stats.setMaxHealth(stats.getMaxHealth() - oldHealth);
            vowedPlayer.updateHealth();
        }

        stats.setBlock(stats.getBlock() - oldBlock);
        stats.setDodge(stats.getDodge() - oldDodge);
        stats.setSpeed(stats.getSpeed() - oldSpeed);
        stats.setArmourPercent(stats.getArmourPercent() - oldArmourPercent);
        stats.setHealthRegen(stats.getHealthRegen() - oldHealthRegen);
        stats.setEnergyRegen(stats.getEnergyRegen() - oldEnergyRegen);
    }
}
