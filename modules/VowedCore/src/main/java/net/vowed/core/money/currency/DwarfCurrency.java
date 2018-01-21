package net.vowed.core.money.currency;

import net.vowed.api.player.races.RaceType;

import java.math.BigDecimal;

/**
 * Created by JPaul on 11/22/2015.
 */
public class DwarfCurrency extends CurrencyAbstract
{
    @Override
    public BigDecimal convertCurrencyToElf(BigDecimal inValue) throws ArithmeticException
    {
        BigDecimal dwarfElfRate = dwarfFactor.divide(elfFactor, 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal elf = inValue.multiply(dwarfElfRate);
        elf = elf.setScale(5, BigDecimal.ROUND_HALF_UP);
        return elf;
    }

    @Override
    public BigDecimal convertCurrencyToHuman(BigDecimal inValue) throws ArithmeticException
    {
        BigDecimal dwarfHumanRate = dwarfFactor.divide(humanFactor, 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal human = inValue.multiply(dwarfHumanRate);
        human = human.setScale(5, BigDecimal.ROUND_HALF_UP);
        return human;
    }

    @Override
    public BigDecimal convertCurrencyToDwarf(BigDecimal inValue) throws ArithmeticException
    {
        return inValue;
    }
}
