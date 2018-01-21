package net.vowed.items.food.generator;

import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.core.items.generator.Generator;
import net.vowed.items.food.FoodType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 2017-02-06.
 */
public class FoodGenerator extends Generator<ItemStack>
{
    FoodType foodType;

    public FoodGenerator(FoodType foodType, UUID objectUUID)
    {
        super(false, false, false);

        if (foodType == null)
        {
            foodType = FoodType.values()[ThreadLocalRandom.current().nextInt(FoodType.values().length)];
        }

        this.foodType = foodType;
    }

    @Override
    public Enum getType()
    {
        return foodType;
    }

    @Override
    public String getName()
    {
        return foodType.getName();
    }

    @Override
    public Material getMaterial()
    {
        return foodType.getMaterial();
    }

    @Override
    public ItemStack generateObject(@Nullable Rarity rarity, @Nullable Tier tier)
    {
        return new ItemBuilder(generateItem(rarity, tier)).setName(foodType.getName()).setData(foodType.getData()).getItem();
    }

    public static ItemStack createFood(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable FoodType foodType)
    {
        return new FoodGenerator(foodType, UUID.randomUUID()).generateObject(rarityOverride, tierOverride);
    }
}
