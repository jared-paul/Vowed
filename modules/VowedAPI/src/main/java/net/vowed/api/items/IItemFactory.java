package net.vowed.api.items;

import net.vowed.api.items.armour.ArmourType;
import net.vowed.api.items.weapon.WeaponType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by JPaul on 2017-02-13.
 */
public interface IItemFactory
{
    ItemStack createWeapon(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable WeaponType weaponTypeOverride);

    ItemStack createArmour(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable ArmourType armourTypeOverride);

    List<ItemStack> createEquipment(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride);

    ItemStack createShield(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride);
}
