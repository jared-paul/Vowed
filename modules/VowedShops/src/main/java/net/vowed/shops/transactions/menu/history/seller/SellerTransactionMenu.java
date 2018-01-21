package net.vowed.shops.transactions.menu.history.seller;

import com.google.common.collect.Maps;
import me.jpaul.menuapi.types.Menu;
import me.jpaul.menuapi.types.MultiMenu;
import me.jpaul.menuapi.types.Page;
import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.util.fetchers.NameFetcher;
import net.vowed.core.util.serialization.SerializationUtil;
import net.vowed.shops.transactions.Transaction;
import net.vowed.shops.transactions.menu.history.buyer.BuyerMenuItem;
import org.bukkit.ChatColor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by JPaul on 3/19/2016.
 */
public class SellerTransactionMenu extends MultiMenu
{
    public SellerTransactionMenu(UUID uuid, int pages)
    {
        super(54, ChatColor.YELLOW + "Seller Transactions", pages);

        Map<Integer, Menu> byPageNumber = Maps.newHashMap();

        Vowed.getSQLStorage().selectAllAsyncQuery("transaction_data",
                                                      "sellerID",
                                                      uuid.toString(),
                                                      new SQLStorage.Callback<ResultSet>()
                                                 {
                                                     @Override
                                                     public void onSuccess(ResultSet resultSet) throws SQLException
                                                     {
                                                         int index = 0;
                                                         int slot = 0;
                                                         int pageIndex = 0;

                                                         while (resultSet.next())
                                                         {
                                                             if (index >= 45)
                                                             {
                                                                 pageIndex++;

                                                                 int pageIndexINC = pageIndex + 1;

                                                                 Page page = new Page(54, ChatColor.YELLOW + "PAGE: " + pageIndexINC);
                                                                 byPageNumber.put(pageIndex, page);
                                                                 setMenu(pageIndex, page);
                                                                 index = 0;
                                                             }
                                                             if (slot >= 45)
                                                             {
                                                                 slot = 0;
                                                             }

                                                             Transaction transaction = (Transaction) SerializationUtil.fromBase64(resultSet.getString("transaction"));

                                                             if (byPageNumber.get(pageIndex) != null)
                                                             {
                                                                 byPageNumber.get(pageIndex).addItem(slot, new BuyerMenuItem(), transaction);
                                                             }
                                                             else
                                                             {
                                                                 addItem(slot, new SellerMenuItem(), transaction);
                                                             }

                                                             slot++;
                                                             index++;
                                                         }
                                                     }

                                                     @Override
                                                     public void onFailure(Throwable cause)
                                                     {
                                                         NameFetcher.getNameOfAsync(uuid)
                                                                 .syncLast(name ->
                                                                           {
                                                                               if (name != null && name.equals("") || name == null)
                                                                               {
                                                                                   Vowed.LOG.warning("MYSQL ERROR: Error loading transaction history");
                                                                                   cause.printStackTrace();
                                                                               }
                                                                               else
                                                                               {
                                                                                   Vowed.LOG.warning("MYSQL ERROR: Error loading " + name + "'s transaction history");
                                                                                   cause.printStackTrace();
                                                                               }
                                                                           })
                                                                 .execute();
                                                     }
                                                 });
    }
}

