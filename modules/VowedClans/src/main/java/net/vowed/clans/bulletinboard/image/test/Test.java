package net.vowed.clans.bulletinboard.image.test;

import net.vowed.api.clans.billboard.IBoardImage;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.plugin.Vowed;
import net.vowed.clans.bulletinboard.image.BoardImage;
import net.vowed.clans.bulletinboard.image.ImageMap;
import net.vowed.clans.bulletinboard.image.factory.ImageFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JPaul on 2017-03-20.
 */
public class Test implements Listener
{
    public static final int MAP_WIDTH = 128;
    public static final int MAP_HEIGHT = 128;

    private Map<String, PlacingCacheEntry> placing = new HashMap<String, PlacingCacheEntry>();
    private Map<Short, ImageMap> maps = new HashMap<Short, ImageMap>();
    private Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
    private List<Short> mapIDs = new ArrayList<>();
    private BuildTask sendTask;

    public void onEnable()
    {
        if (!new File(Vowed.getPlugin().getDataFolder(), "images").exists())
            new File(Vowed.getPlugin().getDataFolder(), "images").mkdirs();

        //loadMaps();
        Vowed.getPlugin().getServer().getPluginManager().registerEvents(this, Vowed.getPlugin());
        sendTask = new BuildTask(this, 8);
        Vowed.getPlugin().getServer().getPluginManager().registerEvents(sendTask, Vowed.getPlugin());
        sendTask.runTaskTimer(Vowed.getPlugin(), 20, 20);
    }

    public void onDisable()
    {
        //saveMaps();
        Vowed.getPlugin().getServer().getScheduler().cancelTasks(Vowed.getPlugin());
    }

    public List<Short> getMaps()
    {
        return mapIDs;
    }

    public void startPlacing(Player p, boolean fastsend)
    {
        BufferedImage bufferedImage = ImageFactory.getImage(Vowed.getPlayerRegistry().getVowedPlayer(p).getClan().getBulletinBoard());
        String file = Vowed.getPlugin().getDataFolder() + "\\images\\test1.png";

        try
        {
            ImageIO.write(bufferedImage, "PNG", new File(file));
        } catch (IOException e)
        {
            e.printStackTrace();
        }


        placing.put(p.getName(), new PlacingCacheEntry("test1.png", fastsend));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent e)
    {
        if (!e.hasBlock())
            return;

        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!placing.containsKey(e.getPlayer().getName()))
            return;

        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(e.getPlayer());
        IBoardImage boardImage = vowedPlayer.getClan().getBulletinBoard().getBoardImage();
        boardImage.place(e.getClickedBlock(), e.getBlockFace(), false);

        //saveMaps();

        placing.remove(e.getPlayer().getName());
    }

    /*
    private BufferedImage loadImage(String file)
    {
        if (images.containsKey(file))
            return images.get(file);

        File f = new File(Vowed.getPlugin().getDataFolder(), "images" + File.separatorChar + file);
        BufferedImage image = null;

        if (!f.exists())
            return null;

        try
        {
            image = ImageIO.read(f);
            images.put(file, image);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;
    }

    @SuppressWarnings("deprecation")
    private void loadMaps()
    {
        File file = new File(Vowed.getPlugin().getDataFolder(), "maps.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false))
        {
            short id = Short.parseShort(key);

            MapView map = Vowed.getPlugin().getServer().getMap(id);

            for (MapRenderer r : map.getRenderers())
            {
                map.removeRenderer(r);
            }

            String image = config.getString(key + ".image");
            int x = config.getInt(key + ".x");
            int y = config.getInt(key + ".y");
            boolean fastsend = config.getBoolean(key + ".fastsend", false);

            BufferedImage bimage = loadImage(image);

            if (bimage == null)
            {
                Vowed.LOG.warning("Image file " + image + " not found, removing this map!");
                continue;
            }

            if (fastsend)
                mapIDs.add(id);

            map.addRenderer(new ImageMapRenderer(loadImage(image), x, y));
            maps.put(id, new ImageMap(image, x, y, fastsend));
        }
    }

    private void saveMaps()
    {
        File file = new File(Vowed.getPlugin().getDataFolder(), "maps.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false))
        {
            config.set(key, null);
        }

        for (Map.Entry<Short, ImageMap> e : maps.entrySet())
        {
            config.set(e.getKey() + ".image", e.getValue().getImageFile());
            config.set(e.getKey() + ".x", e.getValue().getX());
            config.set(e.getKey() + ".y", e.getValue().getY());
            config.set(e.getKey() + ".fastsend", e.getValue().isFastSend());
        }

        try
        {
            config.save(file);
        } catch (IOException e1)
        {
            Vowed.LOG.severe("Failed to save maps.yml!");
            e1.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void reloadImage(String file)
    {
        images.remove(file);
        BufferedImage image = loadImage(file);

        int width = (int) Math.ceil((double) image.getWidth() / (double) MAP_WIDTH);
        int height = (int) Math.ceil((double) image.getHeight() / (double) MAP_HEIGHT);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                short id = getMapItem(file, x * MAP_WIDTH, y * MAP_HEIGHT, image).getDurability();
                MapView map = Vowed.getPlugin().getServer().getMap(id);

                for (MapRenderer renderer : map.getRenderers())
                {
                    if (renderer instanceof ImageMapRenderer)
                        ((ImageMapRenderer) renderer).recalculateInput(image, x * MAP_WIDTH, y * MAP_HEIGHT);
                }

                sendTask.addToQueue(id);
            }
        }

    }
    */
}
