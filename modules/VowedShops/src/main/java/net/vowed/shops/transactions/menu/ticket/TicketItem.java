package net.vowed.shops.transactions.menu.ticket;

import com.google.common.collect.Lists;
import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.types.Container;
import me.jpaul.menuapi.util.ItemBuilder;
import net.vowed.shops.transactions.Transaction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * Created by JPaul on 3/29/2016.
 */
public class TicketItem implements MenuItem, Container<Transaction>
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

        return new ItemBuilder(transaction.getItem()).getItem();
    }

    @Override
    public void onClick(InventoryClickEvent clickEvent)
    {

    }
}
