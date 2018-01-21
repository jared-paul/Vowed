package net.vowed.shops.methods;

import com.google.common.collect.Lists;
import me.jpaul.menuapi.types.Menu;
import me.jpaul.menuapi.types.MultiMenu;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.shops.IChestShop;
import net.vowed.api.shops.IShop;
import net.vowed.shops.ChestShop;
import net.vowed.shops.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 10/15/2015.
 */
public class ShopListener implements Listener
{
    private HashMap<UUID, IShop> isInShop = new HashMap<>();

    @EventHandler
    public void onCreateShop(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getHand() == EquipmentSlot.HAND)
        {
            if (player.isSneaking() && player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOOK)
            {
                if (Vowed.getShopRegistry().getShop(player.getUniqueId()) == null)
                {
                    Location location = event.getClickedBlock().getLocation();
                    IChestShop shop = Vowed.getShopRegistry().createShop(player.getUniqueId(), location.add(0, 1, 0));
                    shop.createShop();
                    shop.setOpen(false);
                    new NamingShopConversation(player, shop);
                }
                else
                {
                    player.sendMessage(ChatColor.RED + "You cannot create a shop while you already have one open!");
                }
            }
        }
    }


    @EventHandler
    public void onOpenShop(PlayerInteractEvent interactEvent)
    {
        Player player = interactEvent.getPlayer();
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        Action action = interactEvent.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK && interactEvent.getHand() == EquipmentSlot.HAND)
        {
            Block block = interactEvent.getClickedBlock();
            Location location = block.getLocation();

            if (Vowed.getShopRegistry().getShop(block.getLocation()) != null)
            {
                interactEvent.setCancelled(true);

                IChestShop shop = Vowed.getShopRegistry().getShop(location);
                MultiMenu menu = shop.getMenu(vowedPlayer);

                if (shop.isOpen())
                {
                    isInShop.put(player.getUniqueId(), shop);
                    menu.showToPlayer(player);
                }
                else if (shop.getOwner().equals(player.getUniqueId()))
                {
                    isInShop.put(player.getUniqueId(), shop);

                    Vowed.LOG.debug(menu.getMenu(0).getMenuItems().toString());
                    Vowed.LOG.severe(Arrays.toString(menu.getMenu(0).getInventory().getContents()));
                    Vowed.LOG.warning(menu.getMenuItems().toString());
                    Vowed.LOG.debug(shop.getContents().toString());
                    menu.showToPlayer(player);
                }
                else
                {
                    player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Sorry, this shop is closed");
                }
            }
        }
        else if (action == Action.LEFT_CLICK_BLOCK)
        {
            if (Vowed.getShopRegistry().getShop(interactEvent.getClickedBlock().getLocation()) == null)
            {
                interactEvent.setCancelled(true);

                IChestShop shop = Vowed.getShopRegistry().getShop(interactEvent.getClickedBlock().getLocation());
                shop.destroyShop(player);
            }
        }
    }

    @EventHandler
    public void on(InventoryCloseEvent closeEvent)
    {
        Player player = (Player) closeEvent.getPlayer();
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        Inventory inventory = closeEvent.getInventory();

        if (isInShop.containsKey(player.getUniqueId()))
        {
            IChestShop shop = (IChestShop) isInShop.get(player.getUniqueId());

            if (inventory.equals(shop.getMenu(vowedPlayer).getInventory()))
            {
                isInShop.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void on(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        ItemStack clickedItem = clickEvent.getCurrentItem();
        IChestShop shop = (IChestShop) isInShop.get(player.getUniqueId());

        if (shop != null)
        {
            if (clickEvent.isShiftClick())
            {//setting price
                if (clickEvent.getClickedInventory().equals(player.getInventory()) && shop.isOwner(player.getUniqueId()))
                {
                    for (Menu menu : shop.getMenu(vowedPlayer).getMenus())
                    {
                        if (menu.getInventory().equals(player.getOpenInventory().getTopInventory()))
                        {
                            player.closeInventory();

                            ShopItem shopItem = new ShopItem(clickedItem, shop);
                            new ApplyingPrice(vowedPlayer, shop, shopItem);

                            isInShop.put(player.getUniqueId(), shop);
                        }
                    }
                }
                else
                {
                    clickEvent.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent chatEvent)
    {
        Player player = chatEvent.getPlayer();
        //PlayerWrapper playerWrapper = VowedPlayers.getPlayerManager().getWrappedPlayer(player);
    }

    @EventHandler
    public void testingTransactionMenu(PlayerEggThrowEvent throwEvent)
    {
        Player player = throwEvent.getPlayer();

        ItemStack postCreator = new ItemStack(Material.BLAZE_ROD);

        ItemMeta meta = postCreator.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Spawn Point Selector");
        meta.setLore(Lists.newArrayList(ChatColor.GRAY + "Create a spawn point!"));
        postCreator.setItemMeta(meta);

        player.getInventory().addItem(postCreator);

        List<UUID> playerList = Lists.newArrayList();

        for (Player playerTest : Bukkit.getOnlinePlayers())
        {
            playerList.add(playerTest.getUniqueId());
        }
    }
}

