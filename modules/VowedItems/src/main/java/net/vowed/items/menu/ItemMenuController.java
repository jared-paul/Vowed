package net.vowed.items.menu;

import me.jpaul.menuapi.types.Menu;
import me.jpaul.menuapi.types.MenuController;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by JPaul on 2017-02-07.
 */
public abstract class ItemMenuController extends MenuController
{
    protected Tier tier;

    public ItemMenuController(List<Menu> menus)
    {
        super(menus);
    }

    public Tier getTier()
    {
        return tier;
    }

    public void setTier(Tier tier)
    {
        this.tier = tier;
    }

    public abstract ItemMenu getItemMenu();

    public abstract ItemStack getItem(Rarity rarity);
}
