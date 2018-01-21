package net.vowed.items.menu.generalGUI;

import me.jpaul.menuapi.items.MenuItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 2017-02-06.
 */
public abstract class GeneralItem implements MenuItem
{
    @Override
    public abstract ItemStack getItem();

    @Override
    public abstract void onClick(InventoryClickEvent inventoryClickEvent);
}
