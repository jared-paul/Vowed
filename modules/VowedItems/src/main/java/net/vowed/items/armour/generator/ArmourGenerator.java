package net.vowed.items.armour.generator;

import com.google.common.collect.Lists;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.armour.ArmourType;
import net.vowed.api.items.armour.IArmourGenerator;
import net.vowed.core.items.generator.Generator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 3/31/2016.
 */
public class ArmourGenerator extends Generator<ItemStack> implements IArmourGenerator
{
    private ArmourType armourType;

    public ArmourGenerator()
    {
        super(true, true, true);
    }

    public void setArmourType(ArmourType armourType)
    {
        this.armourType = armourType;
    }

    @Override
    public Enum getType()
    {
        return armourType;
    }

    @Override
    public String getName()
    {
        return getTier().getChatColour() + armourType.getTierName(getTier());
    }

    @Override
    public Material getMaterial()
    {
        return ArmourType.getMatFromType(armourType);
    }

    @Override
    public ItemStack generateObject(@Nullable Rarity rarity, @Nullable Tier tier)
    {
        return generateItem(rarity, tier);
    }

    @Override
    public ItemStack createArmour(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride, @Nullable ArmourType armourTypeOverride)
    {
        if (armourTypeOverride != null)
        {
            this.armourType = armourTypeOverride;
        }
        else
        {
            this.armourType = ArmourType.values()[ThreadLocalRandom.current().nextInt(ArmourType.values().length)];
        }

        return generateObject(rarityOverride, tierOverride);
    }

    @Override
    public List<ItemStack> createEquipment(@Nullable Rarity rarity, @Nullable Tier tier)
    {
        ItemStack boots = createArmour(rarity, tier, ArmourType.BOOTS);
        ItemStack leggings = createArmour(rarity, tier, ArmourType.LEGGINGS);
        ItemStack chestplate = createArmour(rarity, tier, ArmourType.CHESTPLATE);
        ItemStack helmet = createArmour(rarity, tier, ArmourType.HELMET);

        return Lists.newArrayList(boots, leggings, chestplate, helmet);
    }
}
