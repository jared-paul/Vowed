package net.vowed.core.money.currency;

import net.vowed.api.player.races.RaceType;

import java.math.BigDecimal;

/**
 * Created by JPaul on 2017-08-31.
 */
public abstract class CurrencyAbstract implements CurrencyFX
{
    @Override
    public BigDecimal convertCurrencyToCurrency(CurrencyType newCurrency, BigDecimal inValue) throws ArithmeticException
    {
        switch (newCurrency)
        {
            case DWARF:
                return convertCurrencyToDwarf(inValue);
            case ELF:
                return convertCurrencyToElf(inValue);
            case HUMAN:
                return convertCurrencyToHuman(inValue);
        }

        return null;
    }
}
