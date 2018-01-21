package net.vowed.items.armour.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

/**
 * CREATED BY ROJOSS https://gist.github.com/Rojoss/7a3172e410b47c9cdaad
 */
public class ArmourEquipEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private ItemStack item;
    private int slot;
    boolean cancelled = false;

    public ArmourEquipEvent(Player who, ItemStack item, int slot)
    {
        super(who);
        this.item = item;
        this.slot = slot;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public int getSlot()
    {
        return slot;
    }

    public boolean isCancelled()
    {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
