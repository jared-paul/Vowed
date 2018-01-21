package net.vowed.api.items.weapon;

import net.vowed.api.items.Tier;
import org.bukkit.Material;

import javax.annotation.Nullable;

/**
 * Created by JPaul on 3/31/2016.
 */
public enum WeaponType
{
    STAFF(Material.WOOD_HOE, "Staff", Material.STONE_HOE, "Brute Staff", Material.IRON_HOE, "Wizard Staff", Material.DIAMOND_HOE, "Ancient Staff", Material.GOLD_HOE, "Legendary Staff"),
    AXE(Material.WOOD_AXE, "Hatchet", Material.STONE_AXE, "Great Axe", Material.IRON_AXE, "War Axe", Material.DIAMOND_AXE, "Ancient Axe", Material.GOLD_AXE, "Legendary Axe"),
    SWORD(Material.WOOD_SWORD, "Shortsword", Material.STONE_SWORD, "Broadsword", Material.IRON_SWORD, "Magic Sword", Material.DIAMOND_SWORD, "Ancient Sword", Material.GOLD_SWORD, "Legendary Sword"),
    BOW(Material.BOW, "Shortbow", Material.BOW, "Longbow", Material.BOW, "Magic Bow", Material.BOW, "Ancient Bow", Material.BOW, "Legendary Bow");

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

    WeaponType(Material tier1, String tier1Name, Material tier2, String tier2Name, Material tier3, String tier3Name, Material tier4, String tier4Name, Material tier5, String tier5name)
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

    public static WeaponType getTypeFromMat(Material material)
    {
        for (WeaponType weaponType : values())
        {
            if (weaponType.tier1 == material)
            {
                return weaponType;
            }
            else if (weaponType.tier2 == material)
            {
                return weaponType;
            }
            else if (weaponType.tier3 == material)
            {
                return weaponType;
            }
            else if (weaponType.tier4 == material)
            {
                return weaponType;
            }
            else if (weaponType.tier5 == material)
            {
                return weaponType;
            }
        }

        return null;
    }

    public static Material getMatFromType(WeaponType weaponType, @Nullable Tier tier)
    {
        if (tier != null)
        {
            switch (weaponType)
            {
                case STAFF:
                    switch (tier)
                    {
                        case TIER1:
                            return Material.WOOD_HOE;
                        case TIER2:
                            return Material.STONE_HOE;
                        case TIER3:
                            return Material.IRON_HOE;
                        case TIER4:
                            return Material.DIAMOND_HOE;
                        case TIER5:
                            return Material.GOLD_HOE;
                    }
                    break;

                case SWORD:
                    switch (tier)
                    {
                        case TIER1:
                            return Material.WOOD_SWORD;
                        case TIER2:
                            return Material.STONE_SWORD;
                        case TIER3:
                            return Material.IRON_SWORD;
                        case TIER4:
                            return Material.DIAMOND_SWORD;
                        case TIER5:
                            return Material.GOLD_SWORD;
                    }
                    break;

                case AXE:
                    switch (tier)
                    {
                        case TIER1:
                            return Material.WOOD_AXE;
                        case TIER2:
                            return Material.STONE_AXE;
                        case TIER3:
                            return Material.IRON_AXE;
                        case TIER4:
                            return Material.DIAMOND_AXE;
                        case TIER5:
                            return Material.GOLD_AXE;
                    }
                    break;

                case BOW:
                    return Material.BOW;
            }
        }

        return null;
    }

    public static WeaponType getTypeAlias(String string)
    {
        switch (string.toLowerCase())
        {
            case "axe":
                return AXE;

            case "sword":
                return SWORD;

            case "staff":
                return STAFF;

            case "bow":
                return BOW;
        }

        return null;
    }
}
