package net.vowed.api.items;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * Created by JPaul on 1/18/2016.
 */
public enum Tier
{
    TIER1(Material.WOOD_SWORD, Material.WOOD_AXE, Material.WOOD_HOE, Color.PURPLE, ChatColor.DARK_PURPLE, DyeColor.PURPLE, 1, 201, 201),
    TIER2(Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_HOE, Color.SILVER, ChatColor.GRAY, DyeColor.GRAY, 2, 401, 401),
    TIER3(Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_HOE, Color.ORANGE, ChatColor.GOLD, DyeColor.ORANGE, 3, 601, 601),
    TIER4(Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_HOE, Color.TEAL, ChatColor.DARK_AQUA, DyeColor.CYAN, 4, 1001, 1001),
    TIER5(Material.GOLD_SWORD, Material.GOLD_AXE, Material.GOLD_HOE, Color.fromRGB(255, 223, 0), ChatColor.YELLOW, DyeColor.YELLOW, 5, 1401, 1401);

    private Material[] materials;
    private Color color;
    private ChatColor chatColour;
    private DyeColor dyeColour;
    private int tierNumber;
    private int weaponDurability;
    private int armourDurability;

    Tier(Material sword, Material woodAxe, Material woodSword, Color armourColour, ChatColor chatColour, DyeColor dyeColour, int tierNumber, int weaponDurability, int armourDurability)
    {
        this.materials = new Material[]{sword, woodAxe, woodSword};
        this.color = armourColour;
        this.chatColour = chatColour;
        this.dyeColour = dyeColour;
        this.tierNumber = tierNumber;
        this.weaponDurability = weaponDurability;
        this.armourDurability = armourDurability;
    }

    public Material[] getMaterials()
    {
        return materials;
    }
    public Color getColour()
    {
        return color;
    }

    public ChatColor getChatColour()
    {
        return chatColour;
    }

    public int getTierNumber()
    {
        return tierNumber;
    }

    public int getWeaponMAXDurability()
    {
        return weaponDurability;
    }

    public int getArmourMAXDurability()
    {
        return armourDurability;
    }

    public static Tier getTierFromWeapon(Material weapon)
    {
        // TODO: make this check if item is armour or weapon, am too lazy atm
        String name = weapon.toString().toLowerCase();

        if (name.startsWith("wood"))
        {
            return TIER1;
        }
        else if (name.startsWith("stone"))
        {
            return TIER2;
        }
        else if (name.startsWith("iron"))
        {
            return TIER3;
        }
        else if (name.startsWith("diamond"))
        {
            return TIER4;
        }
        else if (name.startsWith("gold"))
        {
            return TIER5;
        }

        return null;
    }

    public static Tier getTierFromItem(ItemStack item)
    {
        String materialName = item.getType().name().toLowerCase();

        if (item.hasItemMeta() && item.getItemMeta() instanceof LeatherArmorMeta)
        {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

            if (meta.getColor() == Color.PURPLE)
            {
                return TIER1;
            }
            else if (meta.getColor() == Color.SILVER)
            {
                return TIER2;
            }
            else if (meta.getColor() == Color.ORANGE)
            {
                return TIER3;
            }
            else if (meta.getColor().equals(Color.TEAL))
            {
                return TIER4;
            }
            else if (meta.getColor().getRed() == 255 && meta.getColor().getGreen() == 223 && meta.getColor().getBlue() == 0)
            {
                return TIER5;
            }
        }
        else
        {
            if (materialName.startsWith("wood"))
            {
                return TIER1;
            }
            else if (materialName.startsWith("stone"))
            {
                return TIER2;
            }
            else if (materialName.startsWith("iron"))
            {
                return TIER3;
            }
            else if (materialName.startsWith("diamond"))
            {
                return TIER4;
            }
            else if (materialName.startsWith("gold"))
            {
                return TIER5;
            }
            else if (materialName.startsWith("bow"))
            {
                String itemName = item.getItemMeta().getDisplayName();

                if (itemName.contains(ChatColor.DARK_PURPLE.toString()))
                {
                    return TIER1;
                }
                else if (itemName.contains(ChatColor.GRAY.toString()))
                {
                    return TIER2;
                }
                else if (itemName.contains(ChatColor.GOLD.toString()))
                {
                    return TIER3;
                }
                else if (itemName.contains(ChatColor.DARK_AQUA.toString()))
                {
                    return TIER4;
                }
                else if (itemName.contains(ChatColor.YELLOW.toString()))
                {
                    return TIER5;
                }
            }
        }

        return null;
    }

    public static Tier getTierFromInt(int tier)
    {
        switch (tier)
        {
            case 1:
                return TIER1;
            case 2:
                return TIER2;
            case 3:
                return TIER3;
            case 4:
                return TIER4;
            case 5:
                return TIER5;
        }

        return null;
    }

    public static Tier getTierAlias(String string)
    {
        switch (string.toLowerCase())
        {
            case "1":
            case "tier1":
                return Tier.TIER1;

            case "2":
            case "tier2":
                return Tier.TIER2;

            case "3":
            case "tier3":
                return Tier.TIER3;

            case "4":
            case "tier4":
                return Tier.TIER4;

            case "5":
            case "tier5":
                return Tier.TIER5;
        }

        return null;
    }

    public DyeColor getDyeColour()
    {
        return dyeColour;
    }
}
