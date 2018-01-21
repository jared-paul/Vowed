package net.vowed.core.items.generator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.core.items.util.ItemUtil;
import net.vowed.core.storage.AttributeStorage;
import net.vowed.core.util.strings.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 3/30/2016.
 */
public abstract class Generator<T>
{
    public static List<Modifier> typeModifiers = Lists.newArrayList();
    public static Map<Class<? extends Modifier>, Modifier> typeModifierObjects = Maps.newHashMap();

    private Rarity rarity;
    private Tier tier;
    private boolean reRoll;
    private boolean useRarity;
    private boolean useTier;
    private boolean useDurability;

    private ItemStack item;
    private ItemStack original;

    private List<String> excessData = Lists.newArrayList();
    private List<ModifierCondition> modifiers = Lists.newArrayList();

    public Generator(boolean useRarity, boolean useTier, boolean useDurability)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        this.useDurability = useDurability;

        //generates a random rarity which can be overridden if you CHOOSE to in generateItem(), hence @Nullable
        if (useRarity)
        {
            this.useRarity = true;

            int chance = random.nextInt(100);

            if (rarity == null)
            {
                if (chance > 98)
                {
                    rarity = Rarity.LEGENDARY;
                }
                else if (chance > 94)
                {
                    rarity = Rarity.RARE;
                }
                else if (chance > 82)
                {
                    rarity = Rarity.UNCOMMON;
                }
                else
                {
                    rarity = Rarity.COMMON;
                }
            }
        }
        else
        {
            this.useRarity = false;
        }

