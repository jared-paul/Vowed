package net.vowed.core.items.util.weapon;

import java.util.Map;

/**
 * Created by JPaul on 5/2/2016.
 */
public class WeaponStats
{
    private int damageLOW;
    private int damageHIGH;
    private int blind;
    private int slow;
    private int trueDamage;
    private int durability;
    private Map<String, Integer> magical;

    public WeaponStats(int damageLOW, int damageHIGH, int blind, int slow, int trueDamage, int durability, Map<String, Integer> magical)
    {
        this.damageLOW = damageLOW;
        this.damageHIGH = damageHIGH;
        this.blind = blind;
        this.slow = slow;
        this.trueDamage = trueDamage;
        this.durability = durability;
        this.magical = magical;
    }

    public int getDamageLOW()
    {
        return damageLOW;
    }

    public int getDamageHIGH()
    {
        return damageHIGH;
    }

    public int getBlind()
    {
        return blind;
    }

    public int getSlow()
    {
        return slow;
    }

    public int getTrueDamage()
    {
        return trueDamage;
    }

    public int getDurability()
    {
        return durability;
    }

    public Map<String, Integer> getMagical()
    {
        return magical;
    }
}
