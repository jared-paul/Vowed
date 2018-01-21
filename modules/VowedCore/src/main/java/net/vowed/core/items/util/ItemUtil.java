package net.vowed.core.items.util;

import com.google.common.collect.Maps;
import net.vowed.core.items.util.armour.ArmourStat;
import net.vowed.core.items.util.armour.ArmourStats;
import net.vowed.core.items.util.weapon.WeaponStat;
import net.vowed.core.items.util.weapon.WeaponStats;
import net.vowed.core.util.strings.Strings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by JPaul on 2017-02-03.
 */
public class ItemUtil
{
    /**
     * Created by JPaul on 1/26/2016.
     */
    public static class ArmourUtil
    {
        public static boolean isLeatherArmour(ItemStack item)
        {
            return item.getItemMeta() instanceof LeatherArmorMeta;
        }

        public static boolean isArmour(Material material)
        {
            return (material.name().endsWith("HELMET") && material.name().endsWith("HELMET")) ||
                    (material.name().endsWith("CHESTPLATE") && material.name().endsWith("CHESTPLATE")) ||
                    (material.name().endsWith("LEGGINGS") && material.name().endsWith("LEGGINGS")) ||
                    (material.name().endsWith("BOOTS") && material.name().endsWith("BOOTS")) ||
                    (material.name().endsWith("HELMET") && material.name().equals("PUMPKIN")) ||
                    (material.name().equals("PUMPKIN") && material.name().endsWith("HELMET")) ||
                    (material.name().equals("PUMPKIN") && material.name().equals("PUMPKIN"));
        }

        public static ArmourStats getArmourStats(ItemStack armour)
        {
            int HP = ItemUtil.ArmourUtil.getHP(armour);
            int hpRegen = ItemUtil.ArmourUtil.getHPRegen(armour);
            int energyRegen = ItemUtil.ArmourUtil.getENERGYRegen(armour);
            int block = ItemUtil.ArmourUtil.getBlock(armour);
            int dodge = ItemUtil.ArmourUtil.getDodge(armour);
            int armourPercent = ItemUtil.ArmourUtil.getArmourPercent(armour);
            float speed = ItemUtil.ArmourUtil.getSpeed(armour);
            int durability = ItemUtil.ArmourUtil.getDurability(armour);

            return new ArmourStats(HP, hpRegen, energyRegen, block, dodge, armourPercent, speed, durability);
        }

        public static int getTotalHP(Player player)
        {
            int totalHP = 50;

            for (ItemStack armour : player.getInventory().getArmorContents())
            {
                totalHP += getHP(armour);
            }

            return totalHP;
        }

