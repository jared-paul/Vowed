package net.vowed.items.shield.generator;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.shield.ShieldType;
import net.vowed.core.items.generator.Modifier;
import net.vowed.core.items.generator.ModifierCondition;
import net.vowed.core.items.generator.ModifierRange;
import net.vowed.core.items.generator.ModifierType;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by JPaul on 3/3/2016.
 */
public class ShieldModifiers
{
    List<ShieldType> shieldType = Collections.singletonList(ShieldType.SHIELD);

    private String colourPrefix = ChatColor.YELLOW + ChatColor.BOLD.toString() + "  > " + ChatColor.GOLD;
    private ChatColor colorSuffix = ChatColor.WHITE;

    private List<String> elements = Arrays.asList("WATER", "FIRE", "EARTH", "AIR");

    public class TierDisplay extends Modifier
    {
        public TierDisplay()
        {
            super(shieldType, 100, ChatColor.YELLOW + "Tier" + ChatColor.RESET + ": ", null);
            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 1)));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 2, 2)));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 3, 3)));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 4, 4)));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 5, 5)));

            setOrderPriority(20);
        }
    }

    public class Defense extends Modifier
    {
        public Defense()
        {
            super(shieldType, 100, colourPrefix + "DEFENSE" + ChatColor.RESET + ": ", colorSuffix + "%");

            addCondition(new ModifierCondition(Tier.TIER1, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 1, 2)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 1, 2)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 1, 2)));
            addCondition(new ModifierCondition(Tier.TIER1, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 1, 2)));

            addCondition(new ModifierCondition(Tier.TIER2, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 1, 2)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 2, 3)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 3, 4)));
            addCondition(new ModifierCondition(Tier.TIER2, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 3, 4)));

            addCondition(new ModifierCondition(Tier.TIER3, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 2, 3)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 3, 5)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 4, 5)));
            addCondition(new ModifierCondition(Tier.TIER3, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 5, 6)));

            addCondition(new ModifierCondition(Tier.TIER4, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 4, 5)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 5, 6)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 6, 7)));
            addCondition(new ModifierCondition(Tier.TIER4, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 7, 7)));

            addCondition(new ModifierCondition(Tier.TIER5, Rarity.COMMON, new ModifierRange(ModifierType.STATIC, 7, 7)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 8, 9)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.RARE, new ModifierRange(ModifierType.STATIC, 10, 11)));
            addCondition(new ModifierCondition(Tier.TIER5, Rarity.LEGENDARY, new ModifierRange(ModifierType.STATIC, 12, 13)));

            setOrderPriority(1);
        }
    }

    public class Magical extends Modifier
    {
        public Magical()
        {
            super(shieldType, -1, null, null);

            addCondition(new ModifierCondition(Tier.TIER1, null, new ModifierRange(ModifierType.STATIC, 1, 4), 6));
            addCondition(new ModifierCondition(Tier.TIER2, null, new ModifierRange(ModifierType.STATIC, 1, 9), 9));
            addCondition(new ModifierCondition(Tier.TIER3, null, new ModifierRange(ModifierType.STATIC, 1, 15), 10));
            addCondition(new ModifierCondition(Tier.TIER4, null, new ModifierRange(ModifierType.STATIC, 1, 25), 15));
            addCondition(new ModifierCondition(Tier.TIER5, null, new ModifierRange(ModifierType.STATIC, 1, 55), 20));

            setOrderPriority(2);
        }

        @Override
        public String getPrefix()
        {
            String strippedElement = elements.get(new Random().nextInt(elements.size()));
            ChatColor elementColour = null;
            String suffix = ChatColor.GOLD + " DEFENSE" + ChatColor.RESET + ": +";

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

        @Override
        public String getSuffix()
        {
            return colorSuffix + "%";
        }
    }

    public void loadModifiers()
    {
        new TierDisplay();
        new Defense();
        new Magical();
    }
}
