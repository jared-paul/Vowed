package net.vowed.items.menu.generalGUI;

import com.google.common.collect.Lists;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.items.Tier;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.items.menu.armour.ArmourMenu;
import net.vowed.items.menu.armour.ArmourMenuController;
import net.vowed.items.menu.food.FoodMenu;
import net.vowed.items.menu.rarity.RarityMenu;
import net.vowed.items.menu.shield.ShieldMenuController;
import net.vowed.items.menu.tier.TierMenu;
import net.vowed.items.menu.weapon.WeaponMenu;
import net.vowed.items.menu.weapon.WeaponMenuController;
import net.vowed.items.misc.MiscType;
import net.vowed.items.misc.generator.MiscGenerator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 2017-02-06.
 */
public class GeneralItems
{
    public static class FoodItem extends GeneralItem
    {
        @Override
        public ItemStack getItem()
        {
            return new ItemBuilder(Material.BREAD)
                    .setName(ChatColor.RED + "Food")
                    .setLore(ChatColor.GRAY + "Generate Food!")
                    .getItem();
        }

        @Override
        public void onClick(InventoryClickEvent inventoryClickEvent)
        {
            FoodMenu foodMenu = new FoodMenu();
            foodMenu.showToPlayer(inventoryClickEvent.getWhoClicked());
        }
    }

    public static class OrbOfAlteration extends GeneralItem
    {
        @Override
        public ItemStack getItem()
        {
            return new ItemBuilder(Material.MAGMA_CREAM)
                .setName(ChatColor.RED + "Orb of Revision")
                .setLore(ChatColor.GRAY + "Generate an Orb of Revision!")
                .getItem();
        }

        @Override
        public void onClick(InventoryClickEvent inventoryClickEvent)
        {
            ItemStack orb = MiscGenerator.createItem(null, null, MiscType.ORB_OF_REVISION);
            inventoryClickEvent.getWhoClicked().getInventory().addItem(orb);
        }
    }

    public static class WeaponItem extends GeneralItem
    {
        @Override
        public ItemStack getItem()
        {
            return new ItemBuilder(WeaponType.getMatFromType(WeaponType.AXE, Tier.TIER4))
                    .setName(ChatColor.RED + "Weapon")
                    .setLore(ChatColor.GRAY + "Generate a random weapon")
                    .getItem();
        }

        @Override
        public void onClick(InventoryClickEvent inventoryClickEvent)
        {
            WeaponMenuController weaponMenuController = new WeaponMenuController(Lists.newArrayList(new TierMenu(), new WeaponMenu(), new RarityMenu()));
            weaponMenuController.showFirstMenu(inventoryClickEvent.getWhoClicked());
        }
    }

    public static class ArmourItem extends GeneralItem
    {
        @Override
        public ItemStack getItem()
        {
            return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                    .setName(ChatColor.RED + "Armour")
                    .setLore(ChatColor.GRAY + "Generate a random piece of armour!")
                    .setLeatherColour(Tier.TIER4.getColour())
                    .getItem();
        }

        @Override
        public void onClick(InventoryClickEvent inventoryClickEvent)
        {
            ArmourMenuController armourMenuController = new ArmourMenuController(Lists.newArrayList(new TierMenu(), new ArmourMenu(), new RarityMenu()));
            armourMenuController.showFirstMenu(inventoryClickEvent.getWhoClicked());
        }
    }

    public static class ShieldItem extends GeneralItem
    {
        @Override
        public ItemStack getItem()
        {
            return new ItemBuilder(Material.SHIELD)
                    .setName(ChatColor.RED + "Shield")
                    .setLore(ChatColor.GRAY + "Generate a random shield!")
                    .getItem();
        }

        @Override
        public void onClick(InventoryClickEvent inventoryClickEvent)
        {
            ShieldMenuController shieldMenuController = new ShieldMenuController(Lists.newArrayList(new TierMenu(), new RarityMenu()));
            shieldMenuController.showFirstMenu(inventoryClickEvent.getWhoClicked());
        }
    }
}
