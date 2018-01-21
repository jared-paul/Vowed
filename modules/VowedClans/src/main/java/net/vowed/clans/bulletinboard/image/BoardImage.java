package net.vowed.clans.bulletinboard.image;

import com.google.common.collect.Maps;
import net.vowed.api.clans.billboard.IBoardImage;
import net.vowed.api.clans.billboard.IBulletinBoard;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import net.vowed.clans.bulletinboard.BulletinBoard;
import net.vowed.clans.bulletinboard.image.factory.ImageFactory;
import net.vowed.clans.bulletinboard.image.render.ImageMapRenderer;
import net.vowed.core.util.serialization.LocationSerialization;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by JPaul on 2017-03-20.
 */
public class BoardImage implements IBoardImage
{
    private static final int MAP_WIDTH = 128;
    private static final int MAP_HEIGHT = 128;

    private Map<Short, ImageMap> maps = Maps.newHashMap();

    private File imageFile;
    private BufferedImage image;
    private Location location;
    private BlockFace direction;
    private IBulletinBoard bulletinBoard;

    public BoardImage(IBulletinBoard bulletinBoard)
    {
        this.bulletinBoard = bulletinBoard;
        this.imageFile = new File(bulletinBoard.getDataFolder() + "\\bulletinboard.png");
        try
        {
            if (!imageFile.exists())
            {
                BufferedImage image = ImageFactory.getImage(bulletinBoard);
                ImageIO.write(image, "PNG", imageFile);
            }

            this.image = ImageIO.read(imageFile);
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public BufferedImage getImage()
    {
        return image;
    }

    @Override
    public File getImageFile()
    {
        return imageFile;
    }

    public void load()
    {
        Vowed.getSQLStorage().executeAsyncQuery("SELECT * FROM bulletinboard WHERE clan_uuid = '" + bulletinBoard.getClan().getUUID() + "'", new SQLStorage.Callback<ResultSet>()
        {
            @Override
            public void onSuccess(ResultSet resultSet) throws SQLException
            {
                while (resultSet.next())
                {
                    Location savedLocation = LocationSerialization.deserializeLocation(resultSet.getString("location"));
                    BlockFace savedDirection = BlockFace.valueOf(resultSet.getString("direction"));

                    location = savedLocation;
                    direction = savedDirection;

                    update();
                }
            }

            @Override
            public void onFailure(Throwable cause)
            {
                cause.printStackTrace();
            }
        });
    }

    @Override
    public void save() throws IOException
    {
        ImageIO.write(image, "PNG", imageFile);

        Vowed.getSQLStorage().updateQuery("bulletinboard",
                new String[]{"clan_uuid", "location", "direction"},
                new String[]{bulletinBoard.getClan().getUUID().toString(), LocationSerialization.serializeLocation(location), direction.name()},
                new String[]{bulletinBoard.getClan().getUUID().toString(), LocationSerialization.serializeLocation(location), direction.name()},
                new SQLStorage.Callback<Integer>()
                {
                    @Override
                    public void onSuccess(Integer integer) throws SQLException
                    {

                    }

                    @Override
                    public void onFailure(Throwable cause)
                    {

                    }
                });


    }

    @Override
    public boolean place(Block block, BlockFace face, boolean isLoading)
    {
        this.location = block.getLocation();
        this.direction = face;

        int xMod = 0;
        int zMod = 0;

        switch (face)
        {
            case EAST:
                zMod = -1;
                break;
            case WEST:
                zMod = 1;
                break;
            case SOUTH:
                xMod = 1;
                break;
            case NORTH:
                xMod = -1;
                break;
            default:
                Vowed.LOG.severe("Someone tried to create an image with an invalid block facing");
                return false;
        }

        if (image == null)
        {
            Vowed.LOG.severe("Someone tried to create an image with an invalid file!");
            return false;
        }

        Block b = block.getRelative(face);

        int width = (int) Math.ceil((double) image.getWidth() / (double) MAP_WIDTH);
        int height = (int) Math.ceil((double) image.getHeight() / (double) MAP_HEIGHT);

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (!block.getRelative(x * xMod, -y, x * zMod).getType().isSolid())
                    return false;

                if (block.getRelative(x * xMod - zMod, -y, x * zMod + xMod).getType().isSolid())
                    return false;
            }
        }

        try
        {
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    setItemFrame(b.getRelative(x * xMod, -y, x * zMod), face, x * MAP_WIDTH, y * MAP_HEIGHT, isLoading);
                }
            }
        } catch (NullPointerException e)
        {
            // God forgive me, but I actually HAVE to catch this...
            Vowed.LOG.info("Some error occured while placing the ItemFrames. This can for example happen when some existing ItemFrame/Hanging Entity is blocking.");
            Vowed.LOG.info("Unfortunatly this is caused be the way Minecraft/CraftBukkit handles the spawning of Entities.");
            return false;
        }

        return true;
    }


    private void loadItemFrame(Block block, BlockFace direction, int x, int y)
    {

    }

    private void setItemFrame(Block bb, BlockFace face, int x, int y, boolean isLoading)
    {
        ItemFrame i = null;

        if (isLoading)
        {
            for (Entity entity : bb.getWorld().getNearbyEntities(bb.getLocation(), 1, 1, 1))
            {
                i = (ItemFrame) entity;
            }
        }
        else
        {
            i = bb.getWorld().spawn(bb.getLocation(), ItemFrame.class);
            i.setFacingDirection(face, false);
        }


        ItemStack item = getMapItem(x, y);
        i.setItem(item);

        short id = item.getDurability();

        maps.put(id, new ImageMap(imageFile, x, y, false));
    }

    @SuppressWarnings("deprecation")
    private ItemStack getMapItem(int x, int y)
    {
        ItemStack item = new ItemStack(Material.MAP);

        MapView map = Vowed.getPlugin().getServer().createMap(Vowed.getPlugin().getServer().getWorlds().get(0));
        for (MapRenderer r : map.getRenderers())
        {
            map.removeRenderer(r);
        }

        map.addRenderer(new ImageMapRenderer(image, x, y));

        item.setDurability(map.getId());

        return item;
    }

    public void update()
    {
        image = ImageFactory.getImage(bulletinBoard);
        try
        {
            ImageIO.write(image, "PNG", getImageFile());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        place(location.getBlock(), direction, true);
    }
}
