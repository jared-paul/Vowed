package net.vowed.core.money;

import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.player.races.RaceType;
import net.vowed.core.money.currency.CurrencyFactory;
import net.vowed.core.money.currency.CurrencyType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

/**
 * Created by JPaul on 5/11/2016.
 */
public class MoneyUtil
{
    public static boolean isMoneyItem(ItemStack item)
    {
        for (MoneyItem moneyItem : MoneyItem.getMoneyItems())
        {
            if (MoneyItem.getFromMoney(moneyItem) == item.getType())
            {
                return true;
            }
        }

        return false;
    }

    public static double convertToRace(RaceType raceType, Material material, int amount)
    {
        switch (material)
        {
            case DIAMOND:
                switch (raceType)
                {
                    case DWARF:
                        return CurrencyFactory.getInstance(CurrencyType.ELF).convertCurrencyToDwarf(BigDecimal.valueOf(amount)).doubleValue();
                    case HUMAN:
                        return CurrencyFactory.getInstance(CurrencyType.ELF).convertCurrencyToHuman(BigDecimal.valueOf(amount)).doubleValue();
                    case ELF:
                        return amount;
                }
                break;

            case GOLD_NUGGET:
                switch (raceType)
                {
                    case DWARF:
                        return amount;
                    case HUMAN:
                        return CurrencyFactory.getInstance(CurrencyType.DWARF).convertCurrencyToHuman(BigDecimal.valueOf(amount)).doubleValue();
                    case ELF:
                        return CurrencyFactory.getInstance(CurrencyType.DWARF).convertCurrencyToElf(BigDecimal.valueOf(amount)).doubleValue();
                }
                break;

            case EMERALD:
                switch (raceType)
                {
                    case DWARF:
                        return CurrencyFactory.getInstance(CurrencyType.HUMAN).convertCurrencyToDwarf(BigDecimal.valueOf(amount)).doubleValue();
                    case HUMAN:
                        return amount;
                    case ELF:
                        return CurrencyFactory.getInstance(CurrencyType.HUMAN).convertCurrencyToElf(BigDecimal.valueOf(amount)).doubleValue();
                }
        }

        return Double.parseDouble(null); //want to throw exception
    }



    public static BigDecimal convertToRace(RaceType from, RaceType to, double amount)
    {
        return CurrencyFactory.getInstance(CurrencyType.fromRace(from)).convertCurrencyToCurrency(CurrencyType.fromRace(to), BigDecimal.valueOf(amount));
    }

    public static double convertToRace(RaceType race, IVowedPlayer player)
    {
        switch (race)
        {
            case DWARF:
                return convertToDwarf(player);
            case ELF:
                return convertToElf(player);
            case HUMAN:
                return convertToHuman(player);
        }

        return Double.parseDouble(null); //want to throw exception
    }

    private static double convertToDwarf(IVowedPlayer vowedPlayer)
    {
        return CurrencyFactory.getInstance(CurrencyType.fromRace(vowedPlayer.getRace())).convertCurrencyToDwarf(BigDecimal.valueOf(vowedPlayer.getMoney())).doubleValue();
    }

    private static double convertToElf(IVowedPlayer vowedPlayer)
    {
        return CurrencyFactory.getInstance(CurrencyType.fromRace(vowedPlayer.getRace())).convertCurrencyToElf(BigDecimal.valueOf(vowedPlayer.getMoney())).doubleValue();
    }

    private static double convertToHuman(IVowedPlayer vowedPlayer)
    {
        return CurrencyFactory.getInstance(CurrencyType.fromRace(vowedPlayer.getRace())).convertCurrencyToHuman(BigDecimal.valueOf(vowedPlayer.getMoney())).doubleValue();
    }
}
