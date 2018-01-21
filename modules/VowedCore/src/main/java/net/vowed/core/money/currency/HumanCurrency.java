package net.vowed.core.money.currency;

import java.math.BigDecimal;

/**
 * Created by JPaul on 11/22/2015.
 */
public class HumanCurrency extends CurrencyAbstract
{
    @Override
    public BigDecimal convertCurrencyToDwarf(BigDecimal inValue) throws ArithmeticException
    {
        BigDecimal humanDwarfRate = humanFactor.divide(dwarfFactor, 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal dwarf = inValue.multiply(humanDwarfRate);
        dwarf = dwarf.setScale(5, BigDecimal.ROUND_HALF_UP);
        return dwarf;
    }

    @Override
    public BigDecimal convertCurrencyToElf(BigDecimal inValue) throws ArithmeticException
    {
        BigDecimal humanElfRate = humanFactor.divide(elfFactor, 10, BigDecimal.ROUND_HALF_UP);

        BigDecimal elf = inValue.multiply(humanElfRate);
        elf = elf.setScale(5, BigDecimal.ROUND_HALF_UP);
        return elf;
    }

    @Override
    public BigDecimal convertCurrencyToHuman(BigDecimal inValue) throws ArithmeticException
    {
        return inValue;
    }
}
