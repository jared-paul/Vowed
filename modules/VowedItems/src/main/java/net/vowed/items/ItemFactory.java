package net.vowed.items;

import net.vowed.api.items.IItemFactory;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.armour.ArmourType;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.items.armour.generator.ArmourGenerator;
import net.vowed.items.shield.generator.ShieldGenerator;
import net.vowed.items.weapon.generator.WeaponGenerator;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by JPaul on 2017-02-13.
 */
public class ItemFactory implements IItemFactory
{
    @Override
    public ItemStack createWeapon(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable WeaponType weaponTypeOverride)
    {
        return new WeaponGenerator().createWeapon(rarityOverride, tierOverride, weaponTypeOverride);
    }

    @Override
    public ItemStack createArmour(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable ArmourType armourTypeOverride)
    {
        return new ArmourGenerator().createArmour(rarityOverride, tierOverride, armourTypeOverride);
    }

    @Override
    public List<ItemStack> createEquipment(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride)
    {
        return new ArmourGenerator().createEquipment(rarityOverride, tierOverride);
    }

    @Override
    public ItemStack createShield(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride)
    {
        return new ShieldGenerator().createShield(rarityOverride, tierOverride);
    }
}
