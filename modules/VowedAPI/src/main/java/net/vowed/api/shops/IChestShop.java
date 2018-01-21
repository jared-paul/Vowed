package net.vowed.api.shops;

import me.jpaul.menuapi.items.MenuItem;
import me.jpaul.menuapi.types.MultiMenu;
import me.jpaul.menuapi.types.Page;
import net.vowed.api.player.IVowedPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 2017-09-04.
 */
public interface IChestShop extends IShop
{
    List<Page> getPages(IVowedPlayer vowedPlayer);

    List<Page> getPages(IVowedPlayer vowedPlayer, UUID ownerUUID);

    MultiMenu getMenu(IVowedPlayer vowedPlayer);

    List<Page> getOwnerPages();

    void setOwnerPages(List<Page> ownerPages);

    void addPage();

    String getName();

    void setName(String name);

    void changeName(String name);

    List<MenuItem> getContents();

    void setContents(List<MenuItem> contents);

    Location getLocation();

    void setLocation(Location location);

    boolean isOwner(UUID playerUUID);

    UUID getOwner();

    void setOwner(UUID owner);

    void addItem(MenuItem item);

    void addItem(MenuItem item, int slot);

    void removeItem(MenuItem item);

    void removeEditedItem(MenuItem item);

    List<Location> getBlocks();

    void setOpen(boolean open);

    boolean isOpen();

    void createShop();

    void destroyShop(Player requester);
}
