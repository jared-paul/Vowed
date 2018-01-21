package net.vowed.items.shield.generator;

import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.shield.IShieldGenerator;
import net.vowed.api.items.shield.ShieldType;
import net.vowed.core.items.generator.Generator;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 3/31/2016.
 */
public class ShieldGenerator extends Generator<ItemStack> implements IShieldGenerator
{
    List<PatternType> patterns;

    public ShieldGenerator()
    {
        //TODO integrate durability with shields
        super(true, true, false);
    }

    @Override
    public Enum getType()
    {
        return ShieldType.SHIELD;
    }

    @Override
    public String getName()
    {
        return getTier().getChatColour() + ShieldType.SHIELD.getTierName(getTier());
    }

    @Override
    public Material getMaterial()
    {
        return Material.SHIELD;
    }

    @Override
    public ItemStack generateObject(@Nullable Rarity rarity, @Nullable Tier tier)
    {
        ItemStack shieldItem = generateItem(rarity, tier);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        DyeColor dyeColor = DyeColor.BLACK;
        PatternType pattern1 = PatternType.values()[random.nextInt(PatternType.values().length)];
        PatternType pattern2 = PatternType.values()[random.nextInt(PatternType.values().length)];

        ItemMeta meta = shieldItem.getItemMeta();
        BlockStateMeta blockStateMeta = (BlockStateMeta) meta;

        Banner banner1 = (Banner) blockStateMeta.getBlockState();
        banner1.setBaseColor(tier.getDyeColour());
        banner1.addPattern(new Pattern(dyeColor, pattern1));
        banner1.addPattern(new Pattern(dyeColor, pattern2));

        banner1.update();
        blockStateMeta.setBlockState(banner1);
        shieldItem.setItemMeta(blockStateMeta);
        setItem(shieldItem);

        return shieldItem;
    }

    public void setPatterns(List<PatternType> patterns)
    {
        this.patterns = patterns;
    }

    public void addPattern(PatternType pattern)
    {
        patterns.add(pattern);
    }

    public List<PatternType> getPatterns()
    {
        return patterns;
    }

    @Override
    public ItemStack createShield(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride)
    {
        return generateObject(rarityOverride, tierOverride);
    }
}
