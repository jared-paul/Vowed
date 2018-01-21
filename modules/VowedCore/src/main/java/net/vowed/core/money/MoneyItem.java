package net.vowed.core.money;

import org.bukkit.Material;

/**
 * Created by JPaul on 2/8/2016.
 */
public enum MoneyItem
{
    DIAMOND,
    GOLD,
    EMERALD;

    public static Material getFromMoney(MoneyItem moneyItem)
    {
        switch (moneyItem)
        {
            case DIAMOND:
                return Material.DIAMOND;
            case GOLD:
                return Material.GOLD_NUGGET;
            case EMERALD:
                return Material.EMERALD;
        }

        return null;
    }

    public static MoneyItem getFromMaterial(Material material)
    {
        switch (material)
        {
            case DIAMOND:
                return MoneyItem.DIAMOND;
            case GOLD_NUGGET:
                return MoneyItem.GOLD;
            case EMERALD:
                return MoneyItem.EMERALD;
        }

        return null;
    }

    public static MoneyItem[] getMoneyItems()
    {
        return new MoneyItem[]{DIAMOND, GOLD, EMERALD};
    }
}
