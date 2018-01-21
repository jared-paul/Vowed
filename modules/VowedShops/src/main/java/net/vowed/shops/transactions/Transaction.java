package net.vowed.shops.transactions;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by JPaul on 3/19/2016.
 */
public class Transaction implements Serializable
{
    private UUID id;
    private UUID buyer;
    private UUID seller;
    private ItemStack item;
    private int dwarfPrice;
    private int elfPrice;
    private int humanPrice;
    private int buyerMoneyAfter;
    private int buyerMoneyBefore;
    private int sellerMoneyAfter;
    private int sellerMoneyBefore;
    private Date date;

    private static final long serialVersionUID = 42L;

    public Transaction(UUID id, UUID buyer, UUID seller, ItemStack item, int dwarfPrice, int elfPrice, int humanPrice, int buyerMoneyAfter, int buyerMoneyBefore, int sellerMoneyAfter, int sellerMoneyBefore, Date date)
    {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.item = item;
        this.dwarfPrice = dwarfPrice;
        this.elfPrice = elfPrice;
        this.humanPrice = humanPrice;
        this.buyerMoneyAfter = buyerMoneyAfter;
        this.buyerMoneyBefore = buyerMoneyBefore;
        this.sellerMoneyAfter = sellerMoneyAfter;
        this.sellerMoneyBefore = sellerMoneyBefore;
        this.date = date;
    }

    public Transaction()
    {
    }

    public void setID(UUID id)
    {
        this.id = id;
    }

    public UUID getID()
    {
        return id;
    }

    public void setBuyer(UUID buyer)
    {
        this.buyer = buyer;
    }

    public UUID getBuyer()
    {
        return buyer;
    }

    public void setSeller(UUID seller)
    {
        this.seller = seller;
    }

    public UUID getSeller()
    {
        return seller;
    }

    public void setItem(ItemStack item)
    {
        this.item = item;
    }

    public ItemStack getItem()
    {
        return item;
    }

    public void setDwarfPrice(int dwarfPrice)
    {
        this.dwarfPrice = dwarfPrice;
    }

    public int getDwarfPrice()
    {
        return dwarfPrice;
    }

    public void setElfPrice(int elfPrice)
    {
        this.elfPrice = elfPrice;
    }

    public int getElfPrice()
    {
        return elfPrice;
    }

    public void setHumanPrice(int humanPrice)
    {
        this.humanPrice = humanPrice;
    }

    public int getHumanPrice()
    {
        return humanPrice;
    }

    public void setBuyerMoneyBefore(int buyerMoneyBefore)
    {
        this.buyerMoneyBefore = buyerMoneyBefore;
    }

    public int getBuyerMoneyBefore()
    {
        return buyerMoneyBefore;
    }

    public void setBuyerMoneyAfter(int buyerMoneyAfter)
    {
        this.buyerMoneyAfter = buyerMoneyAfter;
    }

    public int getBuyerMoneyAfter()
    {
        return buyerMoneyAfter;
    }

    public void setSellerMoneyBefore(int sellerMoneyBefore)
    {
        this.sellerMoneyBefore = sellerMoneyBefore;
    }

    public int getSellerMoneyBefore()
    {
        return sellerMoneyBefore;
    }

    public void setSellerMoneyAfter(int sellerMoneyAfter)
    {
        this.sellerMoneyAfter = sellerMoneyAfter;
    }

    public int getSellerMoneyAfter()
    {
        return sellerMoneyAfter;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Date getDate()
    {
        return date;
    }
}
