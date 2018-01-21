package net.vowed.core.money.currency;

import net.vowed.api.player.races.RaceType;

/**
 * Created by JPaul on 11/20/2015.
 */
public enum CurrencyType
{
    DWARF,
    ELF,
    HUMAN;

    public static CurrencyType fromRace(RaceType raceType)
    {
        for (CurrencyType currencyType : CurrencyType.values())
        {
            if (raceType.name().equals(currencyType.name()))
            {
                return currencyType;
            }
        }

        return null;
    }
}
