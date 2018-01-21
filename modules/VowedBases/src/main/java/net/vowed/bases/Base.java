package net.vowed.bases;

import net.vowed.api.bases.IBase;
import net.vowed.api.bases.IBaseComponent;
import net.vowed.api.clans.IClan;
import net.vowed.api.settings.bases.BaseSetting;
import net.vowed.core.util.schematics.Paster;
import org.bukkit.Location;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by JPaul on 8/10/2016.
 */
public class Base implements IBase
{
    private Location location;
    private IClan owner;
    private List<IBaseComponent> components = new LinkedList<>();

    public Base(Location location, IClan owner)
    {
        this.location = location;
        this.owner = owner;
    }

    public void create() throws Exception
    {

        Paster paster = null; // TODO: 8/25/2016  
        paster.pasteWithBuilders((File) BaseSetting.BASE_SCHEMATIC.getValue(), location, (builder) -> {

            if (builder.isSpawned())
            {
                builder.destroy();
            }
        });
    }

    @Override
    public String getName()
    {
        return owner.getName();
    }

    @Override
    public String getFormattedName()
    {
        return owner.getName() + "'s base";
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public IClan getOwner()
    {
        return owner;
    }

    @Override
    public List<IBaseComponent> getComponents()
    {
        return components;
    }

    public IBaseComponent getComponent(int upgradeStage)
    {
        return components.get(upgradeStage - 1);
    }
}
