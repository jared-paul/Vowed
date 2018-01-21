package net.vowed.api.items;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 1/24/2016.
 */
public enum Rarity
{
    LEGENDARY(ChatColor.GOLD),
    RARE(ChatColor.AQUA),
    UNCOMMON(ChatColor.DARK_RED),
    COMMON(ChatColor.DARK_PURPLE);

    ChatColor chatColour;

    Rarity(ChatColor chatColour)
    {
        this.chatColour = chatColour;
    }

    public ChatColor getChatColour()
    {
        return chatColour;
    }

    public static Rarity getRarityFromItem(ItemStack item)
    {
        if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return null;

        for (String string : item.getItemMeta().getLore())
        {
            for (Rarity rarity : values())
            {
                if (string.toLowerCase().contains(rarity.name().toLowerCase()))
                {
                    return valueOf(ChatColor.stripColor(string.substring(string.indexOf(":") + 2)).toUpperCase());
                }
            }
        }

        return null;
    }

    public static Rarity getRarityAlias(String string)
    {
        switch (string.toLowerCase())
        {
            case "legendary":
                return LEGENDARY;

            case "rare":
                return RARE;

            case "uncommon":
                return UNCOMMON;

            case "common":
                return COMMON;
        }

        return null;
    }

    public static Rarity getRarityChance()
    {
        int rarityChance = ThreadLocalRandom.current().nextInt(100);
        Rarity rarity;

        if (rarityChance > 98)
        {
            rarity = Rarity.LEGENDARY;
        }
        else if (rarityChance > 94)
        {
            rarity = Rarity.RARE;
        }
        else if (rarityChance > 78)
        {
            rarity = Rarity.UNCOMMON;
        }
        else
        {
            rarity = Rarity.COMMON;
        }

        return rarity;
    }
}
