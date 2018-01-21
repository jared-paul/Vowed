package net.vowed.items.menu.tier;

import me.jpaul.menuapi.MenuAPI;
import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.types.MenuController;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.items.Tier;
import net.vowed.core.util.strings.Strings;
import net.vowed.items.menu.ItemMenuController;
import net.vowed.items.menu.rarity.RarityMenu;
import net.vowed.items.menu.shield.ShieldMenuController;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 4/4/2016.
 */
public class TierItem implements MenuItem
{
    protected Tier tier;

    public TierItem(Tier tier)
    {
        this.tier = tier;
    }

    @Override
    public ItemStack getItem()
    {
        String name = Strings.capitalizeFirst(tier.name().toLowerCase());
        String number = name.substring(4);
        String fullName = name.substring(0, 4) + " " + number;

        return new ItemBuilder(Material.WOOL)
                .setName(tier.getChatColour() + fullName)
                .setData(tier.getDyeColour().getWoolData())
                .getItem();
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();

        MenuController controller = MenuAPI.getMenuManager().getMenu(player).getController();

        if (controller instanceof ItemMenuController)
        {
            ItemMenuController itemMenuController = (ItemMenuController) controller;
            itemMenuController.setTier(tier);
            itemMenuController.getItemMenu().addItems(tier);


            itemMenuController.getNext(player).showToPlayer(player);
        }
        else if (controller instanceof ShieldMenuController)
        {
            ((ShieldMenuController) controller).setTier(tier);
            RarityMenu rarityMenu = (RarityMenu) controller.getNext(player);
            rarityMenu.showToPlayer(player);
        }

        /*
        if (parent instanceof ArmourMenuController)
        {
            ((ArmourMenuController) parent).setTier(tier);

            ArmourMenu armourMenu = new ArmourMenu();
            armourMenu.addItems(tier);

            armourMenu.showToPlayer(player);
        }
        else if (parent instanceof WeaponMenuController)
        {
            ((WeaponMenuController) parent).setTier(tier);

            WeaponMenu weaponMenu = new WeaponMenu();
            weaponMenu.addItems(tier);

            weaponMenu.showToPlayer(player);
        }
        else if (parent instanceof ShieldMenuController)
        {
            ((ShieldMenuController) parent).setTier(tier);

            RarityMenu rarityMenu = (RarityMenu) ((ShieldMenuController) parent).getNext(player);
            rarityMenu.showToPlayer(player);
        }
        */
    }
}
