package net.vowed.items.food;

import com.google.common.collect.Lists;
import net.vowed.api.items.Tier;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

/**
 * Created by JPaul on 2017-02-06.
 */
public enum FoodType
{
    BREAD(Material.BREAD, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Bread"),
    APPLE(Material.APPLE, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Apple"),
    GOLDEN_APPLE(Material.GOLDEN_APPLE, 0, Lists.newArrayList(Tier.TIER4, Tier.TIER5), "Golden Apple"),
    CARROT(Material.CARROT_ITEM, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Carrot"),
    GOLDEN_CARROT(Material.GOLDEN_CARROT, 0, Lists.newArrayList(Tier.TIER5), "Golden Carrot"),
    MELON(Material.MELON, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Melon"),
    POTATO(Material.POTATO, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Potato"),
    BAKED_POTATO(Material.BAKED_POTATO, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Baked Potato"),
    COOKIE(Material.COOKIE, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Cookie"),
    PUMPKIN_PIE(Material.PUMPKIN_PIE, 0, Lists.newArrayList(Tier.TIER3, Tier.TIER4, Tier.TIER5), "Pumpkin Pie"),
    PORK(Material.PORK, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Pork"),
    COOKED_PORK(Material.GRILLED_PORK, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Cooked Pork"),
    RAW_BEEF(Material.RAW_BEEF, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Raw Beef"),
    STEAK(Material.COOKED_BEEF, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Steak"),
    RAW_MUTTON(Material.MUTTON, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Raw Mutton"),
    COOKED_MUTTON(Material.COOKED_MUTTON, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Cooked Mutton"),
    RAW_CHICKEN(Material.RAW_CHICKEN, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Raw Chicken"),
    COOKED_CHICKEN(Material.COOKED_CHICKEN, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Cooked Chicken"),
    CAKE(Material.CAKE, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Cake"),
    RABBIT_STEW(Material.RABBIT_STEW, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Rabbit Stew"),
    BEETROOT_STEW(Material.BEETROOT_SOUP, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Beetroot Stew"),
    MUSHROOM_SOUP(Material.MUSHROOM_SOUP, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Mushroom Soup"),
    RAW_FISH(Material.RAW_FISH, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Raw Fish"),
    COOKED_FISH(Material.COOKED_FISH, 0, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Cooked Fish"),
    SALMON(Material.RAW_FISH, 1, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Salmon"),
    COOKED_SALMON(Material.COOKED_FISH, 1, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Cooked Salmon"),
    CLOWNFISH(Material.RAW_FISH, 2, Lists.newArrayList(Tier.TIER1, Tier.TIER2, Tier.TIER3, Tier.TIER4, Tier.TIER5), "Clownfish"),
    PUFFERFISH(Material.RAW_FISH, 3, Lists.newArrayList(Tier.TIER4, Tier.TIER5), "Pufferfish");

    Material material;
    short data;
    List<Tier> applicableTiers; //for chests/loot
    String name;

    FoodType(Material material, int data, List<Tier> applicableTiers, String name)
    {
        this.material = material;
        this.data = (short) data;
        this.applicableTiers = applicableTiers;
        this.name = name;
    }

    public Material getMaterial()
    {
        return material;
    }

    public short getData()
    {
        return data;
    }

    public List<Tier> getApplicableTiers()
    {
        return applicableTiers;
    }

    public String getName()
    {
        return ChatColor.RED + name;
    }
}
