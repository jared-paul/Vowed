package net.vowed.core.items.generator;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * all thanks to vilsol
 */
public abstract class Modifier<T> implements Comparable<Modifier>
{
    List<ModifierCondition> modifierConditions = new ArrayList<>();
    List<T> possibleApplicants;
    int chance = 0;
    String prefix;
    String suffix;
    int orderPriority = 10;
    boolean includeOnReRoll = true;

    public Modifier(List<T> possibleApplicants, int chance, String prefix, String suffix)
    {
        this.possibleApplicants = possibleApplicants;
        this.chance = chance;
        this.prefix = prefix;
        this.suffix = suffix;
        Generator.typeModifiers.add(this);
        Generator.typeModifierObjects.put(this.getClass(), this);
    }

    public void setOrderPriority(int orderPriority)
    {
        this.orderPriority = orderPriority;
    }

    public int getOrderPriority()
    {
        return orderPriority;
    }

    @Override
    public int compareTo(Modifier other)
    {
        return other.getOrderPriority() - orderPriority;
    }

    public boolean canApply(T t)
    {
        return possibleApplicants != null && possibleApplicants.contains(t);
    }

    protected void addCondition(ModifierCondition modifierCondition)
    {
        modifierConditions.add(modifierCondition);
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public int getChance()
    {
        return chance;
    }

    public void setIncludeOnReRoll(boolean includeOnReRoll)
    {
        this.includeOnReRoll = includeOnReRoll;
    }

    public boolean isIncludeOnReRoll()
    {
        return includeOnReRoll;
    }

    public ModifierCondition tryModifier(Tier tier, Rarity rarity)
    {
        for (ModifierCondition modifierCondition : modifierConditions)
        {
            if (modifierCondition.doesConclude(tier, rarity))
            {
                String prefix = getPrefix();
                String suffix = getSuffix();

                if (modifierCondition.getReplacement() != null && modifierCondition.getReplacement().size() > 0)
                {
                    //TODO new ItemModifier
                    /*
                    prefix = ;
                    suffix = getSuffix();
                    */
                }

                modifierCondition.setPrefix(prefix);
                modifierCondition.setSuffix(suffix);

                return modifierCondition;
            }
        }

        return null;
    }


    public ItemMeta applyModifier(ModifierCondition modifierCondition, ItemMeta meta)
    {
        String random = modifierCondition.getModifierRange().generateRandom();

        if (modifierCondition.getPrefix().toLowerCase().contains("tier"))
        {
            Tier tier = Tier.getTierFromInt(Integer.parseInt(random));

            random = tier.getChatColour() + ChatColor.BOLD.toString() + random;
        }

        if (modifierCondition.getPrefix() != null)
        {
            if (modifierCondition.getSuffix() != null)
            {
                random = modifierCondition.getPrefix() + random + modifierCondition.getSuffix();
            }
            else
            {
                random = modifierCondition.getPrefix() + random + "";
            }
        }
        else
        {
            if (modifierCondition.getSuffix() != null)
            {
                random = "" + random + modifierCondition.getSuffix();
            }
            else
            {
                random = "" + random + "";
            }
        }

        List<String> lore = meta.getLore();

        if (ChatColor.stripColor(random).toLowerCase().contains("tier"))
        {
            lore.add("");
        }

        lore.add(random);
        meta.setLore(lore);

        return meta;
    }
}
