package net.vowed.wir.listeners;

import net.vowed.api.plugin.Vowed;
import net.vowed.wir.Map;
import net.vowed.wir.PlayerManager;
import net.vowed.wir.RoomComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;

/**
 * Created by JPaul on 9/26/2016.
 */
public class OnPlayerMove implements Listener
{


    @EventHandler
    public void onMove(PlayerMoveEvent moveEvent)
    {
        Player player = moveEvent.getPlayer();
        Map map = PlayerManager.getMap(player);

        if (map != null)
        {
            for (RoomComponent room : map.getRoomComponents())
            {
                if (isAtEntrance(room, player) && !room.hasSpawnedMobs())
                {
                    Vowed.LOG.severe("SPAWNING MOBS");
                    room.spawnMobs();
                }
            }
        }
        else
        {
        }
    }

    private boolean isAtEntrance(RoomComponent room, Player player)
    {
        for (Location doorway : room.getAllDoorways())
        {
            if (doorway.distanceSquared(player.getLocation()) <= 81)
            {
                return true;
            }
        }

        return false;
    }

    @EventHandler
    public void onMap(MapInitializeEvent initializeEvent)
    {
        initializeEvent.getMap().setScale(MapView.Scale.CLOSE);
    }

    public static void refreshMap(Player player) {
        ItemStack item = player.getItemInHand();
        if (item.getType() == Material.MAP)
        {
        }
    }
}