        if (useTier)
        {
            this.useTier = true;

            tier = Tier.values()[random.nextInt(Tier.values().length)];
        }
        else
        {
            this.useTier = false;
        }
    }

    public abstract Enum getType();

    public abstract String getName();

    public abstract Material getMaterial();

    public abstract T generateObject(@Nullable Rarity rarity, @Nullable Tier tier);

    public void setRarity(Rarity rarity)
    {
        this.rarity = rarity;
    }

    public Rarity getRarity()
    {
        return rarity;
    }

    public void setTier(Tier tier)
    {
        this.tier = tier;
    }

    public Tier getTier()
    {
        return tier;
    }

    public void setReRoll(boolean reRoll)
    {
        this.reRoll = reRoll;
    }

    public void setOriginal(ItemStack original)
    {
        this.original = original;
    }

    public ItemStack getItem()
    {
        if (item == null)
        {
            item = generateItem(rarity, tier);
        }

        return item;
    }

    public void setItem(ItemStack item)
    {
        this.item = item;
    }

    public List<String> getExcessData()
    {
        return excessData;
    }

    public List<ModifierCondition> getModifiers()
    {
        return modifiers;
    }

    /*
                ex:
                    if useRarity = false, do generateItem(null, etc)
                    if useTier = false, do generateItem(@Nullable rarity, null, etc)
                 */
    public ItemStack    generateItem(@Nullable Rarity rarityOverride, @Nullable Tier tierOverride)
    {
        if (reRoll && original != null)
        {
            rarity = Rarity.getRarityFromItem(original);
        }

        if (useRarity)
        {
            if (rarityOverride != null)
            {
                rarity = rarityOverride;
            }
        }
        if (useTier)
        {
            if (tierOverride != null)
            {
                tier = tierOverride;
            }
        }

        Random random = new Random();

        ItemStack item;

        if (reRoll && original != null)
        {
            item = original;
        }
        else
        {
            item = new ItemStack(getMaterial());
        }

        ItemMeta meta = item.getItemMeta().clone();
        meta.setDisplayName(getName());
        item.setItemMeta(meta);

        if (!reRoll)
        {
            meta.setLore(Collections.singletonList(""));
        }
        else
        {
            meta.setLore(meta.getLore().subList(0, 4));
        }

        if (ItemUtil.ArmourUtil.isLeatherArmour(item))
        {
            if (useTier)
            {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
                leatherArmorMeta.setColor(tier.getColour());
                item.setItemMeta(leatherArmorMeta);
            }
        }

        HashMap<ModifierCondition, Modifier> conditions = new HashMap<>();

        Collections.shuffle(typeModifiers);

        for (Modifier modifier : typeModifiers)
        {
            if (modifier.canApply(getType()))
            {
                if (reRoll && !modifier.isIncludeOnReRoll())
                {
                    continue;
                }

                ModifierCondition modifierCondition = modifier.tryModifier(tier, rarity);

                if (modifierCondition != null)
                {
                    conditions.put(modifierCondition, modifier);

                    ModifierCondition bonus = modifierCondition.getBonus();

                    while (bonus != null)
                    {
                        String prefix = modifier.getPrefix();
                        String suffix = modifier.getSuffix();

                        if (bonus.getReplacement() != null && bonus.getReplacement().size() > 0)
                        {
                            Modifier replacement = typeModifierObjects.get(bonus.getReplacement().get(new Random().nextInt(bonus.getReplacement().size())));
                            prefix = replacement.getPrefix();
                            suffix = replacement.getSuffix();
                        }

                        bonus.setPrefix(prefix);
                        bonus.setSuffix(suffix);

                        conditions.put(bonus, modifier);

                        bonus = bonus.getBonus();
                    }
                }
            }
        }

        List<ModifierCondition> order = new ArrayList<>();

        for (Object object : Arrays.asList(conditions.keySet().toArray()))
        {
            ModifierCondition modifierCondition = (ModifierCondition) object;

            if (!modifierCondition.canApply(conditions.keySet()))
            {
                conditions.remove(modifierCondition);
            }
            else
            {
                Modifier modifier = conditions.get(modifierCondition);

                int belowChance = (modifierCondition.getChance() < 0) ? modifier.getChance() : modifierCondition.getChance();

                if (random.nextInt(100) < belowChance)
                {
                    order.add(modifierCondition);
                }
                else
                {
                    conditions.remove(modifierCondition);
                }
            }
        }

        for (Modifier modifier : conditions.values())
        {
            for (ModifierCondition modifierCondition : (List<ModifierCondition>) ((ArrayList<ModifierCondition>) order).clone())
            {
                if (!modifierCondition.canHave(modifier.getClass()))
                {
                    order.remove(modifierCondition);
                }
            }
        }

        Collections.sort(order, (modifierCondition1, modifierCondition2) -> conditions.get(modifierCondition1).getOrderPriority() - conditions.get(modifierCondition2).getOrderPriority());

        String modifierName;


        String[] bonuses = new String[24];
        Arrays.fill(bonuses, "");

        if (reRoll && original != null && original.hasItemMeta() && original.getItemMeta().hasLore() && ItemUtil.ArmourUtil.isLeatherArmour(original))
        {
            for (String line : original.getItemMeta().getLore())
            {
                if (!line.contains(":"))
                {
                    continue;
                }

                if (ChatColor.stripColor(line.substring(0, line.indexOf(":"))).equals("ENERGY REGEN"))
                {
                    bonuses[11] = "ENERGY REGEN";
                }
                else if (ChatColor.stripColor(line.substring(0, line.indexOf(":"))).equals("HP REGEN"))
                {
                    bonuses[2] = "HP REGEN";
                }
            }
        }

        for (ModifierCondition modifierCondition : order)
        {
            Modifier itemModifier = conditions.get(modifierCondition);
            meta = itemModifier.applyModifier(modifierCondition, meta);
            modifierName = ChatColor.stripColor(modifierCondition.getPrefix().substring(0, modifierCondition.getPrefix().indexOf(":")));

            switch (modifierName)
            {
                case "HP REGEN":
                    bonuses[2] = "HP REGEN";
                    break;
                case "ENERGY REGEN":
                    bonuses[11] = "ENERGY REGEN";
                    break;
                default:
                    break;
            }
        }

        if (useRarity)
        {
            List<String> lore = meta.getLore();

            if (!useTier)
            {
                lore.add("");
            }

            lore.add(ChatColor.YELLOW + "Rarity" + ChatColor.RESET + ": " + rarity.getChatColour() + Strings.capitalizeFirst(rarity.name().toLowerCase()));
            meta.setLore(lore);
        }

        List<String> lore = meta.getLore();

        if (useDurability)
        {
            int durability = getDurability(item) - 1;
            int maxDurability = getDurability(item);

            lore.add(ChatColor.YELLOW + "Durability" + ChatColor.RESET + ": " + ChatColor.GREEN + durability + ChatColor.RESET + " / " + maxDurability);
            meta.setLore(lore);
        }

        for (ModifierCondition modifierCondition : order)
        {
            excessData.add(modifierCondition.getModifierRange().getRandom());
            modifiers.add(modifierCondition);
        }

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);

        AttributeStorage storage = AttributeStorage.newTarget(item, AttributeStorage.storageUUID);
        storage.setData(UUID.randomUUID().toString());
        this.item = storage.getTarget();

        return storage.getTarget();
    }

    public int getDurability(ItemStack item)
    {
        Tier tier = Tier.getTierFromItem(item);

        if (tier != null)
        {
            if (ItemUtil.ArmourUtil.isLeatherArmour(item))
            {
                return tier.getArmourMAXDurability();
            }
            else if (ItemUtil.WeaponUtil.isWeapon(item))
            {
                return tier.getWeaponMAXDurability();
            }
        }

        return 0;
    }
}
