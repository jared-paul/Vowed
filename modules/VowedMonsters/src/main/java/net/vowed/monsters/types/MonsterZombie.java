package net.vowed.monsters.types;

import net.minecraft.server.v1_12_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_12_R1.World;
import net.vowed.api.items.Tier;
import net.vowed.api.mobs.monsters.MonsterType;
import net.vowed.core.mobs.type.Melee;

import javax.annotation.Nullable;

/**
 * Created by JPaul on 2/20/2016.
 */
public class MonsterZombie extends AbstractMonster implements Melee
{
    public MonsterZombie(World world, @Nullable Tier tier)
    {
        super(world, tier);
    }

    @Override
    protected void initSpecificPathfinders()
    {
        this.goalSelector.a(0, new PathfinderGoalMeleeAttack(this, 0.4D, true));
    }

    @Override
    protected void initDatawatcher()
    {

    }

    @Override
    public MonsterType getType()
    {
        return MonsterType.ZOMBIE;
    }
}
