package net.vowed.core.util.serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

/**
 * Created by JPaul on 4/7/2016.
 */
public class LocationSerialization
{
    public static String serializeLocation(Location loc)
    { //Converts location -> String
        return loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getWorld().getUID();
        //feel free to use something to split them other than semicolons (Don't use periods or numbers)
    }

    public static Location deserializeLocation(String s)
    {//Converts String -> MobUtil
        String[] parts = s.split(";"); //If you changed the semicolon you must change it here too
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        UUID u = UUID.fromString(parts[3]);
        World w = Bukkit.getServer().getWorld(u);
        return new Location(w, x, y, z); //can return null if the world no longer exists
    }
}
