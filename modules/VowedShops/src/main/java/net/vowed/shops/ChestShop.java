package net.vowed.shops;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.types.MultiMenu;
import me.jpaul.menuapi.types.Page;
import net.vowed.api.player.IVowedPlayer;
import net.vowed.api.player.races.RaceType;
import net.vowed.api.plugin.Vowed;
import net.vowed.api.shops.IChestShop;
import net.vowed.shops.menu.CloseButton;
import net.vowed.shops.menu.OpenButton;
import net.vowed.shops.menu.ShopMenu;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

/**
 * Created by JPaul on 10/15/2015.
 */
public class ChestShop implements IChestShop
{
    private String name;
    private List<MenuItem> contents;
    private Location location;
    private Location armourStandLocation;
    private ShopType shopType;
    private boolean isOpen;
    private UUID owner;
    private ArmorStand armourStand;
    private Location block1;
    private Location block2;

    private List<Page> ownerPages;
    private Map<RaceType, List<Page>> pageTests;

    public ChestShop(UUID owner, String name, Location location, ShopType shopType)
    {
        this.owner = owner;
        this.name = name;
        this.contents = Lists.newArrayList();
        this.location = location;
        this.shopType = shopType;
        this.ownerPages = Lists.newArrayList();
        this.pageTests = Maps.newHashMap();
        pageTests.put(RaceType.ELF, Lists.newArrayList());
        pageTests.put(RaceType.DWARF, Lists.newArrayList());
        pageTests.put(RaceType.HUMAN, Lists.newArrayList());
    }

    public MultiMenu getMenu(IVowedPlayer vowedPlayer)
    {
        return new ShopMenu(this, getPages(vowedPlayer));
    }

    public List<Page> getPages(IVowedPlayer vowedPlayer)
    {
        return getPages(vowedPlayer, getOwner());
    }

    public List<Page> getPages(IVowedPlayer player, UUID ownerUUID)
    {
        if (player.getUUID().equals(ownerUUID))
        {
            return ownerPages;
        }
        else
        {
            return pageTests.get(player.getRace());
        }
    }

    /*
    public MultiMenu getMenu(IVowedPlayer vowedPlayer, UUID companyUUID)
    {
        return new ShopMenu(this, getPages(vowedPlayer, companyUUID));
    }
    */

    public List<Page> getOwnerPages()
    {
        return ownerPages;
    }

    public void setOwnerPages(List<Page> ownerPages)
    {
        this.ownerPages = ownerPages;
    }

    public String getName()
    {
        return this.name;
    }

    public List<MenuItem> getContents()
    {
        return this.contents;
    }

    public Location getLocation()
    {
        return this.location;
    }

    public UUID getOwner()
    {
        return this.owner;
    }

