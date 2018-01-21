package net.vowed.items.menu.armour;

import net.vowed.api.items.Tier;
import net.vowed.api.items.armour.ArmourType;
import net.vowed.items.menu.BackItem;
import net.vowed.items.menu.ItemMenu;
import net.vowed.items.menu.generalGUI.ParentItemMenu;
import org.bukkit.ChatColor;

/**
 * Created by JPaul on 3/10/2016.
 */
public class ArmourMenu extends ItemMenu
{
    public ArmourMenu()
    {
        super(9, ChatColor.DARK_RED + "Armour");
    }

    @Override
    public void addItems(Tier tier)
    {
        for (int slot = 0; slot < ArmourType.values().length; slot++)
        {
            ArmourType armourType = ArmourType.values()[slot];
            addItem(slot, new ArmourItem(armourType, tier));
        }

        addItem(getSize() - 2, new BackItem(new ParentItemMenu()));
    }
}
