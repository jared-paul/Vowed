package net.vowed.core.items.generator;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * All thanks to vilsol
 */
public class ModifierCondition
{
    private Rarity rarity;
    private Tier tier;
    private ModifierRange modifierRange;
    private int chance = -1;
    private List<Class<? extends Modifier>> cantHave;
    private ModifierCondition bonus;
    private List<Class<? extends Modifier>> replacement;
    private String prefix;
    private String suffix;

    public ModifierCondition(Tier tier, Rarity rarity, ModifierRange modifierRange, int chance)
    {
        this.tier = tier;
        this.rarity = rarity;
        this.modifierRange = modifierRange;
        this.chance = chance;
        this.cantHave = new ArrayList<>();
    }

    public ModifierCondition(Tier tier, Rarity rarity, ModifierRange modifierRange)
    {
        this.tier = tier;
        this.rarity = rarity;
        this.modifierRange = modifierRange;
        this.cantHave = new ArrayList<>();
    }

    public ModifierCondition(Rarity rarity, ModifierRange modifierRange, int chance)
    {
        this.rarity = rarity;
        this.modifierRange = modifierRange;
        this.chance = chance;
        this.cantHave = new ArrayList<>();
    }

    public ModifierCondition(Rarity rarity, ModifierRange modifierRange)
    {
        this.rarity = rarity;
        this.modifierRange = modifierRange;
        this.cantHave = new ArrayList<>();
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }

    public String getSuffix()
    {
        return suffix;
    }

    public void setBonus(ModifierCondition bonus)
    {
        this.bonus = bonus;
    }

    public ModifierCondition getBonus()
    {
        return bonus;
    }

    public int getChance()
    {
        return chance;
    }

    public ModifierRange getModifierRange()
    {
        return modifierRange;
    }

    public ModifierCondition setReplacement(Class<? extends Modifier> replacement)
    {
        this.replacement = new ArrayList<>();
        this.replacement.add(replacement);
        return this;
    }

    public ModifierCondition setReplacement(List<Class<? extends Modifier>> replacement)
    {
        this.replacement = replacement;
        return this;
    }

    public List<Class<? extends Modifier>> getReplacement()
    {
        return replacement;
    }

    public ModifierCondition addCantHave(Class<? extends Modifier> cantHave)
    {
        this.cantHave.add(cantHave);
        return this;
    }

    public ModifierCondition setCantHave(List<Class<? extends Modifier>> cantHave)
    {
        this.cantHave = cantHave;
        return this;
    }

    public List<Class<? extends Modifier>> getCantHave()
    {
        return cantHave;
    }

    public boolean doesConclude(Tier tier, Rarity rarity)
    {
        if (tier == null)
        {
            return !(this.rarity != null && this.rarity != rarity);
        }
        else if (rarity == null)
        {
            return !(this.tier != null && this.tier != tier);
        }

        return !(this.tier != null && this.tier != tier) && !(this.rarity != null && this.rarity != rarity);
    }

    public boolean canApply(Set<ModifierCondition> conditions)
    {
        for (ModifierCondition modifierCondition : conditions)
        {
            if (modifierCondition.equals(this)) continue;

            if (modifierCondition.getPrefix() != null)
            {
                if (modifierCondition.getPrefix().equals(this.getPrefix()))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean canHave(Class<? extends Modifier> modifier)
    {
        return !this.cantHave.contains(modifier);
    }
}
