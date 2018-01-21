package net.vowed.shops.transactions.menu.ticket;

import me.jpaul.menuapi.types.Menu;
import net.vowed.shops.transactions.Transaction;

/**
 * Created by JPaul on 3/29/2016.
 */
public class TicketMenu extends Menu
{
    public TicketMenu(String name, Transaction transaction)
    {
        super(45, name);

        //the middle slot
        addItem(44 / 2, new TicketItem(), transaction);
    }
}
