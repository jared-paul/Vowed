package net.vowed.items.misc.generator;

import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.core.items.generator.Generator;
import net.vowed.items.misc.MiscType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 6/8/2016.
 */
public class MiscGenerator extends Generator<ItemStack>
{
    private MiscType miscType;

    public MiscGenerator(@Nullable MiscType miscType)
    {
        super(false, false, false);

        if (miscType == null)
        {
            miscType = MiscType.values()[ThreadLocalRandom.current().nextInt(MiscType.values().length)];
        }

        this.miscType = miscType;
    }

    @Override
    public Enum getType()
    {
        return miscType;
    }

    @Override
    public String getName()
    {
        return miscType.getName();
    }

    @Override
    public Material getMaterial()
    {
        return miscType.getMaterial();
    }

    @Override
    public ItemStack generateObject(@Nullable Rarity rarity, @Nullable Tier tier)
    {
        return new ItemBuilder(generateItem(rarity, tier)).setName(miscType.getName()).setLore(miscType.getLore()).getItem();
    }

    public static ItemStack createItem(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable MiscType miscTypeOverride)
    {
        return new MiscGenerator(miscTypeOverride).generateObject(rarityOverride, tierOverride);
    }
}