        public static int getHP(ItemStack armour)
        {
            try
            {
                return Integer.parseInt(getStat(ArmourStat.HP, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static void addHP(ItemStack armour, int HP)
        {
            ItemMeta meta = armour.getItemMeta();
            List<String> lore = meta.getLore();

            for (String line : lore)
            {
                if (line.toLowerCase().contains("hp") && !line.toLowerCase().contains("regen"))
                {
                    String newHP = line.replace(String.valueOf(getHP(armour)), String.valueOf(getHP(armour) + HP));
                    lore.set(lore.indexOf(line), newHP);
                }
            }

            meta.setLore(lore);
            armour.setItemMeta(meta);
        }

        public static int getHPRegen(ItemStack armour)
        {
            try
            {
                return Integer.parseInt(getStat(ArmourStat.HPREGEN, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getENERGYRegen(ItemStack armour)
        {
            try
            {
                return Integer.parseInt(getStat(ArmourStat.ENERGYREGEN, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getBlock(ItemStack armour)
        {
            try
            {
                return Integer.parseInt(getStat(ArmourStat.BLOCK, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getDodge(ItemStack armour)
        {
            try
            {
                return Integer.parseInt(getStat(ArmourStat.DODGE, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getArmourPercent(ItemStack armour)
        {
            try
            {
                return Integer.parseInt(getStat(ArmourStat.ARMOUR, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getDurability(ItemStack armour)
        {
            try
            {
                return Integer.parseInt(getStat(ArmourStat.DURABILITY, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getMaxDurability(ItemStack armour)
        {
            try
            {
                return Integer.parseInt(getStat(ArmourStat.MAXDURABILITY, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static float getSpeed(ItemStack armour)
        {
            try
            {
                return Float.parseFloat(getStat(ArmourStat.SPEED, armour));
            } catch (Exception e)
            {
                return 0;
            }
        }

        private static String getStat(ArmourStat armourStat, ItemStack weapon)
        {
            if (weapon != null && weapon.hasItemMeta() && weapon.getItemMeta().getLore() != null)
            {
                List<String> lore = weapon.getItemMeta().getLore();

                for (String line : lore)
                {
                    String lowerCase = line.toLowerCase();

                    switch (armourStat)
                    {
                        case HP:
                            if (lowerCase.contains("hp") && !lowerCase.contains("regen"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case HPREGEN:
                            if (lowerCase.contains("hpregen"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case ENERGYREGEN:
                            if (lowerCase.contains("energyregen"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case BLOCK:
                            if (lowerCase.contains("block"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case DODGE:
                            if (lowerCase.contains("dodge"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case ARMOUR:
                            if (lowerCase.contains("armour"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case SPEED:
                            if (lowerCase.contains("speed"))
                            {
                                return String.valueOf(getFloat(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case DURABILITY:
                            if (lowerCase.contains("durability"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"), lowerCase.indexOf("/"))));
                            }
                            break;
                        case MAXDURABILITY:
                            if (lowerCase.contains("durability"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf("/"))));
                            }
                            break;
                    }
                }
            }

            return null;
        }
    }

    /**
     * Created by JPaul on 2/6/2016.
     */
    public static class WeaponUtil
    {
        public static boolean isWeapon(ItemStack weapon)
        {
            return isWeapon(weapon.getType());
        }

        public static boolean isWeapon(Material material)
        {
            if (material.name().contains("SWORD"))
            {
                return true;
            }
            else if (material.name().contains("HOE"))
            {
                return true;
            }
            else if (material.name().contains("AXE"))
            {
                return true;
            }
            else if (material.name().contains("BOW"))
            {
                return true;
            }

            return false;
        }

        public static WeaponStats getWeaponStats(ItemStack weapon)
        {
            int damageLOW = WeaponUtil.getDamageLOW(weapon);
            int damageHIGH = WeaponUtil.getDamageHIGH(weapon);
            int blind = WeaponUtil.getBlind(weapon);
            int slow = WeaponUtil.getSlow(weapon);
            int trueDamage = WeaponUtil.getTrueDamage(weapon);
            int durability = WeaponUtil.getDurability(weapon);
            Map<String, Integer> magical = WeaponUtil.getMagical(weapon);

            return new WeaponStats(damageLOW, damageHIGH, blind, slow, trueDamage, durability, magical);
        }

        public static int getDamageLOW(ItemStack weapon)
        {
            try
            {
                return Integer.parseInt(getStat(WeaponStat.DAMAGELOW, weapon));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getDamageHIGH(ItemStack weapon)
        {
            try
            {
                return Integer.parseInt(getStat(WeaponStat.DAMAGEHIGH, weapon));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getBlind(ItemStack weapon)
        {
            try
            {
                return Integer.parseInt(getStat(WeaponStat.BLIND, weapon));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getSlow(ItemStack weapon)
        {
            try
            {
                return Integer.parseInt(getStat(WeaponStat.SLOW, weapon));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getTrueDamage(ItemStack weapon)
        {
            try
            {
                return Integer.parseInt(getStat(WeaponStat.TRUEDAMAGE, weapon));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getDurability(ItemStack weapon)
        {
            try
            {
                return Integer.parseInt(getStat(WeaponStat.DURABILITY, weapon));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static int getMaxDurability(ItemStack weapon)
        {
            try
            {
                return Integer.parseInt(getStat(WeaponStat.MAXDURABILITY, weapon));
            } catch (Exception e)
            {
                return 0;
            }
        }

        public static void subtractDurability(ItemStack weapon, int durability)
        {
            ItemMeta meta = weapon.getItemMeta();
            List<String> lore = meta.getLore();

            for (String line : lore)
            {
                if (line.toLowerCase().contains("durability"))
                {
                    String newDurability = line.replace(String.valueOf(getDurability(weapon)), String.valueOf(getDurability(weapon) - durability));
                    lore.set(lore.indexOf(line), newDurability);
                }
            }

            meta.setLore(lore);
            weapon.setItemMeta(meta);
        }

        public static Map<String, Integer> getMagical(ItemStack weapon)
        {
            List<String> lore = weapon.getItemMeta().getLore();
            Map<String, Integer> magicalMap = Maps.newHashMap();

            for (String line : lore)
            {
                String prefix = Strings.stringFromList(line, Arrays.asList("FIRE", "EARTH", "AIR", "WATER"));

                if (prefix != null)
                {
                    magicalMap.put(prefix, getInt(line.substring(line.indexOf(":"))));
                }
            }

            return magicalMap;
        }

        private static String getStat(WeaponStat weaponStat, ItemStack weapon)
        {
            if (weapon != null && weapon.hasItemMeta() && weapon.getItemMeta().getLore() != null)
            {
                List<String> lore = weapon.getItemMeta().getLore();

                for (String line : lore)
                {
                    String lowerCase = line.toLowerCase();

                    switch (weaponStat)
                    {
                        case DAMAGELOW:
                            if (lowerCase.contains("damage"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"), lowerCase.indexOf("-"))));
                            }
                            break;
                        case DAMAGEHIGH:
                            if (lowerCase.contains("damage"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf("-"))));
                            }
                            break;
                        case BLIND:
                            if (lowerCase.contains("blind"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case SLOW:
                            if (lowerCase.contains("slow"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case TRUEDAMAGE:
                            if (lowerCase.contains("true"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"))));
                            }
                            break;
                        case DURABILITY:
                            if (lowerCase.contains("durability"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf(":"), lowerCase.indexOf("/"))));
                            }
                            break;
                        case MAXDURABILITY:
                            if (lowerCase.contains("durability"))
                            {
                                return String.valueOf(getInt(lowerCase.substring(lowerCase.indexOf("/"))));
                            }
                    }
                }
            }

            return null;
        }
    }

    private static int getInt(String string)
    {
        return Integer.parseInt(string.replaceAll("[\\D]", ""));
    }

    private static float getFloat(String string)
    {
        return Float.parseFloat(string.replaceAll("[\\D]", ""));
    }
}
