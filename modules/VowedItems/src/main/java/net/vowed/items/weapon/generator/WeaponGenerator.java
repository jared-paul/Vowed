package net.vowed.items.weapon.generator;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.weapon.IWeaponGenerator;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.core.items.generator.Generator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 3/31/2016.
 */
public class WeaponGenerator extends Generator<ItemStack> implements IWeaponGenerator
{
    WeaponType weaponType;

    public WeaponGenerator()
    {
        super(true, true, true);
    }

    @Override
    public Enum getType()
    {
        return weaponType;
    }

    @Override
    public String getName()
    {
        return getTier().getChatColour() + weaponType.getTierName(getTier());
    }

    @Override
    public Material getMaterial()
    {
        return WeaponType.getMatFromType(weaponType, getTier());
    }

    @Override
    public ItemStack generateObject(@Nullable Rarity rarity, @Nullable Tier tier)
    {
        return generateItem(rarity, tier);
    }

    @Override
    public ItemStack createWeapon(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable WeaponType weaponTypeOverride)
    {
        if (weaponTypeOverride != null)
        {
            this.weaponType = weaponTypeOverride;
        }
        else
        {
            this.weaponType = WeaponType.values()[ThreadLocalRandom.current().nextInt(WeaponType.values().length)];
        }

        return generateObject(rarityOverride, tierOverride);
    }
}
