package net.vowed.items.weapon.generator;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.core.items.generator.Modifier;
import net.vowed.core.items.generator.ModifierCondition;
import net.vowed.core.items.generator.ModifierRange;
import net.vowed.core.items.generator.ModifierType;
import net.vowed.core.items.util.weapon.WeaponStat;
import net.vowed.items.Settings;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by JPaul on 2/6/2016.
 */
public class WeaponModifiers
{
    private List<WeaponType> weaponTypes = Arrays.asList(WeaponType.AXE, WeaponType.BOW, WeaponType.STAFF, WeaponType.SWORD);

    private String colourPrefix = ChatColor.YELLOW + ChatColor.BOLD.toString() + "  > " + ChatColor.GOLD;
    private ChatColor colorSuffix = ChatColor.WHITE;

    private List<String> elements = Arrays.asList("WATER", "FIRE", "EARTH", "AIR");

    public class SwordDamage extends Modifier
    {
        public SwordDamage()
        {
            super(Collections.singletonList(WeaponType.SWORD), 100, colourPrefix + "DAMAGE" + ChatColor.RESET + ": ", null);

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_SWORD_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_SWORD_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_SWORD_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_SWORD_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_SWORD_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_SWORD_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_SWORD_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_SWORD_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_SWORD_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_SWORD_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_SWORD_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_SWORD_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_SWORD_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_SWORD_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_SWORD_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_SWORD_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_SWORD_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_SWORD_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_SWORD_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_SWORD_LEGENDARY_DAMAGE.value)));

            setOrderPriority(1);
        }
    }

    public class AxeDamage extends Modifier
    {
        public AxeDamage()
        {
            super(Collections.singletonList(WeaponType.AXE), 100, colourPrefix + "DAMAGE" + ChatColor.RESET + ": ", null);


            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_AXE_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_AXE_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_AXE_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_AXE_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_AXE_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_AXE_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_AXE_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_AXE_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_AXE_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_AXE_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_AXE_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_AXE_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_AXE_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_AXE_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_AXE_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_AXE_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_AXE_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_AXE_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_AXE_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_AXE_LEGENDARY_DAMAGE.value)));

            setOrderPriority(1);
        }
    }

    public class StaffDamage extends Modifier
    {
        public StaffDamage()
        {
            super(Collections.singletonList(WeaponType.STAFF), 100, colourPrefix + "DAMAGE" + ChatColor.RESET + ": ", null);

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_STAFF_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_STAFF_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_STAFF_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_STAFF_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_STAFF_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_STAFF_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_STAFF_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_STAFF_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_STAFF_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_STAFF_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_STAFF_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_STAFF_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_STAFF_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_STAFF_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_STAFF_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_STAFF_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_STAFF_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_STAFF_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_STAFF_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_STAFF_LEGENDARY_DAMAGE.value)));


            setOrderPriority(1);
        }
    }

    public class BowDamage extends Modifier
    {
        public BowDamage()
        {
            super(Collections.singletonList(WeaponType.BOW), 100, colourPrefix + "DAMAGE" + ChatColor.RESET + ": ", null);

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_BOW_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_BOW_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_BOW_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER1_BOW_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_BOW_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_BOW_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_BOW_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER2_BOW_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_BOW_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_BOW_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_BOW_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER3_BOW_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_BOW_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_BOW_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_BOW_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER4_BOW_LEGENDARY_DAMAGE.value)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_BOW_COMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_BOW_UNCOMMON_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_BOW_RARE_DAMAGE.value)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, Settings.WeaponSetting.TIER5_BOW_LEGENDARY_DAMAGE.value)));

            setOrderPriority(1);
        }
    }

    public class TierDisplay extends Modifier
    {
        public TierDisplay()
        {
            super(weaponTypes, 100, ChatColor.YELLOW + "Tier" + ChatColor.RESET + ": ", null);
            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 1)));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 2, 2)));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 3, 3)));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 4, 4)));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 5, 5)));

            setOrderPriority(20);
        }
    }

    public class Slow extends Modifier
    {
        public Slow()
        {
            super(Collections.singletonList(WeaponType.BOW), -1, colourPrefix + "SLOW" + ChatColor.RESET + ": ", colorSuffix + "%");

            for (Tier tier : Tier.values())
            {
                Settings.WeaponSetting weaponSetting = Settings.WeaponSetting.fromTier(tier, WeaponStat.SLOW);

                addCondition(new ModifierCondition(tier, null, new ModifierRange(ModifierType.STATIC, weaponSetting.value), weaponSetting.asChanceStorage().chance));
            }

            setOrderPriority(6);
        }
    }

    public class Blind extends Modifier
    {
        public Blind()
        {
            super(weaponTypes, -1, colourPrefix + "BLIND" + ChatColor.RESET + ": ", colorSuffix + "%");


            for (Tier tier : Tier.values())
            {
                Settings.WeaponSetting weaponSetting = Settings.WeaponSetting.fromTier(tier, WeaponStat.BLIND);

                addCondition(new ModifierCondition(tier, null, new ModifierRange(ModifierType.STATIC, weaponSetting.value), weaponSetting.asChanceStorage().chance));
            }

            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 5), 3));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 1, 7), 5));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 1, 9), 8));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 1, 9), 9));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 1, 11), 11));

            setOrderPriority(8);
        }
    }

    public class Magical extends Modifier
    {
        public Magical()
        {
            super(weaponTypes, -1, null, null);

            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 4), 6));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 1, 9), 9));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 1, 15), 10));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 1, 25), 15));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 1, 55), 20));

            setOrderPriority(9);
        }

        @Override
        public String getPrefix()
        {
            String strippedElement = elements.get(new Random().nextInt(elements.size()));
            ChatColor elementColour = null;
            String suffix = ChatColor.GOLD + " DAMAGE" + ChatColor.RESET + ": +";

            String noColourString = ChatColor.stripColor(strippedElement);

            if (noColourString.toLowerCase().contains("air"))
            {
                elementColour = ChatColor.WHITE;
            }
            else if (noColourString.toLowerCase().contains("water"))
            {
                elementColour = ChatColor.BLUE;
            }
            else if (noColourString.toLowerCase().contains("fire"))
            {
                elementColour = ChatColor.RED;
            }
            else if (noColourString.toLowerCase().contains("earth"))
            {
                elementColour = ChatColor.GREEN;
            }

            String element = colourPrefix + elementColour + strippedElement;

            return element + suffix;
        }
    }

    public class TrueDamage extends Modifier
    {
        public TrueDamage()
        {
            super(weaponTypes, -1, colourPrefix + "TRUE" + ChatColor.RESET + ": ", colorSuffix + "%");

            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 8, 20), 6));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 5, 15), 9));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 5, 12), 5));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 6, 10), 5));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 5, 11), 10));

            setOrderPriority(10);
        }
    }

    public void loadModifiers()
    {
        new SwordDamage();
        new AxeDamage();
        new StaffDamage();
        new BowDamage();
        new TierDisplay();
        new Slow();
        new Blind();
        new Magical();
        new TrueDamage();
    }
}
