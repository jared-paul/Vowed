package net.vowed.monsters.types;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import net.vowed.api.items.Tier;
import net.vowed.api.mobs.monsters.MonsterType;
import net.vowed.core.mobs.type.Melee;
import net.vowed.core.mobs.util.DatawatcherUtil;

import javax.annotation.Nullable;

/**
 * Created by JPaul on 2/20/2016.
 */
public class MonsterCreeper extends AbstractMonster implements Melee
{
    private static final DataWatcherObject<Integer> a = DataWatcher.a(MonsterCreeper.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Boolean> b = DataWatcher.a(MonsterCreeper.class, DataWatcherRegistry.h);
    private static final DataWatcherObject<Boolean> c = DataWatcher.a(MonsterCreeper.class, DataWatcherRegistry.h);

    public MonsterCreeper(World world, @Nullable Tier tier)
    {
        super(world, tier);
        setPowered(true);
    }

    @Override
    protected void initSpecificPathfinders()
    {

    }

    @Override
    protected void initDatawatcher()
    {
        this.datawatcher.register(a, -1);
        this.datawatcher.register(b, false);
        this.datawatcher.register(c, false);
    }

    @Override
    public MonsterType getType()
    {
        return MonsterType.CREEPER;
    }

    public void setPowered(boolean flag)
    {
        DatawatcherUtil.setBoolean(this, "b", flag);
    }

    public void ignite(boolean flag)
    {
        DatawatcherUtil.setBoolean(this, "c", flag);
    }
}
