package net.vowed.shops;

import com.google.common.collect.Lists;
import me.jpaul.menuapi.items.MenuItem;
import net.vowed.api.player.races.RaceType;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.List;

/**
 * Created by JPaul on 4/13/2016.
 */
public class ShopItemDaughter implements MenuItem
{
    private ShopItem parent;
    private RaceType race;

    public ShopItemDaughter(ShopItem parent, RaceType race)
    {
        this.parent = parent;
        this.race = race;
    }

    public ShopItem getParent()
    {
        return parent;
    }

    public void setParent(ShopItem parent)
    {
        this.parent = parent;
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {
        parent.onClick(clickEvent);
    }

    @Override
    public ItemStack getItem()
    {
        ItemStack fakeItem = parent.getItem().clone();

        ItemMeta itemMeta = fakeItem.getItemMeta();
        List<String> lore = itemMeta.getLore();

        if (lore != null)
        {
            Iterator<String> loreIterator = lore.iterator();
            while (loreIterator.hasNext())
            {
                String line = loreIterator.next();
                if (line.contains("Price:"))
                {
                    loreIterator.remove();
                }
            }
        }
        else
        {
            lore = Lists.newArrayList();
        }

        String yourPrice = ChatColor.RESET.toString() +
                ChatColor.RED +
                "Your " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + parent.getPrice(race) + "$";

        lore.add(yourPrice);
        itemMeta.setLore(lore);
        fakeItem.setItemMeta(itemMeta);

        return fakeItem;
    }
}
