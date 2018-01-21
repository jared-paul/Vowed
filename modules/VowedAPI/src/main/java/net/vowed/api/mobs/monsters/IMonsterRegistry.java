package net.vowed.api.mobs.monsters;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by JPaul on 2017-02-08.
 */
public interface IMonsterRegistry
{
    IAbstractMonster createMonsterEntity(World world, MonsterType monsterType);

    boolean spawnMonsterEntity(IAbstractMonster monster, Location location);

    void registerMonsterEntities();

    void unregisterMonsterEntities();
}
