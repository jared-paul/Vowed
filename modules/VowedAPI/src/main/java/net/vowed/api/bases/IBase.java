package net.vowed.api.bases;

import net.vowed.api.clans.IClan;
import org.bukkit.Location;

import java.util.List;

/**
 * Created by JPaul on 8/12/2016.
 */
public interface IBase
{
    String getName();

    String getFormattedName();

    Location getLocation();

    IClan getOwner();

    List<IBaseComponent> getComponents();
}
