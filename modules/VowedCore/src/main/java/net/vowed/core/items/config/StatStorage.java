package net.vowed.core.items.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by JPaul on 6/15/2016.
 */
public interface StatStorage
{
    void refreshValues(FileConfiguration config, String path);
}
