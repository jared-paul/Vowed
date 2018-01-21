package net.vowed.core.items.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by JPaul on 6/14/2016.
 */
public class TripleStatStorage extends AbstractStatStorage
{
    public int statLowHigh;

    public TripleStatStorage(int statMIN, int statLowHigh, int statMAX)
    {
        super(statMIN, statMAX);
        this.statLowHigh = statLowHigh;
    }

    @Override
    public void refreshValues(FileConfiguration config, String path)
    {
        int statLow = config.getInt(path + ".MIN");
        int statLowHigh = config.getInt(path + ".MIN-MAX");
        int statHigh = config.getInt(path + ".MAX");

        this.statMIN = statLow;
        this.statLowHigh = statLowHigh;
        this.statMAX = statHigh;
    }
}
