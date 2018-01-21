package net.vowed.items.menu.armour;

import me.jpaul.menuapi.MenuAPI;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.api.items.Tier;
import net.vowed.api.items.armour.ArmourType;
import net.vowed.core.util.strings.Strings;
import net.vowed.items.menu.generalGUI.GeneralItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by JPaul on 4/4/2016.
 */
public class ArmourItem extends GeneralItem
{
    Tier tier;
    ArmourType armourType;

    public ArmourItem(ArmourType armourType, Tier tier)
    {
        this.armourType = armourType;
        this.tier = tier;
    }

    @Override
    public ItemStack getItem()
    {
        String name = Strings.capitalizeFirst(tier.name().toLowerCase());
        String number = name.substring(4);
        String fullName = name.substring(0, 4) + " " + number;

        switch (armourType)
        {
            case BOOTS:
                return new ItemBuilder(Material.LEATHER_BOOTS)
                        .setName(tier.getChatColour() + fullName + "Boots")
                        .setLore(ChatColor.GRAY + "Generate random " + fullName + " Boots")
                        .setLeatherColour(tier.getColour())
                        .getItem();
            case CHESTPLATE:
                return new ItemBuilder(Material.LEATHER_CHESTPLATE)
                        .setName(tier.getChatColour() + fullName + "Chestplate")
                        .setLore(ChatColor.GRAY + "Generate a random " + fullName + " Chestplate")
                        .setLeatherColour(tier.getColour())
                        .getItem();
            case HELMET:
                return new ItemBuilder(Material.LEATHER_HELMET)
                        .setName(tier.getChatColour() + fullName + "Helmet")
                        .setLore(ChatColor.GRAY + "Generate a random " + fullName + " Helmet")
                        .setLeatherColour(tier.getColour())
                        .getItem();
            case LEGGINGS:
                return new ItemBuilder(Material.LEATHER_LEGGINGS)
                        .setName(tier.getChatColour() + fullName + "Leggings")
                        .setLore(ChatColor.GRAY + "Generate random " + fullName + " Leggings")
                        .setLeatherColour(tier.getColour())
                        .getItem();
        }

        return null;
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();

        ArmourMenuController parentItemMenu = (ArmourMenuController) MenuAPI.getMenuManager().getMenu(player).getController();
        parentItemMenu.setArmourType(armourType);

        parentItemMenu.getNext(player).showToPlayer(player);
    }
}
