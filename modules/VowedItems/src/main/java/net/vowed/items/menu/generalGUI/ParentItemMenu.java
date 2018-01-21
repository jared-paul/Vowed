package net.vowed.items.menu.generalGUI;

import me.jpaul.menuapi.types.Menu;

/**
 * Created by JPaul on 2017-02-06.
 */
public class ParentItemMenu extends Menu
{
    public ParentItemMenu()
    {
        super(9, "Create an Item");
        addItems();
    }

    private void addItems()
    {
        addItem(new GeneralItems.ArmourItem());
        addItem(new GeneralItems.ShieldItem());
        addItem(new GeneralItems.WeaponItem());
        addItem(new GeneralItems.OrbOfAlteration());
        addItem(new GeneralItems.FoodItem());
    }
}
