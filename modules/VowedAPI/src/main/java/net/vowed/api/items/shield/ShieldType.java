package net.vowed.api.items.shield;

import net.vowed.api.items.Tier;
import org.bukkit.DyeColor;
import org.bukkit.Material;

/**
 * Created by JPaul on 3/31/2016.
 */
public enum ShieldType
{
    SHIELD("Broken Shield", DyeColor.PURPLE, "Adequate Shield", DyeColor.GRAY, "Sturdy Shield", DyeColor.ORANGE, "Ancient Shield", DyeColor.CYAN, "Legendary Shield", DyeColor.YELLOW);

    private Material material;

    private String tier1Name;
    private DyeColor tier1Colour;
    private String tier2Name;
    private DyeColor tier2Colour;
    private String tier3Name;
    private DyeColor tier3Colour;
    private String tier4Name;
    private DyeColor tier4Colour;
    private String tier5name;
    private DyeColor tier5Colour;

    ShieldType(String tier1Name, DyeColor tier1Colour, String tier2Name, DyeColor tier2Colour, String tier3Name, DyeColor tier3Colour, String tier4Name, DyeColor tier4Colour, String tier5name, DyeColor tier5Colour)
    {
        this.material = Material.SHIELD;
        this.tier1Name = tier1Name;
        this.tier1Colour = tier1Colour;
        this.tier2Name = tier2Name;
        this.tier2Colour = tier2Colour;
        this.tier3Name = tier3Name;
        this.tier3Colour = tier3Colour;
        this.tier4Name = tier4Name;
        this.tier4Colour = tier4Colour;
        this.tier5name = tier5name;
        this.tier5Colour = tier5Colour;
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
}
