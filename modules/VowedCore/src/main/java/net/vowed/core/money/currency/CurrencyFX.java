package net.vowed.core.money.currency;

import net.vowed.api.player.races.RaceType;

import java.math.BigDecimal;

/**
 * Created by JPaul on 11/22/2015.
 */
public interface CurrencyFX
{
    BigDecimal elfFactor = new BigDecimal("1.35");
    BigDecimal humanFactor = new BigDecimal("1.24");
    BigDecimal dwarfFactor = new BigDecimal("1.12");

    BigDecimal convertCurrencyToCurrency(CurrencyType newCurrency, BigDecimal inValue) throws ArithmeticException;

    BigDecimal convertCurrencyToElf(BigDecimal inValue) throws ArithmeticException;

    BigDecimal convertCurrencyToHuman(BigDecimal inValue) throws ArithmeticException;

    BigDecimal convertCurrencyToDwarf(BigDecimal inValue) throws ArithmeticException;
}
