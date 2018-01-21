package net.vowed.items.armour.event;

import net.vowed.api.plugin.Vowed;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * CREATED BY ROJOSS https://gist.github.com/Rojoss/7a3172e410b47c9cdaad
 */
public class ArmourEventListener implements Listener
{
    @EventHandler
    public void onDeath(PlayerDeathEvent deathEvent)
    {
        Player player = deathEvent.getEntity();

        for (int i = 0; i < player.getInventory().getArmorContents().length; i++)
        {
            ItemStack armour = player.getInventory().getArmorContents()[i];

            ArmourUnequipEvent armourEvent = new ArmourUnequipEvent(player, armour, i);
            Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEvent);
        }
    }

    @EventHandler
    private void clickArmor(final InventoryClickEvent event)
    {
        final Player player = (Player) event.getWhoClicked();

        //Shift clicking armour items to equip and unequip.
        if (event.isShiftClick())
        {
            //Only check armour
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR || !isArmourItem(item.getType()) || event.getSlotType() == InventoryType.SlotType.CRAFTING)
            {
                return;
            }

            if (event.getSlotType() == InventoryType.SlotType.ARMOR)
            {
                //Unequip - Full inventory check.

                if (player.getInventory().firstEmpty() < 0)
                {
                    return;
                }
                ArmourUnequipEvent armourEvent = new ArmourUnequipEvent(player, item, event.getSlot());
                Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEvent);
                if (armourEvent.isCancelled())
                {
                    event.setCancelled(true);
                }
            }
            else
            {
                //Equip - Check for empty slot
                if (!hasFreeArmourSpot(player, item.getType()))
                {
                    return;
                }
                //Can't shift click pumpkins
                if (item.getType() == Material.PUMPKIN)
                {
                    return;
                }
                ArmourEquipEvent armourEvent = new ArmourEquipEvent(player, item, getFreeArmourSlot(player, item.getType()));
                Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEvent);
                if (armourEvent.isCancelled())
                {
                    event.setCancelled(true);
                }
            }
            return;
        }

        //When not shift clicking only check for clicking armour slots.
        if (event.getSlotType() != InventoryType.SlotType.ARMOR)
        {
            return;
        }

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
        {
            final int slot = event.getSlot();
            final ItemStack slotItem = player.getInventory().getItem(slot);
            //Equip
            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    ItemStack item = player.getInventory().getItem(slot);
                    if (item == null || item.getType() == Material.AIR || !isArmourItem(item.getType()))
                    {
                        return;
                    }
                    ArmourEquipEvent armourEvent = new ArmourEquipEvent(player, item, slot);
                    Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEvent);
                    if (armourEvent.isCancelled())
                    {
                        player.getInventory().setItem(slot, slotItem);
                        event.setCursor(item);
                    }
                }
            }.runTaskLater(Vowed.getPlugin(), 1);
        }
        else
        {
            if (event.getCursor() != null && event.getCursor().getType() != Material.AIR)
            {
                //Swapping items (for example clicking with a gold helmet on current equiped iron helmet unequip iron and equip gold.
                if (!isArmourItem(event.getCursor().getType()))
                {
                    return;
                }
                String cursorItem = event.getCursor().getType().toString();
                String slotItem = event.getCurrentItem().getType().toString();
                if ((cursorItem.endsWith("HELMET") && slotItem.endsWith("HELMET")) ||
                        (cursorItem.endsWith("CHESTPLATE") && slotItem.endsWith("CHESTPLATE")) ||
                        (cursorItem.endsWith("LEGGINGS") && slotItem.endsWith("LEGGINGS")) ||
                        (cursorItem.endsWith("BOOTS") && slotItem.endsWith("BOOTS")) ||
                        (cursorItem.endsWith("HELMET") && slotItem.equals("PUMPKIN")) ||
                        (cursorItem.equals("PUMPKIN") && slotItem.endsWith("HELMET")) ||
                        (cursorItem.equals("PUMPKIN") && slotItem.equals("PUMPKIN")))
                {
                    ArmourUnequipEvent armourEvent = new ArmourUnequipEvent(player, event.getCurrentItem(), event.getSlot());
                    Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEvent);
                    if (armourEvent.isCancelled())
                    {
                        event.setCancelled(true);
                    }
                    else
                    {
                        ArmourEquipEvent armourEquipEvent = new ArmourEquipEvent(player, event.getCursor(), event.getSlot());
                        Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEquipEvent);
                        if (armourEquipEvent.isCancelled())
                        {
                            event.setCancelled(true);
                        }
                    }
                }
                return;
            }

            //Unequip
            ItemStack item = event.getCurrentItem();
            ArmourUnequipEvent armourEvent = new ArmourUnequipEvent(player, item, event.getSlot());
            Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEvent);
            if (armourEvent.isCancelled())
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void dragArmour(InventoryDragEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getOldCursor();
        if (item == null || item.getType() == Material.AIR || !isArmourItem(item.getType()))
        {
            return;
        }
        int armourSlot = getFreeArmourSlot(player, event.getOldCursor().getType());
        if (armourSlot < 0)
        {
            return;
        }
        if (!event.getInventorySlots().contains(armourSlot))
        {
            return;
        }
        ArmourEquipEvent armourEvent = new ArmourEquipEvent(player, item, armourSlot);
        Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEvent);
        if (armourEvent.isCancelled())
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void equipArmour(final PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }

        if (event.getClickedBlock() != null && event.getClickedBlock().getType() != Material.AIR)
        {
            return;
        }

        final ItemStack item = event.getPlayer().getItemInHand();
        if (item == null || item.getType() == Material.AIR)
        {
            return;
        }
        final int amount = item.getAmount();

        final int armourSlot = getFreeArmourSlot(event.getPlayer(), item.getType());
        if (armourSlot < 0)
        {
            return;
        }

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                ArmourEquipEvent armourEvent = new ArmourEquipEvent(event.getPlayer(), item, armourSlot);
                Vowed.getPlugin().getServer().getPluginManager().callEvent(armourEvent);
                if (armourEvent.isCancelled())
                {
                    event.getPlayer().getInventory().setItem(armourSlot, new ItemStack(Material.AIR));
                    ItemStack returnItem = item.clone();
                    returnItem.setAmount(amount);
                    event.getPlayer().setItemInHand(returnItem);
                    event.getPlayer().updateInventory();
                }
            }
        }.runTaskLater(Vowed.getPlugin(), 1);
    }

    private boolean isArmourItem(Material mat)
    {
        if (mat.toString().endsWith("HELMET") || mat.toString().endsWith("CHESTPLATE") || mat.toString().endsWith("LEGGINGS") || mat.toString().endsWith("BOOTS") || mat == Material.PUMPKIN)
        {
            return true;
        }

        return false;
    }

    private boolean hasFreeArmourSpot(Player player)
    {
        for (ItemStack itemStack : player.getInventory().getArmorContents())
        {
            if (itemStack == null || itemStack.getType() == Material.AIR)
            {
                return true;
            }
        }

        return false;
    }

    private boolean hasFreeArmourSpot(Player player, Material mat)
    {
        if (mat.toString().endsWith("HELMET") || mat == Material.PUMPKIN)
        {
            return player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType() == Material.AIR;
        }
        else if (mat.toString().endsWith("CHESTPLATE"))
        {
            return player.getInventory().getChestplate() == null || player.getInventory().getChestplate().getType() == Material.AIR;
        }
        else if (mat.toString().endsWith("LEGGINGS"))
        {
            return player.getInventory().getLeggings() == null || player.getInventory().getLeggings().getType() == Material.AIR;
        }
        else if (mat.toString().endsWith("BOOTS"))
        {
            return player.getInventory().getBoots() == null || player.getInventory().getBoots().getType() == Material.AIR;
        }
        return false;
    }

    private int getFreeArmourSlot(Player player, Material mat)
    {
        if (!hasFreeArmourSpot(player, mat))
        {
            return -1;
        }
        if (mat.toString().endsWith("HELMET") || mat == Material.PUMPKIN)
        {
            return 39;
        }
        else if (mat.toString().endsWith("CHESTPLATE"))
        {
            return 38;
        }
        else if (mat.toString().endsWith("LEGGINGS"))
        {
            return 37;
        }
        else if (mat.toString().endsWith("BOOTS"))
        {
            return 36;
        }

        return -1;
    }
}
