package net.vowed.items.menu.weapon;

import me.jpaul.menuapi.types.Menu;
import net.vowed.api.items.Rarity;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.api.plugin.Vowed;
import net.vowed.items.menu.ItemMenu;
import net.vowed.items.menu.ItemMenuController;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by JPaul on 4/4/2016.
 */
public class WeaponMenuController extends ItemMenuController
{
    private WeaponType weaponType;

    public WeaponMenuController(List<Menu> menus)
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
        return Vowed.getItemFactory().createWeapon(rarity, tier, weaponType);
    }

    public WeaponType getWeaponType()
    {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType)
    {
        this.weaponType = weaponType;
    }
}
