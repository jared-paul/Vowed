package net.vowed.api.settings.bases;

import net.vowed.api.plugin.Vowed;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Created by JPaul on 8/24/2016.
 */
public enum BaseSetting
{
    BASE_SCHEMATIC("bases.schematic", new File(Vowed.getPlugin().getDataFolder() + "\\Bases\\Schematics\\base.schematic"));

    String path;
    Object defaultValue;

    BaseSetting(String path, Object defaultValue)
    {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getPath()
    {
        return path;
    }

    public Object getValue()
    {
        return defaultValue;
    }

    public boolean hasKey(FileConfiguration config)
    {
        return config.isSet(path);
    }

    public void loadFromKey(Object object)
    {
        defaultValue = object;
    }

    public void addDefault(FileConfiguration config)
    {
        config.addDefault(path, defaultValue);
    }
}
