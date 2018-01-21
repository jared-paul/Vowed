package net.vowed.items.menu.weapon;

import me.jpaul.menuapi.MenuAPI;
import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.types.Menu;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.items.Tier;
import net.vowed.api.items.weapon.WeaponType;
import net.vowed.core.util.strings.Strings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 4/4/2016.
 */
public class WeaponItem implements MenuItem
{
    Tier tier;
    WeaponType weaponType;

    public WeaponItem(WeaponType weaponType, Tier tier)
    {
        this.weaponType = weaponType;
        this.tier = tier;
    }

    @Override
    public ItemStack getItem()
    {
        String name = Strings.capitalizeFirst(tier.name().toLowerCase());
        String number = name.substring(4);
        String fullName = name.substring(0, 4) + " " + number;

        switch (weaponType)
        {
            case AXE:
                return new ItemBuilder(WeaponType.getMatFromType(weaponType, tier))
                        .setName(tier.getChatColour() + fullName + " Axe")
                        .setLore(ChatColor.GRAY + "Generate a random " + fullName + " axe")
                        .getItem();
            case BOW:
                return new ItemBuilder(WeaponType.getMatFromType(weaponType, tier))
                        .setName(tier.getChatColour() + fullName + " Bow")
                        .setLore(ChatColor.GRAY + "Generate a random " + fullName + " bow")
                        .getItem();
            case STAFF:
                return new ItemBuilder(WeaponType.getMatFromType(weaponType, tier))
                        .setName(tier.getChatColour() + fullName + " Staff")
                        .setLore(ChatColor.GRAY + "Generate a random " + fullName + " staff")
                        .getItem();
            case SWORD:
                return new ItemBuilder(WeaponType.getMatFromType(weaponType, tier))
                        .setName(tier.getChatColour() + fullName + " Sword")
                        .setLore(ChatColor.GRAY + "Generate a random " + fullName + " sword")
                        .getItem();
        }

        return null;
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();

        Menu menu = MenuAPI.getMenuManager().getMenu(player);

        WeaponMenuController weaponMenuController = (WeaponMenuController) MenuAPI.getMenuManager().getMenu(player).getController();
        weaponMenuController.setWeaponType(weaponType);

        weaponMenuController.getNext(player).showToPlayer(player);
    }
}
