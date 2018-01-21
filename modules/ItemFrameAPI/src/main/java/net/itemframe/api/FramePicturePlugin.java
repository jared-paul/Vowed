package net.itemframe.api;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.itemframe.api.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class FramePicturePlugin extends JavaPlugin {
    public static Logger log;
    private static FrameManager manager = null;
    private static FramePicturePlugin instance;

    @Override
    public void onLoad() {
        log = this.getLogger();
        instance = this;

        manager = new FrameManager(this);
    }

    @Override
    public void onEnable() {
        if (log == null) log = this.getLogger();
        if (instance == null) instance = this;

        if (manager == null) manager = new FrameManager(this);
        manager.onEnable();
        log.info(Lang.PLUGIN_ENABLED.getText());
    }

    @Override
    public void onDisable() {
        if (manager != null)
            manager.onDisable();
        log.info(Lang.PLUGIN_DISABLED.getText());
    }

    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin != null && plugin instanceof WorldGuardPlugin)
            return (WorldGuardPlugin)plugin;
        else
            return null;
    }

    public static FramePicturePlugin getPlugin() {
        return instance;
    }

    public static FrameManager getManager() {
        return manager;
    }

}