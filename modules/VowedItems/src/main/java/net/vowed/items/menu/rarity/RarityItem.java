package net.vowed.items.menu.rarity;

import me.jpaul.menuapi.MenuAPI;
import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.types.MenuController;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.items.Rarity;
import net.vowed.items.menu.ItemMenuController;
import net.vowed.items.menu.shield.ShieldMenuController;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 4/4/2016.
 */
public class RarityItem implements MenuItem
{
    Rarity rarity;

    public RarityItem(Rarity rarity)
    {
        this.rarity = rarity;
    }

    @Override
    public ItemStack getItem()
    {
        switch (rarity)
        {
            case COMMON:
                return new ItemBuilder(Material.COAL)
                        .setName(ChatColor.RED + "Common")
                        .setLore(ChatColor.GRAY + "Generate a random common item")
                        .getItem();
            case UNCOMMON:
                return new ItemBuilder(Material.EMERALD)
                        .setName(ChatColor.RED + "Uncommon")
                        .setLore(ChatColor.GRAY + "Generate a random uncommon item")
                        .getItem();
            case RARE:
                return new ItemBuilder(Material.DIAMOND)
                        .setName(ChatColor.RED + "Rare")
                        .setLore(ChatColor.GRAY + "Generate a random rare item")
                        .getItem();
            case LEGENDARY:
                return new ItemBuilder(Material.GOLD_INGOT)
                        .setName(ChatColor.RED + "Legendary")
                        .setLore(ChatColor.GRAY + "Generate a random legendary item")
                        .getItem();
        }

        return null;
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();

        MenuController menuController = MenuAPI.getMenuManager().getMenu(player).getController();

        if (menuController instanceof ShieldMenuController)
        {
            ItemStack shield = ((ShieldMenuController) menuController).getItem(rarity);
            player.getInventory().addItem(shield);
        }
        else
        {
            ItemMenuController itemMenuController = (ItemMenuController) MenuAPI.getMenuManager().getMenu(player).getController();
            ItemStack item = itemMenuController.getItem(rarity);
            player.getInventory().addItem(item);
        }
    }
}
