package net.vowed.api.wir;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Map;
import java.util.UUID;

/**
 * Created by JPaul on 8/25/2016.
 */
public interface IMapComponent
{
    IMapComponent newInstance();

    UUID getUUID();

    String getName();

    boolean isDoneProcessing();

    void setDoneProcessing(boolean done);

    Location getLocation();

    void setLocation(Location location);

    Location getMaximum();

    Location getMinimum();

    int getLength();

    int getWidth();

    int getHeight();

    int getRadius();

    int getFloorLevel();

    Map<Location, Tuple<Material, Byte>> getVirtualBlocks();

    void setVirtualBlocks(Map<Location, Tuple<Material, Byte>> virtualBlocks);

    void updateVirtualBlocks();

    boolean isPlaced();

    void loadInWorld();
}
