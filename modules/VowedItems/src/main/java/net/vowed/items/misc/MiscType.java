package net.vowed.items.misc;

import com.google.common.collect.Lists;
import net.vowed.api.items.Tier;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JPaul on 6/8/2016.
 */
public enum MiscType
{
    ORB_OF_REVISION(Material.MAGMA_CREAM, Lists.newArrayList(Tier.TIER4, Tier.TIER5), "Orb of Revision", "Reroll the stats on any piece of armour or weapon!");

    Material material;
    List<Tier> applicableTiers; //for loot/chests
    String name;
    String lore;

    MiscType(Material material, List<Tier> tiers, String name, String lore)
    {
        this.material = material;
        this.applicableTiers = tiers;
        this.name = name;
        this.lore = lore;
    }

    public Material getMaterial()
    {
        return material;
    }

    public List<Tier> getApplicableTiers()
    {
        return applicableTiers;
    }

    public String getName()
    {
        return ChatColor.GOLD + name;
    }

    public List<String> getLore()
    {
        //splits the lines up to avoid one line lores
        List<String> matchList = Lists.newArrayList();
        Pattern regex = Pattern.compile(".{1,30}(?:\\s|$)", Pattern.DOTALL);
        Matcher regexMatcher = regex.matcher(lore);
        while (regexMatcher.find())
        {
            matchList.add(ChatColor.GRAY + regexMatcher.group());
        }

        return matchList;
    }
}
