package net.vowed.core.itempair;

import net.vowed.api.plugin.Vowed;
import net.vowed.core.storage.AttributeStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by JPaul on 8/23/2016.
 */
public class ItemPair implements Listener
{
    public static final UUID ITEMPAIR_UUID = UUID.fromString("68737ea2-291e-4932-a564-dd025843abb9");

    ItemStack item;
    InteractCallback callback;

    public ItemPair(ItemStack item, InteractCallback callback)
    {
        this.item = item;
        this.callback = callback;

        AttributeStorage storage = AttributeStorage.newTarget(item, ITEMPAIR_UUID);
        storage.setData(UUID.randomUUID().toString());

        Bukkit.getPluginManager().registerEvents(this, Vowed.getPlugin());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent interactEvent)
    {
        if (interactEvent.getHand() == EquipmentSlot.HAND)
        {
            if (item.isSimilar(interactEvent.getItem()))
            {
                callback.onInteract(interactEvent.getClickedBlock().getLocation(), interactEvent.getAction(), interactEvent.getPlayer());
                HandlerList.unregisterAll(this);
            }
        }
    }

    public interface InteractCallback
    {
        void onInteract(Location clickedBlock, Action action, Player player);
    }
}
