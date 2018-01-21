package net.vowed.api.mobs.monsters;

import net.vowed.api.mobs.monsters.types.IMonsterCreeper;
import net.vowed.api.mobs.monsters.types.IMonsterSkeleton;
import net.vowed.api.mobs.monsters.types.IMonsterZombie;

/**
 * Created by JPaul on 2/20/2016.
 */
public enum MonsterType
{
    ZOMBIE("ZOMBIE", 54, IMonsterZombie.class),
    CREEPER("CREEPER", 50, IMonsterCreeper.class),
    SKELETON("SKELETON", 51, IMonsterSkeleton.class);

    private String bukkitType;
    private int ID;
    private Class<? extends IAbstractMonster> monsterClass;

    MonsterType(String bukkitType, int ID, Class<? extends IAbstractMonster> monsterClass)
    {
        this.bukkitType = bukkitType;
        this.ID = ID;
        this.monsterClass = monsterClass;
    }

    public String getBukkitName()
    {
        return bukkitType;
    }

    public String getMinecraftName()
    {
        return "";
    }

    public int getTypeID()
    {
        return ID;
    }

    public Class<? extends IAbstractMonster> getMonsterClass()
    {
        return monsterClass;
    }

    public MonsterType getTypeByBukkitName(String bukkitType)
    {
        for (MonsterType monsterType : MonsterType.values())
        {
            if (monsterType.getBukkitName().equalsIgnoreCase(bukkitType))
            {
                return monsterType;
            }
        }

        return null;
    }

    public MonsterType getTypeByName(String name)
    {
        for (MonsterType monsterType : MonsterType.values())
        {
            if (monsterType.name().equalsIgnoreCase(name))
            {
                return monsterType;
            }
        }

        return null;
    }

    public static MonsterType getMonsterAlias(String type)
    {
        switch (type.toLowerCase())
        {
            case "zombie":
                return ZOMBIE;
            case "skeleton":
                return SKELETON;
            case "creeper":
                return CREEPER;
            default:
                return null;
        }
    }
}
