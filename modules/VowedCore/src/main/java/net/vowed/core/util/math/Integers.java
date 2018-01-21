package net.vowed.core.util.math;

import java.math.BigDecimal;

/**
 * Created by JPaul on 1/14/2016.
 */
public class Integers
{
    public static boolean isInteger(String s)
    {
        try
        {
            Integer.parseInt(s);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public static double round(double value, int numberOfDigitsAfterDecimalPoint)
    {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_HALF_UP);

        return bigDecimal.doubleValue();
    }

    public static int roundUpPositive(double d)
    {
        int i = (int) d;
        double remainder = d - i;
        if (remainder > 0.0)
        {
            i++;
        }
        if (i < 0) return 0;
        return i;
    }

    public static int roundUpPositiveWithMax(double d, int max)
    {
        int result = roundUpPositive(d);
        if (d > max) return max;
        return result;
    }
}
