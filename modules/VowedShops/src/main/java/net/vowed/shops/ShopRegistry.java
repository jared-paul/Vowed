package net.vowed.shops;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.jpaul.menuapi.types.Menu;
import me.jpaul.menuapi.types.Page;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.player.races.RaceType;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.shops.IChestShop;
import net.vowed.api.shops.IShopRegistry;
import net.vowed.core.util.math.Integers;
import net.vowed.core.util.serialization.SerializationUtil;
import net.vowed.shops.transactions.Transaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by JPaul on 11/13/2015.
 */
public class ShopRegistry implements IShopRegistry
{
    //HashMap<Company, ChestShop> companyShop = Maps.newHashMap();

    public List<IChestShop> shops = Lists.newArrayList();

    public IChestShop createShop(UUID owner, Location location)
    {
        IChestShop shop = new ChestShop(owner, Bukkit.getPlayer(owner).getName(), location, ShopType.PLAYER);
        shops.add(shop);
        shop.addPage();
        return shop;
    }

    /*
    public ChestShop createShop(Player owner, Company company)
    {
        ChestShop shop = new ChestShop(owner, owner.getLabel(), company.getLocation(), ShopType.COMPANY);
        companyShop.put(company, shop);
        shops.add(shop);
        addPage(owner.getUniqueId());
        shop.addItem(new CloseButton(shop), 8);
        return shop;
    }
    */


    public IChestShop getShop(UUID owner)
    {
        for (IChestShop shop : shops)
        {
            if (shop.getOwner().equals(owner))
            {
                return shop;
            }
        }

        return null;
    }


    public IChestShop getShop(Location location)
    {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        Location shopLocation = new Location(location.getWorld(), x, y, z);

        for (IChestShop shop : shops)
        {
            if (shop.getBlocks().contains(shopLocation))
            {
                return shop;
            }
        }

        return null;
    }

    /*
    public ChestShop getShop(Company company)
    {
        if (companyShop.containsKey(company))
        {
            return companyShop.get(company);
        }

        return null;
    }
    */

    /*
    public boolean shopExists(Company company)
    {
        return companyShop.containsKey(company) && companyShop.get(company) != null;
    }
    */

    public void saveTransaction(IVowedPlayer buyer, IVowedPlayer seller, ShopItem shopItem)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss-a");

        int buyerBefore = (int) (buyer.getMoney() + getPrice(buyer, shopItem));
        int sellerBefore = (int) (seller.getMoney() - getPrice(seller, shopItem));

        UUID transactionID = UUID.randomUUID();
        UUID buyerID = buyer.getUUID();
        UUID sellerID = seller.getUUID();
        ItemStack item = shopItem.getItem();
        int dwarfPrice = shopItem.getPrice(RaceType.DWARF);
        int elfPrice = shopItem.getPrice(RaceType.ELF);
        int humanPrice = shopItem.getPrice(RaceType.HUMAN);
        int buyerMoneyAfter = Integers.roundUpPositive(buyer.getMoney());
        int sellerMoneyAfter = Integers.roundUpPositive(seller.getMoney());
        Date date = new Date();

        Transaction transaction = new Transaction(transactionID, buyerID, sellerID, item, dwarfPrice, elfPrice, humanPrice, buyerBefore, buyerMoneyAfter, sellerBefore, sellerMoneyAfter, date);

        Vowed.getSQLStorage().updateAsyncQuery("transaction_data",
                                                   new String[]{"id", "buyerID", "sellerID", "transaction"},
                                                   new String[]{transactionID.toString(), buyerID.toString(), sellerID.toString(), SerializationUtil.toBase64(transaction)},
                                                   new String[]{transactionID.toString(), buyerID.toString(), sellerID.toString(), SerializationUtil.toBase64(transaction)},
                                                   new SQLStorage.Callback<Integer>()
                                               {

                                                   public void onSuccess(Integer object) throws SQLException
                                                   {
                                                       Vowed.LOG.info("Saved transaction " + transactionID);
                                                   }


                                                   public void onFailure(Throwable cause)
                                                   {
                                                       Vowed.LOG.warning("MYSQL ERROR: Error saving transaction " + transactionID);
                                                       cause.printStackTrace();
                                                   }
                                               }
        );
    }

    public int getPrice(IVowedPlayer vowedPlayer, ShopItem shopItem)
    {
        return shopItem.getPrice(vowedPlayer.getRace());
    }

    //gets lowest size page
    public Page getLowestSize(List<Page> pages)
    {
        List<Integer> sizes = Lists.newArrayList();

        for (Page pageFinder : pages)
        {
            sizes.add(pageFinder.getSize());
        }

        Menu lowestSize = null;

        for (Page pageFinder : pages)
        {
            if (pageFinder.getSize() == Collections.min(sizes))
            {
                lowestSize = pageFinder;
            }
        }

        return (Page) lowestSize;
    }
}
