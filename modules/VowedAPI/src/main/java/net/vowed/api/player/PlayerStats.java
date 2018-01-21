package net.vowed.api.player;

/**
 * Created by JPaul on 2017-02-04.
 */
public class PlayerStats
{
    private int maxHealth = 50;
    private int healthRegen;
    private int energyRegen;
    private int armourPercent;
    private int block;
    private int dodge;
    private float speed;

    public int getMaxHealth()
    {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth)
    {
        this.maxHealth = maxHealth;
    }

    public int getHealthRegen()
    {
        return healthRegen;
    }

    public void setHealthRegen(int healthRegen)
    {
        this.healthRegen = healthRegen;
    }

    public int getEnergyRegen()
    {
        return energyRegen;
    }

    public void setEnergyRegen(int energyRegen)
    {
        this.energyRegen = energyRegen;
    }

    public int getArmourPercent()
    {
        return armourPercent;
    }

    public void setArmourPercent(int armourPercent)
    {
        this.armourPercent = armourPercent;
    }

    public int getBlock()
    {
        return block;
    }

    public void setBlock(int block)
    {
        this.block = block;
    }

    public int getDodge()
    {
        return dodge;
    }

    public void setDodge(int dodge)
    {
        this.dodge = dodge;
    }

    public float getSpeed()
    {
        return speed;
    }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
}
