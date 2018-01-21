package net.vowed.shops.menu;

import me.jpaul.menuapi.types.MultiMenu;
import me.jpaul.menuapi.types.Page;
import net.vowed.shops.ChestShop;

import java.util.List;

/**
 * Created by JPaul on 4/13/2016.
 */
public class ShopMenu extends MultiMenu
{
    public ShopMenu(ChestShop shop, List<Page> pages)
    {
        super(36, "", pages);
    }
}
