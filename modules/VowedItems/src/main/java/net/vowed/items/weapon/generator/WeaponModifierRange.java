package net.vowed.items.weapon.generator;

import net.vowed.core.items.config.AbstractStatStorage;
import net.vowed.core.items.config.TripleStatStorage;
import net.vowed.core.items.generator.ModifierRange;
import net.vowed.core.items.generator.ModifierType;

/**
 * Created by JPaul on 6/17/2016.
 */
public class WeaponModifierRange extends ModifierRange
{
    public WeaponModifierRange(ModifierType modifierType, AbstractStatStorage damageStorage)
    {
        super(modifierType, damageStorage.statMIN, damageStorage.statMAX);

        if (damageStorage instanceof TripleStatStorage)
        {
            this.lowHigh = ((TripleStatStorage) damageStorage).statLowHigh;
        }
    }
}
