package net.vowed.wir;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.data.DataException;
import net.vowed.api.wir.IComponentContainer;
import net.vowed.api.wir.IMapComponent;
import org.bukkit.Location;

import java.io.IOException;
import java.util.List;

/**
 * Created by JPaul on 8/25/2016.
 */
public class ComponentContainer implements IComponentContainer
{
    List<IMapComponent> components = Lists.newArrayList();

    public ComponentContainer()
    {
    }

    @Override
    public List<IMapComponent> getComponents()
    {
        return components;
    }

    @Override
    public IMapComponent getComponent(String name)
    {
        for (IMapComponent component : components)
        {
            if (component.getName().equalsIgnoreCase(name))
            {
                return component.newInstance();
            }
        }

        return null;
    }

    @Override
    public void registerComponent(IMapComponent component)
    {
        components.add(component);
    }

    @Override
    public void loadInWorld(List<IMapComponent> components, Location location) throws IOException, DataException
    {
        for (IMapComponent component : components)
        {
            if (component instanceof RoomComponent)
            {

            }


            /*
            List<Block> blockList = Paster.paste(component.getSchematic(), location);

            Tuple tuple = findStartAndEnd(blockList);

            component.setEnd(tuple.getEnd());
            component.setStart(tuple.getStart());


            try
            {
                IMapComponent nextComponent = components.get(components.indexOf(component) + 1);

                if (nextComponent != null)
                {
                    Paster.rotate(component.getEnd().getDirection(), nextComponent.getSchematic());
                }
            }
            catch (IndexOutOfBoundsException ignored) {}


            location = component.getEnd().getLocation();
            */
        }
    }
}
