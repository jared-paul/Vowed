package net.vowed.items.menu.food;

import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.items.food.FoodType;
import net.vowed.items.food.generator.FoodGenerator;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 2017-02-06.
 */
public class FoodItem implements MenuItem
{
    private FoodType foodType;

    public FoodItem(FoodType foodType)
    {
        this.foodType = foodType;
    }

    @Override
    public ItemStack getItem()
    {
        return new ItemBuilder(foodType.getMaterial())
                .setName(ChatColor.RED + foodType.getName())
                .setData(foodType.getData())
                .getItem();
    }

    @Override
    public void onClick(InventoryClickEvent inventoryClickEvent)
    {
        ItemStack food = FoodGenerator.createFood(null, null, foodType);
        inventoryClickEvent.getWhoClicked().getInventory().addItem(food);
    }
}
