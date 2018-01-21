package net.vowed.wir;

import com.google.common.collect.Maps;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by JPaul on 9/30/2016.
 */
public class PlayerManager
{
    private final static java.util.Map<UUID, Map> mapContainer = Maps.newHashMap();

    public static Map getMap(Player player)
    {
        return getMap(player.getUniqueId());
    }

    public static Map getMap(UUID playerUUID)
    {
        return mapContainer.get(playerUUID);
    }

    public static void setMap(Player player, Map map)
    {
        setMap(player.getUniqueId(), map);
    }

    public static void setMap(UUID playerUUID, Map map)
    {
        mapContainer.put(playerUUID, map);
    }
}
