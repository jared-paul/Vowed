package net.vowed.core.money.currency;

/**
 * Created by JPaul on 11/22/2015.
 */
public class CurrencyFactory
{
    public static CurrencyFX getInstance(CurrencyType currencyType)
    {
        switch (currencyType)
        {
            case ELF:
                return new ElfCurrency();
            case HUMAN:
                return new HumanCurrency();
            case DWARF:
                return new DwarfCurrency();
        }

        return null;
    }
}
