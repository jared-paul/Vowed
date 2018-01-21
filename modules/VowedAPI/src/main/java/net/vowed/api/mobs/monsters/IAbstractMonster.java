package net.vowed.api.mobs.monsters;

import net.minecraft.server.v1_12_R1.Entity;
import org.bukkit.Location;

/**
 * Created by JPaul on 2017-02-08.
 */
public interface IAbstractMonster
{
    void setLocation(Location location);

    int getCurrentHP();

    void setCurrentHP(int health);

    int getMaxHP();

    void setMaxHP(int maxHealth);

    Entity getEntity();
}
