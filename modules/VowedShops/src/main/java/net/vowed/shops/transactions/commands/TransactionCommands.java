package net.vowed.shops.transactions.commands;

import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.commands.Command;
import net.vowed.core.commands.CommandClass;
import net.vowed.core.commands.CommandContext;
import net.vowed.core.util.Preconditions;
import net.vowed.core.util.fetchers.UUIDFetcher;
import net.vowed.core.util.math.Integers;
import net.vowed.core.util.strings.Strings;
import net.vowed.shops.transactions.menu.history.seller.SellerTransactionMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by JPaul on 2017-08-31.
 */
public class TransactionCommands implements CommandClass
{
    @Command(
            aliases = {"vowed transaction"},
            usage = "/vowed transaction history <player>",
            description = "see someones transaction history",
            initializer = "history",
            minArgs = 1,
            maxArgs = 1
    )
    public void lookUpHistory(CommandContext args, CommandSender sender)
    {
        Player player = (Player) sender;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args.getString(0));

        if (Preconditions.checkNotNull(offlinePlayer, player, Strings.handleError("Sorry, but that player is null")))
            return;

        if (offlinePlayer.hasPlayedBefore())
        {
            showHistory(player, offlinePlayer.getUniqueId());
        }
        else
        {
            UUIDFetcher.getUUIDOf(args.getString(0))
                    .syncLast(uuid ->
                    {
                        showHistory(player, uuid);
                    })
                    .execute();
        }
    }

    private void showHistory(Player player, UUID uuid)
    {
        Vowed.getSQLStorage().selectAllAsyncQuery("transaction_data",
                "sellerID",
                uuid.toString(),
                new SQLStorage.Callback<ResultSet>()
                {
                    @Override
                    public void onSuccess(ResultSet resultSet) throws SQLException
                    {
                        int counter = 0;

                        while (resultSet.next())
                        {

                        }

                        SellerTransactionMenu sellerTransactionMenu = new SellerTransactionMenu(uuid, Integers.roundUpPositive(counter / 45D));
                        sellerTransactionMenu.showToPlayer(player);
                    }

                    @Override
                    public void onFailure(Throwable cause)
                    {
                        Vowed.LOG.warning("MYSQL ERROR: Error loading " + player.getName() + "'s selling history");
                        cause.printStackTrace();
                    }
                });
    }
}
