package net.vowed.wir.generation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.wir.Tuple;
import net.vowed.core.util.schematics.Paster;
import net.vowed.core.util.schematics.Schematic;
import net.vowed.wir.Map;
import net.vowed.wir.RoomComponent;
import net.vowed.wir.WiR;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JPaul on 9/20/2016.
 */
public class DecorationLoader implements Loader
{
    List<RoomComponent> rooms;
    Map map;

    boolean isDone = false;
    boolean isProcessing = false;

    public DecorationLoader(HallwayLoader hallwayLoader)
    {
        this.rooms = hallwayLoader.rooms;
        this.map = hallwayLoader.loaderSync.loaderAsync.map;
    }

    @Override
    public void load()
    {
        isProcessing = true;

        Vowed.LOG.debug("LOADING DECORATIONS");

        NPC starter = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Dungeon Starter");
        starter.spawn(map.getStart().getLocation().clone().add(0, 2, 0));

        NPC finisher = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Dungeon Finisher");
        finisher.spawn(map.getEnd().getLocation().clone().add(0, 2, 0));


        int midX = (map.getMinimumPoint().getBlockX() + map.getMaximumPoint().getBlockX()) / 2;
        int midZ = (map.getMinimumPoint().getBlockZ() + map.getMaximumPoint().getBlockZ()) / 2;

        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.setItemInHand(new ItemStack(Material.MAP));

            MapView mapView = Bukkit.createMap(player.getWorld());
            mapView.setScale(MapView.Scale.CLOSE);
            mapView.setCenterX(midX);
            mapView.setCenterZ(midZ);

            player.sendMap(mapView);
        }

        Widget.Size[] sizes = Widget.Size.values();

        for (RoomComponent room : rooms)
        {
            for (Widget.Size size : sizes)
            {
                Vowed.LOG.debug(size.name());

                List<Widget> widgets = WiR.getWidgetContainer().getWidgets(size);
                Collections.shuffle(widgets);

                for (Widget widget : widgets)
                {
                    paste(widget, room);
                }
            }
        }

        for (RoomComponent room : RoomUtil.getMainRooms(rooms))
        {
            room.getLocation().getBlock().getRelative(BlockFace.UP).setType(Material.BEACON);
        }

        isDone = true;
        isProcessing = false;
    }

    public void paste(Widget widget, RoomComponent room)
    {
        Location suitablePosition = findSuitablePosition(widget, room);

        if (suitablePosition != null)
        {
            Paster.paste(widget.getSchematic(), suitablePosition);
        }
    }

    public boolean canFit(RoomComponent room, Widget widget, Location location, Set<Location> locations)
    {
        if (widget.isWall() && !room.isBesideWall(location))
        {
            return false;
        }

        List<Location> outerRing = RoomUtil.getOuterRing(room);

        if (room.getFloorLevel() + 1 == location.getBlockY() && !room.isBesideDoorway(location))
        {
            Schematic schematic = Paster.getBlockData(widget.getSchematic(), location);
            java.util.Map<Location, Tuple<Material, Byte>> blockData = schematic.getBlockData();

            Set<Location> schematicLocations = blockData.keySet();

            for (Location comparerLocation : schematicLocations)
            {
                if (room.isBesideDoorway(comparerLocation))
                    return false;

                if (outerRing.contains(comparerLocation))
                    return false;

                if (comparerLocation.getBlock().getType() == Material.AIR || (comparerLocation.getBlock().getType() == Material.SMOOTH_BRICK && comparerLocation.getBlockY() == room.getFloorLevel()))
                {
                    if (comparerLocation.getBlockY() > room.getFloorLevel() && !locations.contains(comparerLocation))
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    public Location findSuitablePosition(Widget widget, RoomComponent room)
    {
        Paster.rotate(DIRECTIONS.get(ThreadLocalRandom.current().nextInt(DIRECTIONS.size())), widget.getSchematic());

        Location min = room.getMinimum();
        Location max = room.getMaximum();

        Set<Location> locations = Sets.newHashSet();

        for (int x = min.getBlockX(); x < max.getBlockX(); x++)
        {
            for (int y = min.getBlockY(); y < max.getBlockY(); y++)
            {
                for (int z = min.getBlockZ(); z < max.getBlockZ(); z++)
                {
                    locations.add(new Location(max.getWorld(), x, y, z));
                }
            }
        }

        for (Location location : locations)
        {
            if (!canFit(room, widget, location, locations))
            {
                continue;
            }

            return location;
        }

        return null;
    }

    private final List<BlockFace> DIRECTIONS = Lists.newArrayList(BlockFace.NORTH, BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH);

    @Override
    public boolean isProcessing()
    {
        return isProcessing;
    }

    @Override
    public boolean isDone()
    {
        return isDone;
    }
}
