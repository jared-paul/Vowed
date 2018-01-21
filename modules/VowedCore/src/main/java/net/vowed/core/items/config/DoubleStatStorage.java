package net.vowed.core.items.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by JPaul on 6/15/2016.
 */
public class DoubleStatStorage extends AbstractStatStorage
{
    public DoubleStatStorage(int statMIN, int statMAX)
    {
        super(statMIN, statMAX);
    }

    @Override
    public void refreshValues(FileConfiguration config, String path)
    {
    }
}
