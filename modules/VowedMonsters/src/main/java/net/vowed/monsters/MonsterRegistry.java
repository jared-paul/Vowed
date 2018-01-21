package net.vowed.monsters;

import net.minecraft.server.v1_12_R1.*;
import net.vowed.api.mobs.monsters.IAbstractMonster;
import net.vowed.api.mobs.monsters.IMonsterRegistry;
import net.vowed.api.mobs.monsters.MonsterType;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.util.reflection.ReflectionUtil;
import net.vowed.core.util.strings.Strings;
import net.vowed.monsters.types.AbstractMonster;
import net.vowed.monsters.types.MonsterCreeper;
import net.vowed.monsters.types.MonsterSkeleton;
import net.vowed.monsters.types.MonsterZombie;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JPaul on 3/3/2016.
 */
public class MonsterRegistry implements IMonsterRegistry
{
    private Map<MonsterType, Class<? extends AbstractMonster>> entityMonsterClasses = new HashMap<>();
    private RegistryMaterials<MinecraftKey, Class<? extends Entity>> original;

    public MonsterRegistry()
    {
        entityMonsterClasses.put(MonsterType.ZOMBIE, MonsterZombie.class);
        entityMonsterClasses.put(MonsterType.SKELETON, MonsterSkeleton.class);
        entityMonsterClasses.put(MonsterType.CREEPER, MonsterCreeper.class);


    }

    public void onDisable()
    {
        try
        {
            Field field = ReflectionUtil.getField(EntityTypes.class, "b");
            field.setAccessible(true);
            field.set(null, new RegistryMaterials<>());
        } catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    //copied from echopet/mypet
    public IAbstractMonster createMonsterEntity(org.bukkit.World world, MonsterType monsterType)
    {
        IAbstractMonster monster = null;

        Class<? extends AbstractMonster> entityClass = entityMonsterClasses.get(monsterType);
        World nmsWorld = ((CraftWorld) world).getHandle();

        try
        {
            Constructor<?> ctor = entityClass.getConstructor(World.class);
            Object obj = ctor.newInstance(nmsWorld);
            if (obj instanceof IAbstractMonster)
            {
                monster = (IAbstractMonster) obj;
            }
        } catch (Exception e)
        {
            Vowed.LOG.warning(Strings.handleError(entityClass.getName() + "is not a valid monster entity", "not", "valid"));
            e.printStackTrace();
        }
        return monster;
    }

    public boolean spawnMonsterEntity(IAbstractMonster monster, Location location)
    {
        if (monster != null)
        {
            World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
            monster.setLocation(location);
            nmsWorld.addEntity((AbstractMonster) monster, CreatureSpawnEvent.SpawnReason.CUSTOM);
            return true;
        }

        return false;
    }

    public void registerMonsterEntities()
    {
        for (MonsterType monsterType : entityMonsterClasses.keySet())
        {
            registerEntityType(monsterType);
        }
    }

    public void unregisterMonsterEntities()
    {
        try
        {
            Field field = ReflectionUtil.getField(EntityTypes.class, "b");
            field.setAccessible(true);
            field.set(null, new RegistryMaterials<>());
        } catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void registerEntityType(MonsterType monsterType)
    {
        EntityTypes.b.a(monsterType.getTypeID(), new MinecraftKey("Vowed" + monsterType.name()), entityMonsterClasses.get(monsterType));
    }
}
