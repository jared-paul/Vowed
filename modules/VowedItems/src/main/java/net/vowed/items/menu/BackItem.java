package net.vowed.items.menu;

import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.types.Menu;
import me.jpaul.menuapi.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 2017-02-07.
 */
public class BackItem implements MenuItem
{
    Menu menuToReturnTo;

    public BackItem(Menu menuToReturnTo)
    {
        this.menuToReturnTo = menuToReturnTo;
    }

    @Override
    public ItemStack getItem()
    {
        return new ItemBuilder(Material.NETHER_STAR)
                .setName(ChatColor.GOLD + "Return Home")
                .getItem();
    }

    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent)
    {
        menuToReturnTo.showToPlayer(inventoryClickEvent.getWhoClicked());
    }
}
