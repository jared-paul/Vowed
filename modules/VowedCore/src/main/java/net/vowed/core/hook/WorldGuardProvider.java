package net.vowed.core.hook;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import net.vowed.api.plugin.hook.IWorldGuardProvider;
import net.vowed.api.plugin.hook.PluginDependencyProvider;
import org.bukkit.plugin.Plugin;

/**
 * Created by JPaul on 8/10/2016.
 */
public class WorldGuardProvider extends PluginDependencyProvider<WorldGuardPlugin> implements IWorldGuardProvider
{
    public WorldGuardProvider(Plugin myPlugin)
    {
        super(myPlugin, "WorldGuard");
    }

    @Override
    public void onHook()
    {
        //TODO register listener on hook
    }

    @Override
    public void onUnhook()
    {

    }
}
