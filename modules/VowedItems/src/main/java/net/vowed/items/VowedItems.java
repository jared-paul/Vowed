package net.vowed.items;

import net.vowed.items.armour.generator.ArmourModifiers;
import net.vowed.items.shield.generator.ShieldModifiers;
import net.vowed.items.weapon.generator.WeaponModifiers;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

/**
 * Created by JPaul on 2017-02-02.
 */
public class VowedItems
{
    public void onEnable()
    {
        try
        {
            new Settings();
        } catch (IOException | InvalidConfigurationException e)
        {
            e.printStackTrace();
        }

        new ArmourModifiers().loadModifiers();
        new WeaponModifiers().loadModifiers();
        new ShieldModifiers().loadModifiers();
    }
}
