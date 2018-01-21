package net.vowed.items.menu.tier;

import me.jpaul.menuapi.types.Menu;
import net.vowed.api.items.Tier;
import net.vowed.items.menu.BackItem;
import net.vowed.items.menu.generalGUI.ParentItemMenu;
import org.bukkit.ChatColor;

/**
 * Created by JPaul on 3/8/2016.
 */
public class TierMenu extends Menu
{
    public TierMenu()
    {
        super(9, ChatColor.DARK_RED + "Tier");

        addItem(0, new TierItem(Tier.TIER1));
        addItem(1, new TierItem(Tier.TIER2));
        addItem(2, new TierItem(Tier.TIER3));
        addItem(3, new TierItem(Tier.TIER4));
        addItem(4, new TierItem(Tier.TIER5));
        addItem(8, new BackItem(new ParentItemMenu()));
    }
}
