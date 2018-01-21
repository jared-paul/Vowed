package net.vowed.shops.menu;

import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.shops.ChestShop;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 4/13/2016.
 */
public class OpenButton implements MenuItem
{
    ChestShop shop;

    public OpenButton(ChestShop shop)
    {
        this.shop = shop;
    }

    @Override
    public ItemStack getItem()
    {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .setData((byte) 5)
                .setName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Close your shop!")
                .getItem();
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {
        shop.setOpen(false);

        shop.removeItem(this);
        shop.addItem(new CloseButton(shop), 8);
    }
}
