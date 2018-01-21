package net.vowed.plugin;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;
import net.vowed.api.TaskChain;
import net.vowed.api.clans.IClanRegistry;
import net.vowed.api.clans.members.IClanPlayerRegistry;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.items.IItemFactory;
import net.vowed.api.mobs.monsters.IMonsterRegistry;
import net.vowed.api.player.AbstractVowedPlayerRegistry;
import net.vowed.api.plugin.IVowedPlugin;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.plugin.hook.IWorldEditProvider;
import net.vowed.api.plugin.hook.IWorldGuardProvider;
import net.vowed.api.requests.IRequestManager;
import net.vowed.api.settings.bases.BaseSettings;
import net.vowed.api.shops.IShopRegistry;
import net.vowed.api.wir.IComponentContainer;
import net.vowed.bases.commands.BaseCommands;
import net.vowed.clans.ClanRegistry;
import net.vowed.clans.bulletinboard.image.test.BuildTask;
import net.vowed.clans.bulletinboard.image.test.ImageMapCommand;
import net.vowed.clans.bulletinboard.image.test.Test;
import net.vowed.clans.commands.ClanCommands;
import net.vowed.clans.members.ClanPlayerRegistry;
import net.vowed.clans.requests.RequestManager;
import net.vowed.core.commands.CommandManager;
import net.vowed.core.commands.SimpleInjector;
import net.vowed.core.hook.WorldEditProvider;
import net.vowed.core.hook.WorldGuardProvider;
import net.vowed.items.ItemFactory;
import net.vowed.items.VowedItems;
import net.vowed.items.armour.commands.ArmourCommands;
import net.vowed.items.commands.ItemCommands;
import net.vowed.items.shield.commands.ShieldCommands;
import net.vowed.items.weapon.commands.WeaponCommands;
import net.vowed.monsters.MonsterRegistry;
import net.vowed.monsters.commands.MonsterCommands;
import net.vowed.player.VowedPlayerRegistry;
import net.vowed.shops.ShopRegistry;
import net.vowed.wir.ComponentContainer;
import net.vowed.wir.WiRCommands;
import net.vowed.wir.listeners.OnPlayerJoin;
import net.vowed.wir.listeners.OnPlayerMove;
import net.vowed.wir.test.MoonChunkGenerator;
import net.vowed.wir.test.MoonCommandExec;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.IOException;

/**
 * Created by JPaul on 8/10/2016.
 */
public class VowedPlugin extends JavaPlugin implements IVowedPlugin
{
    private static final CommandManager COMMANDS = new CommandManager();

    private WorldGuardProvider worldGuardProvider;
    private WorldEditProvider worldEditProvider;
    private AbstractVowedPlayerRegistry playerRegistry;
    private SQLStorage sqlStorage;
    private IClanRegistry clanRegistry;
    private IClanPlayerRegistry clanPlayerRegistry;
    private IRequestManager requestManager;
    private IComponentContainer componentContainer;
    private EffectManager effectManager;
    private IItemFactory itemGenerator;
    private IShopRegistry shopRegistry;
    private IMonsterRegistry monsterRegistry;
    Test test;

    public void onEnable()
    {
        Vowed.setPlugin(this);
        TaskChain.initialize(this);

        worldGuardProvider = new WorldGuardProvider(this);
        worldEditProvider = new WorldEditProvider(this);
        playerRegistry = new VowedPlayerRegistry();
        sqlStorage = new SQLStorage();
        clanRegistry = new ClanRegistry();
        clanPlayerRegistry = new ClanPlayerRegistry();
        requestManager = new RequestManager();
        componentContainer = new ComponentContainer();
        effectManager = new EffectManager(EffectLib.instance());
        itemGenerator = new ItemFactory();
        shopRegistry = new ShopRegistry();
        monsterRegistry = new MonsterRegistry();
        new VowedItems().onEnable();

        registerListeners();
        initSettings();

        COMMANDS.setInjector(new SimpleInjector());
        initCommands();

        monsterRegistry.registerMonsterEntities();

        test = new Test();
        test.onEnable();

        getCommand("imagemap").setExecutor(new ImageMapCommand(test));
    }

    public void onDisable()
    {
        try
        {
            clanRegistry.saveClans();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        test.onDisable();
        Bukkit.getScheduler().cancelAllTasks();
    }

    public void registerListeners()
    {
        Reflections reflections = new Reflections("net.vowed");

        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerMove(), this);

        for (Class clazz : reflections.getSubTypesOf(Listener.class))
        {
            if (!(clazz.getName().contains("PluginDependencyProvider")) && !(clazz.getName().contains("ItemPair")) && !(clazz.getName().contains("ItemSizeChecker")) && !(clazz.getName().contains("BuildTask")))
            {
                try
                {
                    Bukkit.getPluginManager().registerEvents((Listener) clazz.newInstance(), this);
                }
                catch (InstantiationException | IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initCommands()
    {
        COMMANDS.register(ClanCommands.class);
        COMMANDS.register(BaseCommands.class);
        COMMANDS.register(WiRCommands.class);
        COMMANDS.register(ArmourCommands.class);
        COMMANDS.register(WeaponCommands.class);
        COMMANDS.register(ShieldCommands.class);
        COMMANDS.register(ItemCommands.class);
        COMMANDS.register(MonsterCommands.class);

        getCommand("vowed").setExecutor(COMMANDS);
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
    {
        return new MoonChunkGenerator();
    }

    private void initSettings()
    {
        new BaseSettings();
    }

    @Override
    public IWorldGuardProvider getWorldGuardProvider()
    {
        return worldGuardProvider;
    }

    @Override
    public IWorldEditProvider getWorldEditProvider()
    {
        return worldEditProvider;
    }

    @Override
    public AbstractVowedPlayerRegistry getPlayerRegistry()
    {
        return playerRegistry;
    }

    @Override
    public IClanRegistry getClanRegistry()
    {
        return clanRegistry;
    }

    @Override
    public IClanPlayerRegistry getClanPlayerRegistry()
    {
        return clanPlayerRegistry;
    }

    @Override
    public IRequestManager getRequestManager()
    {
        return requestManager;
    }

    @Override
    public IComponentContainer getMapComponentContainer()
    {
        return componentContainer;
    }

    @Override
    public EffectManager getEffectManager()
    {
        return effectManager;
    }

    @Override
    public IItemFactory getItemFactory()
    {
        return itemGenerator;
    }

    @Override
    public IShopRegistry getShopRegistry()
    {
        return shopRegistry;
    }

    @Override
    public IMonsterRegistry getMonsterRegistry()
    {
        return monsterRegistry;
    }

    @Override
    public SQLStorage getSQLStorage()
    {
        return sqlStorage;
    }
}
