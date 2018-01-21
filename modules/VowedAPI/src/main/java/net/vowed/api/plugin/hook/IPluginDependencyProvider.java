package net.vowed.api.plugin.hook;

import org.bukkit.plugin.Plugin;

/**
 * Created by JPaul on 8/10/2016.
 */
public interface IPluginDependencyProvider<T extends Plugin>
{
    T getDependency();

    boolean isHooked();

    Plugin getHandlingPlugin();

    String getDependencyName();
}
