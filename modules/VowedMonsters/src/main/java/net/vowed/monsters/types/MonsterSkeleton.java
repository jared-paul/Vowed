package net.vowed.monsters.types;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import net.vowed.api.items.Tier;
import net.vowed.api.mobs.monsters.MonsterType;
import net.vowed.core.mobs.type.Ranged;
import net.vowed.monsters.ai.combat.MonsterBowAttack;

import javax.annotation.Nullable;

/**
 * Created by JPaul on 2/20/2016.
 */
public class MonsterSkeleton extends AbstractMonster implements Ranged
{
    private static final DataWatcherObject<Boolean> a = DataWatcher.a(MonsterSkeleton.class, DataWatcherRegistry.h);

    public MonsterSkeleton(World world, @Nullable Tier tier)
    {
        super(world, tier);
    }

    @Override
    protected void initSpecificPathfinders()
    {
        this.goalSelector.a(0, new MonsterBowAttack(this, 0.2D, 20, 15.0F));
    }

    @Override
    protected void initDatawatcher()
    {
        this.datawatcher.register(a, false);
    }

    @Override
    public MonsterType getType()
    {
        return MonsterType.SKELETON;
    }

    public void poweringBow(boolean flag)
    {
        this.datawatcher.set(a, flag);
    }
}
