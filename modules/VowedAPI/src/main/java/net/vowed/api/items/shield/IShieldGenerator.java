package net.vowed.api.items.shield;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * Created by JPaul on 2017-02-13.
 */
public interface IShieldGenerator
{
    ItemStack createShield(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride);
}
