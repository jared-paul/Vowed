package net.vowed.shops;

import com.google.common.collect.Lists;
import me.jpaul.menuapi.execution.OnChat;
import me.jpaul.menuapi.items.MenuItem;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.player.races.RaceType;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.shops.IChestShop;
import net.vowed.core.money.MoneyUtil;
import net.vowed.core.storage.AttributeStorage;
import net.vowed.core.util.math.Integers;
import net.vowed.core.util.strings.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 11/12/2015.
 */
public class ShopItem implements MenuItem, OnChat, Serializable
{
    private static final UUID shopItemUUID = UUID.fromString("9804bcee-978a-4e58-817e-3116e9038203");

    private ItemStack item;
    private transient IChestShop shop;

    private BigDecimal dwarfPrice;
    private BigDecimal elfPrice;
    private BigDecimal humanPrice;
    private int test;

    private ItemMeta oldMeta;

    private UUID uuid;

    public ShopItem(ItemStack item, IChestShop shop)
    {
        this.oldMeta = item.getItemMeta();
        this.shop = shop;
        this.uuid = UUID.randomUUID();

        AttributeStorage attributeStorage = AttributeStorage.newTarget(item, shopItemUUID);
        attributeStorage.setData(shopItemUUID.toString());

        this.item = attributeStorage.getTarget();
    }

    @Override
    public ItemStack getItem()
    {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();
        IVowedPlayer vowedPlayer = Vowed.getPlayerRegistry().getVowedPlayer(player);
        ItemStack clickedItem = clickEvent.getCurrentItem();

        if (clickEvent.isShiftClick())
        {
            shop.getMenu(vowedPlayer).getMenus().stream().filter(menu -> menu.getInventory().equals(clickEvent.getClickedInventory())).forEach(menu ->
            {
                if (!shop.getOwner().equals(player.getUniqueId())) //buying item
                {
                    IVowedPlayer buyer = Vowed.getPlayerRegistry().getVowedPlayer(player);
                    IVowedPlayer seller = Vowed.getPlayerRegistry().getVowedPlayer(shop.getOwner());

                    if (buyer.getMoney() >= getPrice(buyer.getRace()))
                    {
                        buyer.subtractMoney(getPrice(buyer.getRace()));
                        seller.addMoney(getPrice(seller.getRace()));

                        shop.removeEditedItem(this);

                        resetItem();
                        buyer.getPlayer().getInventory().addItem(item);
                    }
                }
                else if (shop.getOwner().equals(player.getUniqueId())) //returning item
                {
                    if (!shop.isOpen())
                    {
                        shop.removeEditedItem(this);
                        resetItem();
                        player.getInventory().addItem(item);
                    }
                }
            });
        }
        else if (clickEvent.isRightClick())
        {
            shop.getMenu(vowedPlayer).getMenus()
                    .stream()
                    .filter(menu -> menu.getInventory().equals(clickEvent.getClickedInventory())).forEach(menu ->
            {
                if (shop.getOwner().equals(player.getUniqueId())) //editing price
                {
                    item = clickedItem;
                    resetItem();
                    player.closeInventory();
                    player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Please enter the new price");

                }
            });
        }
    }

    @Override
    public void onChat(AsyncPlayerChatEvent chatEvent, OnChat.Callback callback)
    {
        Player player = chatEvent.getPlayer();
        String message = chatEvent.getMessage();

        if (Integers.isInteger(message))
        {
            addPrice(Integer.parseInt(message), shop);
            shop.getMenu(Vowed.getPlayerRegistry().getVowedPlayer(player)).showToPlayer(player);

            callback.onSuccess();
        }
        else
        {
            player.sendMessage(Strings.handleError("Please type in a valid integer or type cancel", "valid", "integer", "cancel"));
        }
    }

    public void addPrice(int price, IChestShop shop)
    {
        RaceType race = Vowed.getPlayerRegistry().getVowedPlayer(shop.getOwner()).getRace();

        this.dwarfPrice = MoneyUtil.convertToRace(race, RaceType.DWARF, price);
        this.elfPrice = MoneyUtil.convertToRace(race, RaceType.ELF, price);
        this.humanPrice = MoneyUtil.convertToRace(race, RaceType.HUMAN, price);

        String dwarfStringPrice = ChatColor.RESET.toString() +
                ChatColor.DARK_RED +
                ChatColor.BOLD +
                "Dwarf " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + dwarfPrice.intValue() + "$";

        String elfStringPrice = ChatColor.RESET.toString() +
                ChatColor.DARK_GREEN +
                ChatColor.BOLD +
                "Elf " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + elfPrice.intValue() + "$";

        String humanStringPrice = ChatColor.RESET.toString() +
                ChatColor.DARK_AQUA +
                ChatColor.BOLD +
                "Human " +
                ChatColor.GREEN +
                "Price: " + ChatColor.RESET + humanPrice.intValue() + "$";

        ItemMeta meta = item.getItemMeta();
        List<String> lore = Lists.newArrayList();

        if (meta.getLore() == null)
        {
            lore.add(ChatColor.GRAY + "---------------------");
            lore.add(dwarfStringPrice);
            lore.add(elfStringPrice);
            lore.add(humanStringPrice);
        }
        else
        {
            lore = meta.getLore();
            lore.add(ChatColor.GRAY + "---------------------");
            lore.add(dwarfStringPrice);
            lore.add(elfStringPrice);
            lore.add(humanStringPrice);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void resetItem()
    {
        item.setItemMeta(oldMeta);
    }

    public int getPrice(RaceType raceType)
    {
        refreshPrice();

        switch (raceType)
        {
            case DWARF:
                return dwarfPrice.intValue();
            case ELF:
                return elfPrice.intValue();
            case HUMAN:
                return humanPrice.intValue();
        }

        return Integer.parseInt(null); //want to throw exception
    }

    public void refreshPrice()
    {
        if (dwarfPrice == null || elfPrice == null || humanPrice == null)
        {
            if (isOldShopItem(item))
            {
                List<String> lore = item.getItemMeta().getLore();
                for (String line : lore)
                {
                    String lowercase = ChatColor.stripColor(line.toLowerCase());

                    try
                    {
                        BigDecimal lineINT = BigDecimal.valueOf(Integer.parseInt(lowercase.replaceAll("[\\D]", "")));

                        if (lowercase.contains("elf price"))
                        {
                            if (elfPrice == null)
                            {
                                elfPrice = lineINT;
                            }
                        }
                        else if (lowercase.contains("dwarf price"))
                        {
                            if (dwarfPrice == null)
                            {
                                dwarfPrice = lineINT;
                            }
                        }
                        else if (lowercase.contains("human price"))
                        {
                            if (humanPrice == null)
                            {
                                humanPrice = lineINT;
                            }
                        }
                    } catch (Exception ignored)
                    {
                    }
                }
            }
        }
    }

    public ShopItemDaughter getRaceInstance(RaceType race)
    {
        return new ShopItemDaughter(clone(), race);
    }

    public UUID getUUID()
    {
        return uuid;
    }

    public static boolean isOldShopItem(ItemStack item)
    {
        AttributeStorage storage = AttributeStorage.newTarget(item, shopItemUUID);

        if (storage.hasData() && storage.getData(null).equals(shopItemUUID.toString()))
        {
            return true;
        }

        return false;
    }

    @Override
    protected ShopItem clone()
    {
        try
        {
            return (ShopItem) super.clone();
        } catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static ShopItem newInstance(ItemStack item, ChestShop shop)
    {
        if (item != null && item.getType() != Material.AIR)
        {
            return new ShopItem(item, shop);
        }

        return null;
    }
}
