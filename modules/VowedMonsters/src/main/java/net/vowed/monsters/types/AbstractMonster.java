package net.vowed.monsters.types;

import net.minecraft.server.v1_12_R1.*;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.Tier;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.api.mobs.monsters.IAbstractMonster;
import net.vowed.api.mobs.monsters.MonsterType;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.items.util.ItemUtil;
import net.vowed.core.mobs.AIGoalSelector;
import net.vowed.core.mobs.type.Melee;
import net.vowed.core.mobs.type.Ranged;
import net.vowed.core.util.math.Integers;
import net.vowed.core.util.reflection.ReflectionUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMonster;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 2/20/2016.
 */
public abstract class AbstractMonster extends EntityMonster implements IAbstractMonster
{
    private AIGoalSelector pathfinderSelector;
    private AIGoalSelector monsterTargetSelector;
    public int currentHealth;
    private int maxHealth;

    private Tier mobTier;

    public AbstractMonster(World world, @Nullable Tier tier)
    {
        super(world);

        pathfinderSelector = new AIGoalSelector();
        monsterTargetSelector = new AIGoalSelector();

        if (tier == null)
        {
            initRandomTier();
        }
        else
        {
            mobTier = tier;
        }

        initEquipment();
        initHealth();

        try
        {
            initDefaultPathfinders();
            initSpecificPathfinders();
        } catch (Exception e1)
        {
            e1.printStackTrace();
        }

        fireProof = true;
    }

    @Override
    public Entity getEntity()
    {
        return this;
    }

    /*
    PATHFINDING
     */

