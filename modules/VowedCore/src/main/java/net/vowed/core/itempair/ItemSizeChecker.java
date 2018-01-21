package net.vowed.core.itempair;

import com.google.common.collect.Maps;
import net.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by JPaul on 8/24/2016.
 */
public class ItemSizeChecker implements Listener
{
    ItemStack item;
    SizeCallback callback;

    public ItemSizeChecker(ItemStack item, SizeCallback callback)
    {
        this.item = item;
        this.callback = callback;

        Bukkit.getPluginManager().registerEvents(this, Vowed.getPlugin());
    }

    ItemTuple squareCache = null;

    @EventHandler
    public void onMove(PlayerMoveEvent moveEvent)
    {
        Player player = moveEvent.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 10);
        Location targetBlockLocation = targetBlock.getLocation();

        if (mainHand != null && mainHand.getType() != Material.AIR && mainHand.hasItemMeta())
        {
            Vowed.LOG.debug(targetBlockLocation.getBlockY());
            Vowed.LOG.warning(player.getLocation().getBlockY());

            if (targetBlockLocation.getBlockY() == player.getLocation().getBlockY() - 1)
            {
                if (mainHand.isSimilar(item))
                {
                    if (squareCache != null && !squareCache.getCenter().equals(targetBlockLocation))
                    {
                        for (Block newBlock : squareCache.getNewBlocks())
                        {
                            newBlock.setType(squareCache.getOldBlock(newBlock));
                        }

                        squareCache = createSquare(targetBlockLocation, 5);

                        if (isFlat(squareCache.getNewBlocks()))
                        {
                            squareCache.getNewBlocks().forEach(block -> block.setTypeIdAndData(Material.WOOL.getId(), DyeColor.GREEN.getDyeData(), false));
                        }
                        else
                        {
                            squareCache.getNewBlocks().forEach(block -> block.setTypeIdAndData(Material.WOOL.getId(), DyeColor.RED.getDyeData(), false));
                        }
                    }

                    if (squareCache == null)
                    {
                        squareCache = createSquare(targetBlockLocation, 5);

                        if (isFlat(squareCache.getNewBlocks()))
                        {
                            squareCache.getNewBlocks().forEach(block -> block.setTypeIdAndData(Material.WOOL.getId(), DyeColor.GREEN.getDyeData(), false));
                        }
                        else
                        {
                            squareCache.getNewBlocks().forEach(block -> block.setTypeIdAndData(Material.WOOL.getId(), DyeColor.RED.getDyeData(), false));
                        }
                    }
                }
            }
        }
    }


    private ItemTuple createSquare(Location center, int radius)
    {
        Map<Block, Material> blockMap = Maps.newHashMap();

        int blockX = center.getBlockX();
        int blockZ = center.getBlockZ();

        for (int x = blockX - radius; x <= blockX + radius; x++)
        {
            for (int z = blockZ - radius; z <= blockZ + radius; z++)
            {
                Location location = new Location(center.getWorld(), x, center.getY(), z);
                Block block = location.getBlock();

                blockMap.put(block, block.getType());
            }
        }

        return new ItemTuple(blockMap, center);
    }

    public boolean isFlat(Set<Block> square)
    {
        List<Boolean> booleans = new ArrayList<>();

        for (Block block : square)
        {
            Block above = block.getRelative(BlockFace.UP);
            Block below = block.getRelative(BlockFace.DOWN);

            if (above.getType() == Material.AIR && below.getType() != Material.AIR)
            {
                booleans.add(true);
            }
            else
            {
                booleans.add(false);
            }
        }

        return !booleans.contains(false);
    }

    public interface SizeCallback
    {
        void onMove(PlayerMoveEvent moveEvent);
    }
}
