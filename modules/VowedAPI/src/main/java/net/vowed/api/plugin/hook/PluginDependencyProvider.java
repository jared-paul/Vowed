package net.vowed.api.plugin.hook;

import net.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by JPaul on 8/10/2016.
 */
public abstract class PluginDependencyProvider<T extends Plugin> implements IPluginDependencyProvider<T>
{
    protected PluginDependencyProvider<T> instance;
    private T dependencyPlugin;
    protected boolean hooked;
    private Plugin pluginInstance;
    private String dependencyName;

    public PluginDependencyProvider(Plugin myPlugin, String dependencyName)
    {
        this.instance = this;
        this.pluginInstance = myPlugin;
        this.dependencyName = dependencyName;

        if (dependencyPlugin == null && !hooked)
        {
            try
            {
                dependencyPlugin = (T) Bukkit.getPluginManager().getPlugin(getDependencyName());

                if (dependencyPlugin != null && dependencyPlugin.isEnabled())
                {
                    onHook();
                    hooked = true;
                    Vowed.LOG.info("Successfully hooked into [" + dependencyPlugin.getName() + "]");
                }
            }
            catch (Exception e)
            {
                Vowed.LOG.warning("Could not create PluginDependencyProvider for: " + getDependencyName() + " - " + e.getMessage());
            }
        }

        Bukkit.getPluginManager().registerEvents(new Listener()
        {
            @EventHandler
            protected void onEnable(PluginEnableEvent enableEvent)
            {
                if ((dependencyPlugin == null) && (enableEvent.getPlugin().getName().equalsIgnoreCase(getDependencyName())))
                {
                    try
                    {
                        dependencyPlugin = (T) enableEvent.getPlugin();
                        onHook();
                        hooked = true;
                        //TODO log hook
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("Failed to hook into plugin: " + enableEvent.getPlugin().getName());
                    }
                }
            }

            @EventHandler
            protected void onDisable(PluginDisableEvent disableEvent)
            {
                if ((dependencyPlugin != null) && (disableEvent.getPlugin().getName().equalsIgnoreCase(getDependencyName())))
                {
                    dependencyPlugin = null;
                    onUnhook();
                    hooked = false;
                    Vowed.LOG.info("Successfully unhooked from [" + disableEvent.getPlugin().getName() + "]");
                }
            }
        }, getHandlingPlugin());
    }

    public abstract void onHook();

    public abstract void onUnhook();

    @Override
    public T getDependency()
    {
        if (dependencyPlugin == null)
        {
            throw new RuntimeException("Dependency is NULL");
        }

        return dependencyPlugin;
    }

    @Override
    public boolean isHooked()
    {
        return hooked;
    }

    @Override
    public Plugin getHandlingPlugin()
    {
        if (pluginInstance == null)
        {
            throw new RuntimeException("HandlingPlugin is NULL");
        }

        return pluginInstance;
    }

    @Override
    public String getDependencyName()
    {
        if (dependencyName == null)
        {
            throw new RuntimeException("Dependency name is NULL");
        }

        return dependencyName;
    }
}
