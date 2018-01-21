package net.vowed.api.settings.bases;

import net.vowed.api.plugin.Vowed;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by JPaul on 8/12/2016.
 */
public class BaseSettings
{
    FileConfiguration config;

    public BaseSettings()
    {
        this.config = YamlConfiguration.loadConfiguration(new File(Vowed.getPlugin().getDataFolder() + "\\Bases\\config.yml"));

        config.options().copyDefaults(true);

        for (BaseSetting setting : BaseSetting.values())
        {
            if (setting.hasKey(config))
            {
                setting.loadFromKey(config.get(setting.path));
            }
            else
            {
                setting.addDefault(config);
            }
        }
    }
}
