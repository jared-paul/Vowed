package net.vowed.api.shops;

import org.bukkit.Location;

import java.util.UUID;

/**
 * Created by JPaul on 2017-09-04.
 */
public interface IShopRegistry
{
    IChestShop createShop(UUID owner, Location location);

    IChestShop getShop(UUID owner);

    IChestShop getShop(Location location);
}