    private void initDefaultPathfinders() throws Exception
    {
        Set goalB = (Set) ReflectionUtil.getPrivateField(goalSelector, "b");
        goalB.clear();
        Set goalC = (Set) ReflectionUtil.getPrivateField(goalSelector, "c");
        goalC.clear();
        Set targetB = (Set) ReflectionUtil.getPrivateField(targetSelector, "b");
        targetB.clear();
        Set targetC = (Set) ReflectionUtil.getPrivateField(targetSelector, "c");
        targetC.clear();

        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 0.4));

        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
    }

    protected abstract void initSpecificPathfinders();

    /*
    TIER
     */

    private void initRandomTier()
    {
        mobTier = Tier.values()[ThreadLocalRandom.current().nextInt(Tier.values().length)];
    }

    public Tier getTier()
    {
        return mobTier;
    }

    /*
    DATAWATCHER
     */

    protected abstract void initDatawatcher();

    @Override
    public void i()
    {
        super.i();
        initDatawatcher();
    }

    /*
    LOCATION
     */

    public void setLocation(Location location)
    {
        setPosition(location.getBlockX(), location.getY(), location.getBlockZ());
    }

    @Override
    public int getCurrentHP()
    {
        return currentHealth;
    }

    @Override
    public void setCurrentHP(int health)
    {
        this.currentHealth = health;
    }

    @Override
    public int getMaxHP()
    {
        return maxHealth;
    }

    @Override
    public void setMaxHP(int maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    public Location getLocation()
    {
        return super.getBukkitEntity().getLocation();
    }

    /*
    RANDOM
     */

    public Random getRandom()
    {
        return this.random;
    }


    /*
    NAVIGATION
     */
    @Override
    public void n()
    {
        super.n();
        tick();
    }

    public void tick()
    {
        try
        {
            ++this.ticksFarFromPlayer;

            if (isAlive())
            {
                getEntitySenses().a();
            }

            r();
            monsterTargetSelector.tick();
            pathfinderSelector.tick();
            getNavigation().l();

            getControllerMove().c();
            getControllerLook().a();
            getControllerJump().b();

        } catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }



    /*
    SPAWNING
     */

    public abstract MonsterType getType();

    public void spawn(Location location)
    {
        Vowed.getMonsterRegistry().spawnMonsterEntity(this, location);
    }

    /*
    PREPARING MONSTER
     */
    public void initEquipment()
    {
        if (!(this instanceof MonsterCreeper))
        {
            ItemStack weapon = null;

            if (this instanceof Melee)
            {
                WeaponType meleeWeaponType = WeaponType.values()[ThreadLocalRandom.current().nextInt(WeaponType.values().length - 1)];
                weapon = Vowed.getItemFactory().createWeapon(Rarity.getRarityChance(), mobTier, meleeWeaponType);
            }
            else if (this instanceof Ranged)
            {
                weapon = Vowed.getItemFactory().createWeapon(Rarity.getRarityChance(), mobTier, WeaponType.BOW);
            }

            CraftMonster monster = (CraftMonster) getBukkitEntity();

            EntityEquipment entityEquipment = monster.getEquipment();

            assert weapon != null;
            entityEquipment.setItemInMainHand(weapon);
            entityEquipment.setItemInOffHand(Vowed.getItemFactory().createShield(Rarity.getRarityChance(), mobTier));

            List<ItemStack> equipment = Vowed.getItemFactory().createEquipment(null, mobTier);
            for (ItemStack armour : equipment)
            {
                switch (armour.getType())
                {
                    case LEATHER_BOOTS:
                        entityEquipment.setBoots(armour);
                        break;

                    case LEATHER_LEGGINGS:
                        entityEquipment.setLeggings(armour);
                        break;

                    case LEATHER_CHESTPLATE:
                        entityEquipment.setChestplate(armour);
                        break;

                    case LEATHER_HELMET:
                        entityEquipment.setHelmet(armour);
                        break;
                }
            }
        }
    }

    public void initHealth()
    {
        if (getType() == MonsterType.ZOMBIE || getType() == MonsterType.SKELETON)
        {
            EntityEquipment entityEquipment = (((LivingEntity) getBukkitEntity()).getEquipment());

            for (ItemStack armour : entityEquipment.getArmorContents())
            {
                if (armour != null)
                {
                    maxHealth += ItemUtil.ArmourUtil.getHP(armour);
                }
            }

            currentHealth = maxHealth;

            getBukkitEntity().setCustomName(ChatColor.RESET + barArray()[Integers.roundUpPositiveWithMax((((double) currentHealth / (double) maxHealth) * 20.0), 20)]);
            getBukkitEntity().setCustomNameVisible(true);
        }
        else if (getType() == MonsterType.CREEPER)
        {
            Tier mobTier = Tier.values()[ThreadLocalRandom.current().nextInt(Tier.values().length)];

            int maxHealth = 0;

            ThreadLocalRandom random = ThreadLocalRandom.current();

            switch (mobTier)
            {
                case TIER1:
                    maxHealth = random.nextInt(50, 70);
                    break;
                case TIER2:
                    maxHealth = random.nextInt(200, 300);
                    break;

                case TIER3:
                    maxHealth = random.nextInt(500, 700);
                    break;

                case TIER4:
                    maxHealth = random.nextInt(900, 1100);
                    break;

                case TIER5:
                    maxHealth = random.nextInt(1300, 1500);
            }
                                                                                                                    //currenthp
            getBukkitEntity().setCustomName(ChatColor.RESET + barArray()[Integers.roundUpPositiveWithMax((((double) maxHealth / (double) maxHealth) * 20.0), 20)]);
            getBukkitEntity().setCustomNameVisible(true);
        }
    }

    public String[] barArray()
    {
        String[] barArray = new String[21];

        ChatColor red = ChatColor.RED;
        ChatColor yellow = ChatColor.YELLOW;
        ChatColor green = ChatColor.GREEN;
        ChatColor gray = ChatColor.GRAY;

        //cross platform compatibility
        String halfBlock = StringEscapeUtils.unescapeHtml("&#9612;");
        String fullBlock = StringEscapeUtils.unescapeHtml("&#9608;");

        barArray[0] = "                     ";
        barArray[1] = red + halfBlock + "                   ";
        barArray[2] = red + fullBlock + "                  ";
        barArray[3] = red + fullBlock + halfBlock + "                 ";
        barArray[4] = red + fullBlock + fullBlock + "                ";
        barArray[5] = red + fullBlock + fullBlock + halfBlock + "               ";
        barArray[6] = red + fullBlock + fullBlock + fullBlock + "              ";
        barArray[7] = yellow + fullBlock + fullBlock + fullBlock + halfBlock + "             ";
        barArray[8] = yellow + fullBlock + fullBlock + fullBlock + fullBlock + "            ";
        barArray[9] = yellow + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "           ";
        barArray[10] = yellow + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "          ";
        barArray[11] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "         ";
        barArray[12] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "        ";
        barArray[13] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "       ";
        barArray[14] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "      ";
        barArray[15] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "     ";
        barArray[16] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "    ";
        barArray[17] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + "   ";
        barArray[18] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + "  ";
        barArray[19] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + halfBlock + " ";
        barArray[20] = green + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock + fullBlock;

        return barArray;
    }
}
