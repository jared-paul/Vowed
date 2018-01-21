package net.vowed.api.wir;

import com.sk89q.worldedit.world.DataException;
import org.bukkit.Location;

import java.io.IOException;
import java.util.List;

/**
 * Created by JPaul on 8/25/2016.
 */
public interface IComponentContainer
{
    List<IMapComponent> getComponents();

    IMapComponent getComponent(String name);

    void registerComponent(IMapComponent component);

    void loadInWorld(List<IMapComponent> components, Location location) throws IOException, DataException;
}
