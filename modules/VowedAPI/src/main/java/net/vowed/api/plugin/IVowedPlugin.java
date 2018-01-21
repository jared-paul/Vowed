package net.vowed.api.plugin;

import de.slikey.effectlib.EffectManager;
import net.vowed.api.clans.IClanRegistry;
import net.vowed.api.clans.members.IClanPlayerRegistry;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.items.IItemFactory;
import net.vowed.api.mobs.monsters.IMonsterRegistry;
import net.vowed.api.player.AbstractVowedPlayerRegistry;
import net.vowed.api.plugin.hook.IWorldEditProvider;
import net.vowed.api.plugin.hook.IWorldGuardProvider;
import net.vowed.api.requests.IRequestManager;
import net.vowed.api.shops.IShopRegistry;
import net.vowed.api.wir.IComponentContainer;
import org.bukkit.plugin.Plugin;

/**
 * Created by JPaul on 8/10/2016.
 */
public interface IVowedPlugin extends Plugin
{
    IWorldGuardProvider getWorldGuardProvider();

    IWorldEditProvider getWorldEditProvider();

    AbstractVowedPlayerRegistry getPlayerRegistry();

    IClanRegistry getClanRegistry();

    IClanPlayerRegistry getClanPlayerRegistry();

    IRequestManager getRequestManager();

    IComponentContainer getMapComponentContainer();

    EffectManager getEffectManager();

    IItemFactory getItemFactory();

    IShopRegistry getShopRegistry();

    IMonsterRegistry getMonsterRegistry();

    SQLStorage getSQLStorage();
}
