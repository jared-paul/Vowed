package net.vowed.items.menu.armour;

import me.jpaul.menuapi.types.Menu;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.armour.ArmourType;
import net.vowed.api.plugin.Vowed;
import net.vowed.items.menu.ItemMenu;
import net.vowed.items.menu.ItemMenuController;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by JPaul on 4/4/2016.
 */
public class ArmourMenuController extends ItemMenuController
{
    private ArmourType armourType;

    public ArmourMenuController(List<Menu> menus)
    {
        super(menus);
    }

    @Override
    public ItemMenu getItemMenu()
    {
        return (ItemMenu) menus.get(1);
    }

    @Override
    public ItemStack getItem(Rarity rarity)
    {
        return Vowed.getItemFactory().createArmour(rarity, tier, armourType);
    }

    public ArmourType getArmourType()
    {
        return armourType;
    }

    public void setArmourType(ArmourType armourType)
    {
        this.armourType = armourType;
    }
}
