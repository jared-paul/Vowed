package net.vowed.core.mobs.util;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.Entity;

import java.lang.reflect.Field;

/**
 * Created by JPaul on 3/1/2016.
 */
public class DatawatcherUtil
{
    public static DataWatcher getDataWatcher(Entity entity)
    {
        return entity.getDataWatcher();
    }

    public static DataWatcherObject<Integer> getInt(Entity entity, String field)
    {
        try
        {
            Field dataObject = entity.getClass().getDeclaredField(field);
            dataObject.setAccessible(true);

            return (DataWatcherObject<Integer>) dataObject.get(entity);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static DataWatcherObject<Boolean> getBoolean(Entity entity, String field)
    {
        try
        {
            Field dataObject = entity.getClass().getDeclaredField(field);
            dataObject.setAccessible(true);

            return (DataWatcherObject<Boolean>) dataObject.get(entity);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static DataWatcherObject<Byte> getByte(Entity entity, String field)
    {
        try
        {
            Field dataObject = entity.getClass().getDeclaredField(field);
            dataObject.setAccessible(true);

            return (DataWatcherObject<Byte>) dataObject.get(entity);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void setBoolean(Entity entity, DataWatcherObject<Boolean> dataWatcherObject, boolean bool)
    {
        entity.getDataWatcher().set(dataWatcherObject, bool);
    }

    public static void setBoolean(Entity entity, String dataWatcherObjectField, boolean bool)
    {
        entity.getDataWatcher().set(getBoolean(entity, dataWatcherObjectField), bool);
    }

    public static void setInteger(Entity entity, DataWatcherObject<Integer> dataWatcherObject, int integer)
    {
        entity.getDataWatcher().set(dataWatcherObject, integer);
    }

    public static void setInteger(Entity entity, String dataWatcherObjectField, int integer)
    {
        entity.getDataWatcher().set(getInt(entity, dataWatcherObjectField), integer);
    }

    public static void setByte(Entity entity, DataWatcherObject<Byte> dataWatcherObject, byte b)
    {
        entity.getDataWatcher().set(dataWatcherObject, b);
    }

    public static void setByte(Entity entity, String dataWatcherObjectField, byte b)
    {
        entity.getDataWatcher().set(getByte(entity, dataWatcherObjectField), b);
    }
}
