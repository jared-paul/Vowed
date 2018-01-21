package net.vowed.core.hook;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.vowed.api.plugin.hook.IWorldEditProvider;
import net.vowed.api.plugin.hook.PluginDependencyProvider;
import org.bukkit.plugin.Plugin;

/**
 * Created by JPaul on 8/10/2016.
 */
public class WorldEditProvider extends PluginDependencyProvider<WorldEditPlugin> implements IWorldEditProvider
{
    public WorldEditProvider(Plugin myPlugin)
    {
        super(myPlugin, "WorldEdit");
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
