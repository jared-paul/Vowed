package net.vowed.clans;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * Created by JPaul on 8/16/2016.
 */
public class Format
{
    public static List<String> formatAllies(List<String> stringList)
    {
        List<String> formattedList = Lists.newArrayList();
        formattedList.add(ChatColor.GOLD + "     Your Allies");

        for (String string : stringList)
        {
            formattedList.add(ChatColor.GREEN + "   " + string);
        }

        return formattedList;
    }


}
