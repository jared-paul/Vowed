package net.vowed.clans;

import net.vowed.core.VowedColours;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by JPaul on 8/16/2016.
 */
public class StringUtil
{
    public static String capitalizeFirst(String origin)
    {
        return origin.length() == 0 ? origin : origin.substring(0, 1).toUpperCase() + origin.substring(1);
    }

    public static String handleError(String message, String... error)
    {
        //have to convert to array due to replaceeach displaying x amount of lines based on the number of errors
        List<String> array = new ArrayList<>();
        Collections.addAll(array, error);
        String[] errors = array.toArray(new String[array.size()]);

        String capitalized = capitalizeFirst(message);

        return StringUtils.replaceEach(VowedColours.ERROR + capitalized, errors, handleErrorColours(errors));
    }

    private static String[] handleErrorColours(String[] errors)
    {
        List<String> colouredStrings = new ArrayList<>();

        for (String error : errors)
        {
            colouredStrings.add(VowedColours.ERROR_KEYWORD + error + ChatColor.RESET + VowedColours.ERROR);
        }

        return colouredStrings.toArray(new String[colouredStrings.size()]);
    }
}
