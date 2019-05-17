package net.vowed.core.util.schematics;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.adapter.impl.Spigot_v1_13_R1;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.util.io.file.FilenameException;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.wir.Tuple;
import net.vowed.core.util.reflection.ReflectionUtil;
import net.vowed.core.util.schematics.builder.BuilderRunnable;
import net.vowed.core.util.schematics.builder.Coords;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by JPaul on 8/11/2016.
 */
public class Paster
{
    public static String getCardinalDirection(Player player)
    {
        CuboidRegion
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D)
        {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 45.0D))
        {
            return "W";
        }
        if ((45.0D <= rotation) && (rotation < 135.0D))
        {
            return "N";
        }
        if ((135.0D <= rotation) && (rotation < 225.0D))
        {
            return "E";
        }
        if ((225.0D <= rotation) && (rotation < 315.0D))
        {
            return "S";
        }
        if ((315.0D <= rotation) && (rotation < 360.0D))
        {
            return "W";
        }
        return null;
    }

    public static void rotate(BlockFace blockFace, File schemFile)
    {
        if (blockFace == BlockFace.WEST)
        {
            try
            {
                saveSchematic(schemFile, 180);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (blockFace == BlockFace.NORTH)
        {
            try
            {
                saveSchematic(schemFile, 270);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (blockFace == BlockFace.EAST)
        {
            try
            {
                saveSchematic(schemFile, 0);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (blockFace == BlockFace.SOUTH)
        {
            try
            {
                saveSchematic(schemFile, 90);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private static void saveSchematic(File saveFile, int rotateAngle)
            throws MaxChangedBlocksException, EmptyClipboardException, FilenameException, DataException, IOException
    {
        CuboidClipboard cuboidClipboard = SchematicFormat.MCEDIT.load(saveFile);
        if (rotateAngle != 0)
        {
            cuboidClipboard.rotate2D(rotateAngle);
        }

        SchematicFormat.MCEDIT.save(cuboidClipboard, saveFile);
    }


    public void pasteWithBuilders(File file, org.bukkit.Location location, BuilderRunnable.BuilderCallBack onceDone) throws Exception
    {
        location.add(0, 1, 0);

        CuboidClipboard clipboard = CuboidClipboard.loadSchematic(file);
        BaseBlock[][][] data = (BaseBlock[][][]) ReflectionUtil.getPrivateField(clipboard, "data");

        List<Coords> coords = Lists.newArrayList();

        for (int x = 0; x < clipboard.getSize().getBlockX(); ++x)
        {
            for (int y = 0; y < clipboard.getSize().getBlockY(); ++y)
            {
                for (int z = 0; z < clipboard.getSize().getBlockZ(); ++z)
                {
                    BaseBlock block = data[x][y][z];

                    org.bukkit.Location blockLocation = new org.bukkit.Location(location.getWorld(), x, y, z);

                    if (block != null && (!block.isAir()) && blockLocation.getBlock().getTypeId() != block.getId())
                    {
                        coords.add(new Coords(x, y, z, block));
                    }
                }
            }
        }

        //shuffles the block order
        Collections.shuffle(coords);

        //reorders the blocks by layer, but still in a random order, not linearly
        Collections.sort(coords);

        Queue<Coords> coordQueue = Lists.newLinkedList(coords);

        List<NPC> builders = Lists.newArrayList();

        for (int i = 0; i < 2; i++)
        {
            NPC builder = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Bob_The_Builder");
            builders.add(builder);

            builder.getNavigator().getLocalParameters().stuckAction((npc, navigator) -> {
                if (!npc.isSpawned())
                {
                    return false;
                }

                org.bukkit.Location base = navigator.getTargetAsLocation();
                npc.getEntity().teleport(base);

                return false;
            });
            builder.getNavigator().getLocalParameters().stationaryTicks(10);
            builder.getNavigator().getLocalParameters().speedModifier(2.5F);
            builder.getNavigator().getLocalParameters().useNewPathfinder(true);
            builder.getNavigator().getLocalParameters().updatePathRate(5);

            builder.spawn(location);

            new BuilderRunnable(clipboard.getOffset(), location, coordQueue, builder, file, onceDone).runTask(Vowed.getPlugin());
        }
    }

    public static CuboidClipboard pasteSchematic(File file, org.bukkit.Location location)
    {
        Vowed.LOG.debug(location.toString());

        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), Integer.MAX_VALUE);
        session.enableQueue();
        try
        {
            try
            {
                SchematicFormat schematic = SchematicFormat.getFormat(file);
                CuboidClipboard clipboard = schematic.load(file);
                clipboard.paste(session, BukkitUtil.toVector(location), true);
                return clipboard;
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
        catch (IOException | DataException | MaxChangedBlocksException e)
        {
            e.printStackTrace();
        }
        session.flushQueue();

        return null;
    }

    public static void paste(File schematicFile, Location location)
    {
        Schematic schematic = loadSchematic(schematicFile, location);
        paste(schematic);
    }

    public static void paste(Schematic schematic)
    {
        for (Map.Entry<Location, Tuple<Material, Byte>> dataEntry : schematic.getBlockData().entrySet())
        {
            Location location = dataEntry.getKey();
            Material material = dataEntry.getValue().getA();
            byte data = dataEntry.getValue().getB();

            location.getBlock().setTypeIdAndData(material.getId(), data, true);
        }
    }

    public static Schematic loadSchematic(File schematicFile, Location location)
    {
        return getBlockData(schematicFile, location);
    }

    public static Schematic getBlockData(File schematicFile, org.bukkit.Location location)
    {
        Map<Location, Tuple<Material, Byte>> all = Maps.newHashMap();
        int height = -1;
        int length = -1;
        int width = -1;

        try
        {
            SchematicFormat schematic = SchematicFormat.getFormat(schematicFile);
            CuboidClipboard clipboard = schematic.load(schematicFile);
            BaseBlock[][][] data = (BaseBlock[][][]) ReflectionUtil.getPrivateField(clipboard, "data");

            for (int x = 0; x < clipboard.getSize().getBlockX(); x++)
            {
                for (int y = 0; y < clipboard.getSize().getBlockY(); y++)
                {
                    for (int z = 0; z < clipboard.getSize().getBlockZ(); z++)
                    {
                        BaseBlock blockData = data[x][y][z];
                        Material material = Material.getMaterial(blockData.getId());
                        byte materialData = (byte) blockData.getData();

                        BlockVector blockVector = new com.sk89q.worldedit.Vector(x, y, z).add(new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ()).add(clipboard.getOffset())).toBlockPoint();
                        org.bukkit.Location blockLocation = new org.bukkit.Location(location.getWorld(), blockVector.getX(), blockVector.getY(), blockVector.getZ());

                        all.put(blockLocation, new Tuple<>(material, materialData));
                    }
                }
            }

            height = clipboard.getHeight();
            length = clipboard.getLength();
            width = clipboard.getWidth();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return new Schematic(all, width, length, height);
    }
}