    public ShopType getType()
    {
        return this.shopType;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setContents(List<MenuItem> contents)
    {
        this.contents = contents;
    }

    public void addItem(MenuItem item)
    {
        this.contents.add(item);

        for (Page page : ownerPages)
        {
            if (page.getInventory().firstEmpty() != -1)
            {
                page.addItem(item);
                break;
            }
        }

        if (!(item instanceof OpenButton) && !(item instanceof CloseButton))
        {
            if (item instanceof ShopItem)
            {
                for (RaceType race : RaceType.values())
                {
                    List<Page> pages = pageTests.get(race);

                    for (Page page : pages)
                    {
                        if (page.getInventory().firstEmpty() != -1)
                        {
                            page.addItem(((ShopItem) item).getRaceInstance(race));
                            break;
                        }
                    }
                }
            }
        }
    }

    public void addItem(MenuItem item, int slot)
    {
        this.contents.add(item);

        for (Page page : ownerPages)
        {
            if (page.getInventory().firstEmpty() != -1)
            {
                page.addItem(slot, item);
                break;
            }
        }

        if (!(item instanceof OpenButton) && !(item instanceof CloseButton))
        {
            if (item instanceof ShopItem)
            {
                for (RaceType race : RaceType.values())
                {
                    List<Page> pages = pageTests.get(race);

                    for (Page page : pages)
                    {
                        if (page.getInventory().firstEmpty() != -1)
                        {
                            page.addItem(slot, ((ShopItem) item).getRaceInstance(race));
                            break;
                        }
                    }
                }
            }
        }
    }

    private int getAvailableSlot()
    {
        for (List<Page> pageList : pageTests.values())
        {
            for (Page page : pageList)
            {
                for (int slot = 0; slot < page.getInventory().getSize(); slot++)
                {
                    if (page.getInventory().getItem(slot) == null)
                    {
                        return slot;
                    }
                }
            }
        }

        return -1;
    }

    public void removeItem(MenuItem item)
    {
        this.contents.remove(item);

        ownerPages.forEach(page -> page.removeItem(item));

        if (!(item instanceof CloseButton) && !(item instanceof OpenButton))
        {
            for (List<Page> pages : pageTests.values())
            {
                for (Page page : pages)
                {
                    page.removeItem(item);
                }
            }
        }
    }

    public void removeEditedItem(MenuItem item)
    {
        this.contents.remove(item);

        ownerPages.forEach(page -> page.removeItem(item));

        if (item instanceof ShopItem)
        {
            for (RaceType type : RaceType.values())
            {
                for (Page page : pageTests.get(type))
                {
                    page.removeItem(((ShopItem) item).getRaceInstance(type));
                }
            }
        }
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public List<Location> getBlocks()
    {
        return Lists.newArrayList(block1, block2);
    }

    public void setOwner(UUID newOwner)
    {
        this.owner = newOwner;
    }

    public boolean isOwner(UUID owner)
    {
        return this.owner.equals(owner);
    }

    public void setType(ShopType shopType)
    {
        this.shopType = shopType;
    }

    public void setOpen(boolean isOpen)
    {
        this.isOpen = isOpen;

        if (armourStand != null && shopType == ShopType.PLAYER)
        {
            if (isOpen)
            {
                this.armourStand.setCustomName(ChatColor.GREEN + name);
            }
            else
            {
                this.armourStand.setCustomName(ChatColor.RED + name);
            }
        }
    }

    public boolean isOpen()
    {
        return isOpen;
    }

    public void showName()
    {
        this.armourStand = (ArmorStand) this.location.getWorld().spawnEntity(armourStandLocation, EntityType.ARMOR_STAND);

        armourStand.setMarker(true);
        armourStand.setGravity(false);
        armourStand.setVisible(false);
        armourStand.setCustomName(ChatColor.RED + getName());
        armourStand.setCustomNameVisible(true);
        armourStand.setSmall(true);
        armourStand.setBasePlate(false);
    }

    public void changeName(String name)
    {
        this.name = name;

        if (shopType == ShopType.PLAYER)
        {
            if (isOpen())
            {
                this.armourStand.setCustomName(ChatColor.GREEN + name);
            }
            else
            {
                this.armourStand.setCustomName(ChatColor.RED + name);
            }
        }
    }

    public void createShop()
    {
        Block block = this.location.getBlock();
        Block blockRelative = block.getRelative(BlockFace.WEST);

        block.setType(Material.CHEST);
        blockRelative.setType(Material.CHEST);

        block1 = block.getLocation();
        block2 = blockRelative.getLocation();

        block.setMetadata("shop", new FixedMetadataValue(Vowed.getPlugin(), true));
        blockRelative.setMetadata("shop", new FixedMetadataValue(Vowed.getPlugin(), true));

        if (block.getZ() < 0)
        {
            this.armourStandLocation = new Location(this.location.getWorld(), block.getX(), block.getY() + 0.7, block.getZ() + 0.5);
        }
        else if (block.getZ() >= 0)
        {
            this.armourStandLocation = new Location(this.location.getWorld(), block.getX(), block.getY() + 0.7, block.getZ() + 0.5);
        }

        addItem(new CloseButton(this), 8);

        showName();
    }

    public void destroyShop(Player requester)
    {
        ShopRegistry manager = (ShopRegistry) Vowed.getShopRegistry();

        if (!isOpen())
        {
            if (isOwner(requester.getUniqueId()))
            {
                manager.shops.remove(this);
                block1.getBlock().setType(Material.AIR);
                block2.getBlock().setType(Material.AIR);
                armourStand.remove();

                for (MenuItem item : contents)
                {
                    if (item instanceof ShopItem)
                    {
                        ((ShopItem) item).resetItem();
                        requester.getInventory().addItem(item.getItem());
                    }
                }
                requester.sendMessage(ChatColor.GREEN + "You have removed your shop");
            }
            else
            {
                requester.sendMessage(ChatColor.RED + "You are not the owner of this shop!");
            }
        }
        else
        {
            requester.sendMessage(ChatColor.RED + "The shop has to be closed for this action to be done!");
        }
    }

    public void addPage()
    {
        int pageNumber = getOwnerPages().size() + 1;

        for (List<Page> pageList : pageTests.values())
        {
            pageList.add(new Page(36, ChatColor.YELLOW + "PAGE: " + pageNumber));
            Collections.sort(pageList);
        }

        ownerPages.add(new Page(36, ChatColor.YELLOW + "PAGE: " + pageNumber));
    }
}
