package net.vowed.items.menu.food;

import me.jpaul.menuapi.types.Menu;
import net.vowed.items.food.FoodType;
import net.vowed.items.menu.BackItem;
import net.vowed.items.menu.generalGUI.ParentItemMenu;
import org.bukkit.ChatColor;

/**
 * Created by JPaul on 2017-02-06.
 */
public class FoodMenu extends Menu
{
    public FoodMenu()
    {
        super(36, ChatColor.DARK_RED + "Food");
        addItems();
    }

    public void addItems()
    {
        for (FoodType foodType : FoodType.values())
        {
            addItem(new FoodItem(foodType));
        }

        addItem(getSize() - 1, new BackItem(new ParentItemMenu()));
    }
}
