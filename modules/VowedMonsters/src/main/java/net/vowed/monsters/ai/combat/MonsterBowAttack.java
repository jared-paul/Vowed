package net.vowed.monsters.ai.combat;

import net.minecraft.server.v1_12_R1.*;
import net.vowed.core.mobs.util.DatawatcherUtil;
import net.vowed.monsters.types.AbstractMonster;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

/**
 * Created by JPaul on 3/2/2016.
 */
public class MonsterBowAttack extends PathfinderGoal
{
    private final AbstractMonster monster;
    private final double b;
    private int c;
    private final float d;
    private int e = -1;
    private int f;
    private boolean g;
    private boolean h;
    private int i = -1;

    public MonsterBowAttack(AbstractMonster monster, double paramDouble, int paramInt, float paramFloat)
    {
        this.monster = monster;
        this.b = paramDouble;
        this.c = paramInt;
        this.d = (paramFloat * paramFloat);
        a(3);
    }

    public void b(int paramInt)
    {
        this.c = paramInt;
    }

    public boolean a()
    {
        if (this.monster.getGoalTarget() == null) {
            return false;
        }
        return f();
    }

    protected boolean f()
    {
        return (!this.monster.getItemInMainHand().isEmpty()) && (this.monster.getItemInMainHand().getItem() == Items.BOW);
    }

    public boolean b()
    {
        return ((a()) || (!this.monster.getNavigation().o())) && (f());
    }

    public void c()
    {
        super.c();

        ((IRangedEntity) this.monster).p(true);
    }

    public void d()
    {
        super.d();

        ((IRangedEntity)this.monster).p(false);
        this.f = 0;
        this.e = -1;
        this.monster.cN();
    }

    public void e()
    {
        EntityLiving localEntityLiving = this.monster.getGoalTarget();
        if (localEntityLiving == null) {
            return;
        }
        double d1 = this.monster.d(localEntityLiving.locX, localEntityLiving.getBoundingBox().b, localEntityLiving.locZ);
        boolean bool1 = this.monster.getEntitySenses().a(localEntityLiving);
        boolean bool2 = this.f > 0;
        if (bool1 != bool2) {
            this.f = 0;
        }
        if (bool1) {
            this.f += 1;
        } else {
            this.f -= 1;
        }
        if ((d1 > this.d) || (this.f < 20))
        {
            this.monster.getNavigation().a(localEntityLiving, this.b);
            this.i = -1;
        }
        else
        {
            this.monster.getNavigation().p();
            this.i += 1;
        }
        if (this.i >= 20)
        {
            if (this.monster.getRandom().nextFloat() < 0.3D) {
                this.g = (!this.g);
            }
            if (this.monster.getRandom().nextFloat() < 0.3D) {
                this.h = (!this.h);
            }
            this.i = 0;
        }
        if (this.i > -1)
        {
            if (d1 > this.d * 0.75F) {
                this.h = false;
            } else if (d1 < this.d * 0.25F) {
                this.h = true;
            }
            this.monster.getControllerMove().a(this.h ? -0.5F : 0.5F, this.g ? 0.5F : -0.5F);
            this.monster.a(localEntityLiving, 30.0F, 30.0F);
        }
        else
        {
            this.monster.getControllerLook().a(localEntityLiving, 30.0F, 30.0F);
        }
        if (this.monster.isHandRaised())
        {
            if ((!bool1) && (this.f < -60))
            {
                this.monster.cN();
            }
            else if (bool1)
            {
                int j = this.monster.cL();
                if (j >= 20)
                {
                    this.monster.cN();
                    ((IRangedEntity)this.monster).a(localEntityLiving, ItemBow.b(j));
                    this.e = this.c;
                }
            }
        }
        else if ((--this.e <= 0) && (this.f >= -60)) {
            this.monster.c(EnumHand.MAIN_HAND);
        }
    }

    private void a(boolean flag)
    {
        DataWatcherObject<Boolean> a = DatawatcherUtil.getBoolean(monster, "a");
        monster.getDataWatcher().set(a, Boolean.valueOf(flag  ));
    }
}
