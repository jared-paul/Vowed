package net.vowed.core.items.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by JPaul on 6/15/2016.
 */
public class ChanceStatStorage extends AbstractStatStorage
{
    public int chance;

    public ChanceStatStorage(int statMIN, int statMAX, int chance)
    {
        super(statMIN, statMAX);
        this.chance = chance;
    }

    @Override
    public void refreshValues(FileConfiguration config, String path)
    {
        int statLow = config.getInt(path + ".MIN");
        int statHigh = config.getInt(path + ".MAX");

        this.statMIN = statLow;
        this.statMAX = statHigh;
    }
}
