package net.vowed.api.plugin;

import de.slikey.effectlib.EffectManager;
import net.vowed.api.clans.IClanRegistry;
import net.vowed.api.clans.members.IClanPlayerRegistry;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.items.IItemFactory;
import net.vowed.api.logging.Log;
import net.vowed.api.mobs.monsters.IMonsterRegistry;
import net.vowed.api.player.AbstractVowedPlayerRegistry;
import net.vowed.api.requests.IRequestManager;
import net.vowed.api.shops.IShopRegistry;
import net.vowed.api.wir.IComponentContainer;

/**
 * Created by JPaul on 8/10/2016.
 */
public class Vowed
{
    private static IVowedPlugin PLUGIN;
    public static final Log LOG = new Log("[Vowed]");

    public static void setPlugin(IVowedPlugin plugin)
    {
        if (PLUGIN != null)
        {
            return;
        }

        PLUGIN = plugin;
    }

    public static IVowedPlugin getPlugin()
    {
        return PLUGIN;
    }

    public static IClanRegistry getClanHandler()
    {
        return PLUGIN.getClanRegistry();
    }

    public static IClanPlayerRegistry getClanPlayerRegistry()
    {
        return PLUGIN.getClanPlayerRegistry();
    }

    public static IRequestManager getRequestManager()
    {
        return PLUGIN.getRequestManager();
    }

    public static IComponentContainer getMapComponentContainer()
    {
        return PLUGIN.getMapComponentContainer();
    }

    public static AbstractVowedPlayerRegistry getPlayerRegistry()
    {
        return PLUGIN.getPlayerRegistry();
    }

    public static EffectManager getEffectManager()
    {
        return PLUGIN.getEffectManager();
    }

    public static IMonsterRegistry getMonsterRegistry()
    {
        return PLUGIN.getMonsterRegistry();
    }

    public static IItemFactory getItemFactory()
    {
        return PLUGIN.getItemFactory();
    }

    public static IShopRegistry getShopRegistry()
    {
        return PLUGIN.getShopRegistry();
    }

    public static SQLStorage getSQLStorage()
    {
        return PLUGIN.getSQLStorage();
    }
}
