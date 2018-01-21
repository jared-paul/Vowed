package net.vowed.api.player;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.player.races.RaceType;
import net.vowed.api.player.races.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by JPaul on 8/14/2016.
 */
public interface IVowedPlayer
{
    IClan getClan();

    IClanPlayer getClanPlayer();

    UUID getUUID();

    PlayerStats getStats();

    RaceType getRace();

    Skin getSkin();

    double getMoney();

    void setMoney(double amount);

    void addMoney(double amount);

    void subtractMoney(double amount);

    void updateHealth();

    default Player getPlayer()
    {
        return Bukkit.getPlayer(getUUID());
    }
}
