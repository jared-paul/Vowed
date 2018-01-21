package net.vowed.player;

import net.vowed.api.clans.IClan;
import net.vowed.api.clans.members.IClanPlayer;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.player.PlayerStats;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.player.races.Gender;
import net.vowed.api.player.races.RaceType;
import net.vowed.api.player.races.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by JPaul on 8/15/2016.
 */
public class VowedPlayer implements IVowedPlayer
{
    private UUID playerUUID;
    private int currentHealth;
    private PlayerStats stats;
    private Gender gender;
    private RaceType race;
    private double money;

    public VowedPlayer(UUID playerUUID)
    {
        this.playerUUID = playerUUID;
        this.currentHealth = 50;
        this.stats = new PlayerStats();
        this.gender = Gender.MALE;
        this.race = RaceType.ELF;
        this.money = 1000;
        //TODO choose gender, race, money, and stats
    }

    @Override
    public IClan getClan()
    {
        return getClanPlayer().getClan();
    }

    @Override
    public IClanPlayer getClanPlayer()
    {
        return Vowed.getClanPlayerRegistry().getClanPlayer(playerUUID);
    }

    @Override
    public UUID getUUID()
    {
        return playerUUID;
    }

    @Override
    public PlayerStats getStats()
    {
        return stats;
    }

    @Override
    public RaceType getRace()
    {
        return race;
    }

    @Override
    public Skin getSkin()
    {
        return race.getSkin(gender);
    }

    @Override
    public double getMoney()
    {
        return money;
    }

    @Override
    public void setMoney(double money)
    {
        this.money = money;
    }

    @Override
    public void addMoney(double amount)
    {
        this.money += amount;
    }

    @Override
    public void subtractMoney(double amount)
    {
        this.money -= amount;
    }

    @Override
    public void updateHealth()
    {
        Player player = Bukkit.getPlayer(playerUUID);

        if (player != null)
        {
            int maxHealth = stats.getMaxHealth();
            double playerNewHP = (double) this.currentHealth;
            double healthPercent = playerNewHP / (double) maxHealth;
            int healthDisplay = (int) (healthPercent * 20D);
            if (healthDisplay <= 0)
            {
                player.setHealth(1);
            }
            else if (healthDisplay > 20)
            {
                player.setHealth(20);
            }
            else
            {
                player.setHealth(healthDisplay);
            }

            this.currentHealth = (int) playerNewHP;
        }
    }
}
