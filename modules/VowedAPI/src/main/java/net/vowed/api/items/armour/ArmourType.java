package net.vowed.api.items.armour;

import net.vowed.api.items.Tier;
import org.bukkit.Material;

/**
 * Created by JPaul on 3/31/2016.
 */
public enum ArmourType
{
    HELMET(Material.LEATHER_HELMET, "Leather Coif", Material.CHAINMAIL_HELMET, "Medium Helmet", Material.IRON_HELMET, "Full Helmet", Material.DIAMOND_HELMET, "Ancient Full Helmet", Material.LEATHER_HELMET, "Legendary Full Helmet"),
    CHESTPLATE(Material.LEATHER_CHESTPLATE, "Leather Chestplate", Material.CHAINMAIL_CHESTPLATE, "Chainmail", Material.IRON_CHESTPLATE, "Platemail", Material.DIAMOND_CHESTPLATE, "Magic Platemail", Material.LEATHER_CHESTPLATE, "Legendary Platemail"),
    LEGGINGS(Material.LEATHER_LEGGINGS, "Leather Leggings", Material.CHAINMAIL_LEGGINGS, "Chainmail Leggings", Material.IRON_LEGGINGS, "Platemail Leggings", Material.DIAMOND_LEGGINGS, "Magic Platemail Leggings", Material.LEATHER_LEGGINGS, "Legendary Platemail Leggings"),
    BOOTS(Material.LEATHER_BOOTS, "Leather Boots", Material.CHAINMAIL_BOOTS, "Chainmail Boots", Material.IRON_BOOTS, "Platemail Boots", Material.DIAMOND_BOOTS, "Magic Platemail Boots", Material.LEATHER_BOOTS, "Legendary Platemail Boots");

    private Material tier1;
    private String tier1Name;

    private Material tier2;
    private String tier2Name;

    private Material tier3;
    private String tier3Name;

    private Material tier4;
    private String tier4Name;

    private Material tier5;
    private String tier5name;

    ArmourType(Material tier1, String tier1Name, Material tier2, String tier2Name, Material tier3, String tier3Name, Material tier4, String tier4Name, Material tier5, String tier5name)
    {
        this.tier1 = tier1;
        this.tier1Name = tier1Name;
        this.tier2 = tier2;
        this.tier2Name = tier2Name;
        this.tier3 = tier3;
        this.tier3Name = tier3Name;
        this.tier4 = tier4;
        this.tier4Name = tier4Name;
        this.tier5 = tier5;
        this.tier5name = tier5name;
    }

    public String getTierName(Tier tier)
    {
        switch (tier)
        {
            case TIER1:
                return tier1Name;
            case TIER2:
                return tier2Name;
            case TIER3:
                return tier3Name;
            case TIER4:
                return tier4Name;
            case TIER5:
                return tier5name;
        }

        return null;
    }

    public static ArmourType getTypeFromMat(Material material)
    {
        for (ArmourType armourType : values())
        {
            if (armourType.tier1 == material)
            {
                return armourType;
            }
            else if (armourType.tier2 == material)
            {
                return armourType;
            }
            else if (armourType.tier3 == material)
            {
                return armourType;
            }
            else if (armourType.tier4 == material)
            {
                return armourType;
            }
            else if (armourType.tier5 == material)
            {
                return armourType;
            }
        }

        return null;
    }

    public static Material getMatFromType(ArmourType armourType)
    {
        switch (armourType)
        {
            case HELMET:
                return Material.LEATHER_HELMET;
            case CHESTPLATE:
                return Material.LEATHER_CHESTPLATE;
            case LEGGINGS:
                return Material.LEATHER_LEGGINGS;
            case BOOTS:
                return Material.LEATHER_BOOTS;
        }

        return null;
    }

    public static ArmourType getTypeAlias(String string)
    {
        switch (string.toLowerCase())
        {
            case "pants":
            case "leggings":
            case "legs":
                return LEGGINGS;

            case "chest":
            case "chestplate":
                return CHESTPLATE;

            case "helm":
            case "helmet":
                return HELMET;

            case "boots":
            case "shoes":
                return BOOTS;
        }

        return null;
    }
}
