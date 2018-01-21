package net.vowed.core.items.generator;

import net.vowed.core.items.config.AbstractStatStorage;
import net.vowed.core.items.config.TripleStatStorage;

import java.util.concurrent.ThreadLocalRandom;

/**
 * All thanks to vilsol
 */
public class ModifierRange
{
    ModifierType modifierType;
    int low;
    public int lowHigh;
    int high;
    boolean half;

    String random;

    public ModifierRange(ModifierType modifierType, int low, int high)
    {
        this.modifierType = modifierType;
        this.low = low;
        this.high = high;
    }

    public ModifierRange(ModifierType modifierType, int low, int high, boolean half)
    {
        this.modifierType = modifierType;
        this.low = low;
        this.high = high;
        this.half = half;
    }


    public ModifierRange(ModifierType modifierType, AbstractStatStorage damageStorage)
    {
        this.modifierType = modifierType;
        this.low = damageStorage.statMIN;
        this.high = damageStorage.statMAX;

        if (damageStorage instanceof TripleStatStorage)
        {
            this.lowHigh = ((TripleStatStorage) damageStorage).statLowHigh;
        }
    }

    public ModifierRange(ModifierType modifierType, int low, int lowHigh, int high)
    {
        this.modifierType = modifierType;
        this.low = low;
        this.lowHigh = lowHigh;
        this.high = high;
    }

    public String generateRandom()
    {
        String random = "";
        ThreadLocalRandom r = ThreadLocalRandom.current();

        int first;

        if (high - low > 0)
        {
            int bound = high + 1 - low;
            int randomINT = r.nextInt(bound);

            first = randomINT + low;
        }
        else
        {
            first = low;
        }

        int second = high;

        if (modifierType == ModifierType.RANGE)
        {

            if (high - first > 0)
            {
                second = r.nextInt(high - first) + first;
            }

            random += String.valueOf(first);
            random += " - ";
            random += String.valueOf(second);

        }
        else if (modifierType == ModifierType.TRIPLE)
        {

            if (lowHigh - low > 0)
            {
                first = r.nextInt(lowHigh - low) + low;
            }
            else
            {
                first = low;
            }

            if (high - first > 0)
            {
                second = r.nextInt(high - first) + first;
            }

            random += String.valueOf(first) + " - " + String.valueOf(second);

        }
        else
        {
            if (half)
            {
                if (first / 2 >= 1)
                {
                    random += String.valueOf(first / 2);
                }
                else
                {
                    random += "1";
                }
            }
            else
            {
                random += String.valueOf(first);
            }
        }

        this.random = random;
        return random;
    }

    public String getRandom()
    {
        return random;
    }
}
