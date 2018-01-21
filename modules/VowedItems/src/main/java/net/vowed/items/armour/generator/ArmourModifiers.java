package net.vowed.items.armour.generator;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.armour.ArmourType;
import net.vowed.core.items.generator.Modifier;
import net.vowed.core.items.generator.ModifierCondition;
import net.vowed.core.items.generator.ModifierRange;
import net.vowed.core.items.generator.ModifierType;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by JPaul on 2/5/2016.
 */
public class ArmourModifiers
{
    private List<ArmourType> armourTypes = Arrays.asList(ArmourType.HELMET, ArmourType.CHESTPLATE, ArmourType.LEGGINGS, ArmourType.BOOTS);
    private String colourPrefix = ChatColor.YELLOW + ChatColor.BOLD.toString() + "  > " + ChatColor.GOLD;
    private ChatColor colourSuffix = ChatColor.WHITE;

    public class HPRegen extends Modifier
    {
        public HPRegen()
        {
            super(armourTypes, 100, colourPrefix + "HP REGEN" + ChatColor.RESET + ": +", colourSuffix + " HP/s");
            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 20, 35)).addCantHave(EnergyRegen.class));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 45, 60)).addCantHave(EnergyRegen.class));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 65, 80)).addCantHave(EnergyRegen.class));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 110, 130)).addCantHave(EnergyRegen.class));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 250, 300)).addCantHave(EnergyRegen.class));

            setOrderPriority(5);
        }
    }

    public class CoreArmour extends Modifier
    {
        public CoreArmour()
        {
            super(Arrays.asList(ArmourType.CHESTPLATE, ArmourType.LEGGINGS), 100, colourPrefix + "ARMOUR" + ChatColor.RESET + ": ", colourSuffix + "%");

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 1, 1)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 1, 3)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 1, 3)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.TRIPLE, 1, 3)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 1, 3)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 3, 5)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 4, 5)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 5, 6)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 5, 7)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 6, 8)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 7, 8)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 7, 9)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 8, 9)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 8, 9)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 9, 9)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 9, 10)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 10, 10)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 10, 11)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 11, 12)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 11, 14)));

            setOrderPriority(2);
        }
    }

    public class OtherArmour extends Modifier
    {
        public OtherArmour()
        {
            super(Arrays.asList(ArmourType.HELMET, ArmourType.BOOTS), 100, colourPrefix + "ARMOUR" + ChatColor.RESET + ": ", colourSuffix + "%");

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 1, 2)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 1, 2)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 1, 2)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 1, 2)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 1, 2)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 2, 3)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 3, 4)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 3, 4)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 3, 4)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 3, 5)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 4, 5)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 4, 6)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 4, 5)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 5, 6)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 6, 7)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 6, 7)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 6, 7)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 7, 8)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 8, 9)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 9, 10)));

            setOrderPriority(2);
        }
    }

    public class ChestplateHP extends Modifier
    {
        public ChestplateHP()
        {
            super(Collections.singletonList(ArmourType.CHESTPLATE), 100, colourPrefix + "HP" + ChatColor.RESET + ": +", null);

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 10, 90)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 90, 150)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 150, 180)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 180, 240)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 300, 420)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 420, 500)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 500, 560)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 560, 580)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 700, 900)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 900, 1100)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 1100, 1250)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 1250, 1350)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 1800, 2600)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 2600, 3200)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 3200, 3600)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 3600, 3900)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 4200, 5800)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 5800, 6900)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 6900, 7700)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 7700, 8100)));

            setOrderPriority(3);
        }
    }

    public class LeggingsHP extends Modifier
    {
        public LeggingsHP()
        {
            super(Collections.singletonList(ArmourType.LEGGINGS), 100, colourPrefix + "HP" + ChatColor.RESET + ": +", null);

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 10, 90)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 90, 150)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 150, 180)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 180, 240)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 300, 420)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 420, 500)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 500, 560)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 560, 580)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 700, 900)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 900, 1100)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 1100, 1250)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 1250, 1350)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 2000, 2900)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 2900, 3500)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 3500, 4000)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 4000, 4300)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 4400, 6000)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 6000, 7120)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 7120, 7920)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 7920, 8480)));

            setOrderPriority(3);
        }

    }

    public class OtherHP extends Modifier
    {
        public OtherHP()
        {
            super(Arrays.asList(ArmourType.HELMET, ArmourType.BOOTS), 100, colourPrefix + "HP" + ChatColor.RESET + ": +", null);

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 6, 54)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 54, 90)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 90, 108)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 108, 120)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 180, 252)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 252, 300)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 300, 336)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 336, 348)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 420, 540)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 540, 660)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 660, 950)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 750, 810)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 1200, 1740)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 1740, 2100)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 2100, 2400)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 2400, 2580)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 3300, 4500)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 4500, 5340)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 5340, 5940)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 5940, 6360)));

            setOrderPriority(3);
        }
    }

    public class TierDisplay extends Modifier
    {
        public TierDisplay()
        {
            super(armourTypes, 100, ChatColor.YELLOW + "Tier" + ChatColor.RESET + ": ", null);
            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 1)));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 2, 2)));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 3, 3)));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 4, 4)));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 5, 5)));

            setOrderPriority(20);
        }
    }

    public class EnergyRegen extends Modifier
    {
        public EnergyRegen()
        {
            super(armourTypes, 50, colourPrefix + "ENERGY REGEN" + ChatColor.RESET + ": +", colourSuffix + "%");
            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 5, true)));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 3, 7, true)));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 5, 9, true)));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 7, 12, true)));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 7, 12, true)));

            setOrderPriority(4);
        }

    }

    public class Dodge extends Modifier
    {
        public Dodge()
        {
            super(armourTypes, -1, colourPrefix + "DODGE" + ChatColor.RESET + ": ", colourSuffix + "%");
            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 5), 5));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 1, 8), 9));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 1, 10), 15));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 1, 12), 25));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 1, 12), 30));
        }
    }

    public class Block extends Modifier
    {
        public Block()
        {
            super(armourTypes, -1, colourPrefix + "BLOCK" + ChatColor.RESET + ": ", colourSuffix + "%");
            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 5), 5));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 1, 8), 9));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 1, 10), 15));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 1, 12), 25));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 1, 12), 30));
        }
    }

    public class Speed extends Modifier
    {
        public Speed()
        {
            super(Collections.singletonList(ArmourType.BOOTS), -1, colourPrefix + "SPEED" + ChatColor.RESET + ": ", colourSuffix + "%");
            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 3), 5));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 1, 6), 9));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 1, 8), 15));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 1, 10), 25));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 1, 10), 30));
        }
    }

    public void loadModifiers()
    {
        new HPRegen();
        new CoreArmour();
        new OtherArmour();
        new ChestplateHP();
        new LeggingsHP();
        new OtherHP();
        new TierDisplay();
        new EnergyRegen();
        new Dodge();
        new Block();
        new Speed();
    }
}
