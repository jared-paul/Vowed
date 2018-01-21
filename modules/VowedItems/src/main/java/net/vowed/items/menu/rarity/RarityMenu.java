package net.vowed.items.menu.rarity;

import me.jpaul.menuapi.types.Menu;
import net.vowed.api.items.Rarity;
import net.vowed.items.menu.BackItem;
import net.vowed.items.menu.generalGUI.ParentItemMenu;
import org.bukkit.ChatColor;

/**
 * Created by JPaul on 3/8/2016.
 */
public class RarityMenu extends Menu
{
    public RarityMenu()
    {
        super(9, ChatColor.DARK_RED + "Rarity");

        addItem(0, new RarityItem(Rarity.COMMON));
        addItem(1, new RarityItem(Rarity.UNCOMMON));
        addItem(2, new RarityItem(Rarity.RARE));
        addItem(3, new RarityItem(Rarity.LEGENDARY));

        addItem(getSize() - 2, new BackItem(new ParentItemMenu()));
    }
}
