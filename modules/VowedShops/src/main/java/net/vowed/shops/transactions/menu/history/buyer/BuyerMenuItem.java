package net.vowed.shops.transactions.menu.history.buyer;

import com.google.common.collect.Lists;
import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.types.Container;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.core.util.fetchers.NameFetcher;
import net.vowed.shops.transactions.Transaction;
import net.vowed.shops.transactions.menu.ticket.TicketMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

/**
 * Created by JPaul on 3/19/2016.
 */
public class BuyerMenuItem implements MenuItem, Container<Transaction>
{
    Transaction transaction;

    @Override
    public void setData(Transaction transaction)
    {
        this.transaction = transaction;
    }

    @Override
    public ItemStack getItem()
    {
        List<String> lore = Lists.newArrayList();

        NameFetcher.getNameOfAsync(transaction.getBuyer())
                .syncLast(name -> lore.add("Buyer: " + ChatColor.RED + name))
                .execute();

        NameFetcher.getNameOfAsync(transaction.getSeller())
                .syncLast(name -> lore.add("Seller: " + ChatColor.RED + name))
                .execute();

        lore.add("");

        if (transaction.getItem().getItemMeta() != null)
        {
            if (!Objects.equals(transaction.getItem().getItemMeta().getDisplayName(), null))
            {
                lore.add(transaction.getItem().getItemMeta().getDisplayName());
            }
            else
            {
                lore.add(transaction.getItem().getType().name());
            }

            if (transaction.getItem().getItemMeta().getLore() != null)
            {
                for (String loreLine : transaction.getItem().getItemMeta().getLore())
                {
                    lore.add(loreLine);
                }
            }
        }

        lore.add("");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        lore.add("Date: " + ChatColor.RED + dateFormat.format(transaction.getDate()) + " PST");

        List<String> realLore = Lists.newArrayList();

        for (String line : lore)
        {
            realLore.add(ChatColor.RESET + line);
        }


        return new ItemBuilder(transaction.getItem().getType()).setName(ChatColor.RESET + transaction.getID().toString()).setLore(realLore).getItem();
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {
        Player player = (Player) clickEvent.getWhoClicked();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

        String date = ChatColor.YELLOW + dateFormat.format(transaction.getDate()) + " PST";

        new TicketMenu(date, transaction).showToPlayer(player);
    }
}
