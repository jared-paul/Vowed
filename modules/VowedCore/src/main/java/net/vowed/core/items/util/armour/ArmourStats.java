package net.vowed.core.items.util.armour;

/**
 * Created by JPaul on 5/2/2016.
 */
public class ArmourStats
{
    private int HP;
    private int hpRegen;
    private int energyRegen;
    private int block;
    private int dodge;
    private int armourPercent;
    private float speed;
    private int durability;
    private int maxDurability;

    public ArmourStats(int HP, int hpRegen, int energyRegen, int block, int dodge, int armourPercent, float speed, int durability)
    {
        this.HP = HP;
        this.hpRegen = hpRegen;
        this.energyRegen = energyRegen;
        this.block = block;
        this.dodge = dodge;
        this.armourPercent = armourPercent;
        this.speed = speed;
        this.durability = durability;
    }

    public int getHP()
    {
        return HP;
    }

    public int getHPRegen()
    {
        return hpRegen;
    }

    public int getEnergyRegen()
    {
        return energyRegen;
    }

    public int getBlock()
    {
        return block;
    }

    public int getDodge()
    {
        return dodge;
    }

    public int getArmourPercent()
    {
        return armourPercent;
    }

    public float getSpeed()
    {
        return speed;
    }

    public int getDurability()
    {
        return durability;
    }

    public int getMaxDurability()
    {
        return maxDurability;
    }
}
