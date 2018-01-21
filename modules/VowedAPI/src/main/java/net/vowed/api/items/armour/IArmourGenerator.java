package net.vowed.api.items.armour;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by JPaul on 2017-02-13.
 */
public interface IArmourGenerator
{
    ItemStack createArmour(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable ArmourType armourTypeOverride);

    List<ItemStack> createEquipment(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride);
}
