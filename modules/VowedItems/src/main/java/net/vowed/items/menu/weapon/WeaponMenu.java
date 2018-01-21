package net.vowed.items.menu.weapon;

import net.vowed.api.items.Tier;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.items.menu.BackItem;
import net.vowed.items.menu.ItemMenu;
import net.vowed.items.menu.generalGUI.ParentItemMenu;
import org.bukkit.ChatColor;

/**
 * Created by JPaul on 3/10/2016.
 */
public class WeaponMenu extends ItemMenu
{
    public WeaponMenu()
    {
        super(9, ChatColor.DARK_RED + "Weapon");
    }

    @Override
    public void addItems(Tier tier)
    {
        for (int slot = 0; slot < WeaponType.values().length; slot++)
        {
            WeaponType weaponType = WeaponType.values()[slot];
            addItem(slot, new WeaponItem(weaponType, tier));
        }

        addItem(getSize() - 2, new BackItem(new ParentItemMenu()));
    }
}
