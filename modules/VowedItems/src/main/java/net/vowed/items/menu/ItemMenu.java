package net.vowed.items.menu;

import me.jpaul.menuapi.types.Menu;
import net.vowed.api.items.Tier;

/**
 * Created by JPaul on 2017-02-07.
 */
public abstract class ItemMenu extends Menu
{
    public ItemMenu(int size, String name)
    {
        super(size, name);
    }

    public abstract void addItems(Tier tier);
}
