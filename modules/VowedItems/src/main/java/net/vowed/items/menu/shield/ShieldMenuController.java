package net.vowed.items.menu.shield;

import me.jpaul.menuapi.types.Menu;
import me.jpaul.menuapi.types.MenuController;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.plugin.Vowed;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by JPaul on 2017-02-05.
 */
public class ShieldMenuController extends MenuController
{
    private Tier tier;

    public ShieldMenuController(List<Menu> menus)
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

    public ItemStack getItem(Rarity rarity)
    {
        return Vowed.getItemFactory().createShield(rarity, tier);
    }
}
