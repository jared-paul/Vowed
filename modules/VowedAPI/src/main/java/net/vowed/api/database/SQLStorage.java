package net.vowed.api.database;

import net.vowed.api.TaskChain;
import net.vowed.api.database.main.ExecuteSQL;
import net.vowed.api.database.main.QuerySQL;
import net.vowed.api.database.main.QuerySQLTest;
import net.vowed.api.database.main.UpdateSQL;
import net.vowed.api.plugin.Vowed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SQLStorage
{

    private final ConnectionPoolManager pool;

    public SQLStorage()
    {
        pool = new ConnectionPoolManager();
        makeTables();
    }

    private void makeTables()
    {
        Connection connection = null;

        PreparedStatement player_info = null;
        PreparedStatement accessory_data = null;
        PreparedStatement skin_data = null;
        PreparedStatement transaction_data = null;
        PreparedStatement scroll_data = null;
        PreparedStatement home_data = null;
        PreparedStatement post_data = null;
        PreparedStatement bank_data = null;
        PreparedStatement company_data = null;
        PreparedStatement company_shop_data = null;

        PreparedStatement clanTable = null;
        PreparedStatement membersTable = null;
        PreparedStatement bulletinBoardTable = null;
        PreparedStatement bulletinBoardMessagesTable = null;
        try
        {
            connection = pool.getConnection();

            Vowed.LOG.warning("CREATE TABLE IF NOT EXISTS " +
                    "clans" +
                    "(uuid VARCHAR(36) PRIMARY KEY, " +
                    "leader VARCHAR(36), " +
                    "name VARCHAR(16), " +
                    "tag VARCHAR(5)) " +
                    "ENGINE = InnoDB;");

            clanTable = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " +
                            "clans" +
                            "(clan_uuid VARCHAR(36) PRIMARY KEY, " +
                            "leader VARCHAR(36), " +
                            "name VARCHAR(16), " +
                            "tag VARCHAR(5)) " +
                            "ENGINE = InnoDB;"
            );

            Vowed.LOG.debug("CREATE TABLE IF NOT EXISTS " +
                    "members" +
                    "(member_uuid VARCHAR(36) PRIMARY KEY, " +
                    "clan_uuid VARCHAR(36), " +
                    "rank (VARCHAR(16), " +
                    "constraint fk_clan_members FOREIGN KEY(clan_uuid) references clans(clan_uuid) ON UPDATE CASCADE ON DELETE CASCADE) " +
                    "ENGINE = InnoDB;");

            membersTable = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " +
                            "members" +
                            "(member_uuid VARCHAR(36) PRIMARY KEY, " +
                            "clan_uuid VARCHAR(36), " +
                            "rank VARCHAR(16), " +
                            "constraint fk_clan_members FOREIGN KEY(clan_uuid) references clans(clan_uuid) ON UPDATE CASCADE ON DELETE CASCADE) " +
                            "ENGINE = InnoDB;"
            );

            bulletinBoardTable = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " +
                            "bulletinboard" +
                            "(clan_uuid VARCHAR(36), " +
                            "location TEXT, " +
                            "direction TEXT, " +
                            "constraint fk_clan_bulletinboard FOREIGN KEY(clan_uuid) references clans(clan_uuid) ON UPDATE CASCADE ON DELETE CASCADE) " +
                            "ENGINE = InnoDB;"
            );

            bulletinBoardMessagesTable = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS " +
                            "board_messages" +
                            "(clan_uuid VARCHAR(36), " +
                            "message TEXT, " +
                            "priority CHAR(2), " +
                            "date TEXT, " +
                            "constraint fk_clan_messages FOREIGN KEY(clan_uuid) references clans(clan_uuid) ON UPDATE CASCADE ON DELETE CASCADE) " +
                            "ENGINE = InnoDB;"
            );


            /*
            player_info = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                              "player_info" +
                                                              "(UUID VARCHAR(36) PRIMARY KEY, " +
                                                              "first_login BOOL, race VARCHAR(32), " +
                                                              "gender VARCHAR(10), " +
                                                              "test BOOL) " +
                                                              "ENGINE=InnoDB;"
            );
            accessory_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                                 "accessory_data" +
                                                                 "(owner VARCHAR(36) PRIMARY KEY, " +
                                                                 "item BLOB" +
                                                                 ") " +
                                                                 "ENGINE=InnoDB;"
            );
            skin_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                            "skin_data" +
                                                            "(UUID VARCHAR(36) PRIMARY KEY, " +
                                                            "name TEXT, " +
                                                            "value TEXT, " +
                                                            "signature TEXT" +
                                                            ") " +
                                                            "ENGINE=InnoDB;"
            );
            transaction_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                                   "transaction_data" +
                                                                   "(id VARCHAR(36) PRIMARY KEY, " +
                                                                   "buyerID VARCHAR(36), " +
                                                                   "sellerID VARCHAR(36), " +
                                                                   "transaction BLOB" +
                                                                   ") " +
                                                                   "ENGINE=InnoDB;"
            );
            scroll_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                              "scroll_data" +
                                                              "(UUID VARCHAR(36), " +
                                                              "item BLOB, " +
                                                              "type VARCHAR(30), " +
                                                              "tier VARCHAR(30), " +
                                                              "parent BLOB" +
                                                              ") " +
                                                              "ENGINE=InnoDB;"
            );
            home_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                            "home_data" +
                                                            "(UUID VARCHAR(36) PRIMARY KEY, " +
                                                            "location BLOB, " +
                                                            "inventory BLOB, " +
                                                            "region TEXT" +
                                                            ") " +
                                                            "ENGINE=InnoDB;"
            );
            post_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                            "post_data" +
                                                            "(location VARCHAR(255) PRIMARY KEY, " +
                                                            "list BLOB" +
                                                            ") " +
                                                            "ENGINE=InnoDB;"
            );
            bank_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                            "bank_data" +
                                                            "(UUID VARCHAR(36) PRIMARY KEY, " +
                                                            "bank BLOB, " +
                                                            "menuMap BLOB" +
                                                            ") " +
                                                            "ENGINE=InnoDB;"
            );
            company_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                               "company_data" +
                                                               "(UUID VARCHAR(36) PRIMARY KEY, " +
                                                               "ownerUUID VARCHAR(36), " +
                                                               "name VARCHAR(64), " +
                                                               "location BLOB, " +
                                                               "type VARCHAR(36), " +
                                                               "region VARCHAR(100)" +
                                                               ") " +
                                                               "ENGINE=InnoDB;"
            );
            company_shop_data = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                                                               "company_shop_data" +
                                                               "(companyUUID VARCHAR(36), " +
                                                               "slot INT, " +
                                                               "shop_item BLOB" +
                                                               ") " +
                                                               "ENGINE=InnoDB;"
            );



            player_info.executeUpdate();
            accessory_data.executeUpdate();
            skin_data.executeUpdate();
            transaction_data.executeUpdate();
            scroll_data.executeUpdate();
            home_data.executeUpdate();
            post_data.executeUpdate();
            bank_data.executeUpdate();
            company_data.executeUpdate();
            company_shop_data.executeUpdate();
            */
            clanTable.executeUpdate();
            membersTable.executeUpdate();
            bulletinBoardTable.executeUpdate();
            bulletinBoardMessagesTable.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            pool.close(connection, clanTable, null);
            pool.close(connection, membersTable, null);
            pool.close(connection, bulletinBoardTable, null);
            pool.close(connection, bulletinBoardMessagesTable, null);
        }
    }

    public void executeAsyncUpdate(String query, final Callback<Integer> callback)
    {       //run async query
        TaskChain.newChain()
                .async(() -> new UpdateSQL(pool.getDataSource(), query, callback))
                .execute();
    }

    public void executeUpdate(String query, final Callback<Integer> callback)
    {       //run async query
        TaskChain.newChain()
                .sync(() -> new UpdateSQL(pool.getDataSource(), query, callback))
                .execute();
    }

    public void executeAsyncQuery(String query, final Callback<ResultSet> callback)
    {
        TaskChain.newChain()
                .async(() -> new QuerySQL(pool.getDataSource(), query, callback))
                .execute();
    }

    public void executeQuery(String query, final Callback<ResultSet> callback)
    {
        TaskChain.newChain()
                .sync(() -> new QuerySQL(pool.getDataSource(), query, callback))
                .execute();
    }

    public ResultSet executeQuery(String query)
    {
        try (
                Connection connection = pool.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
        )
        {

            return resultSet;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void executeAsync(String query, final Callback<Boolean> callback)
    {
        TaskChain.newChain()
                .async(() -> new ExecuteSQL(pool.getDataSource(), query, callback))
                .execute();
    }

    public void execute(String query, final Callback<Boolean> callback)
    {
        TaskChain.newChain()
                .sync(() -> new ExecuteSQL(pool.getDataSource(), query, callback))
                .execute();
    }


    public interface Callback<T>
    {
        void onSuccess(T t) throws SQLException;

        void onFailure(Throwable cause);
    }

    public void updateAsyncQuery(String table, String[] columns, String[] values, String[] updates, Callback<Integer> callback)
    {
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        StringBuilder updateBuilder = new StringBuilder();

        List<String> columnList = Arrays.asList(columns);
        List<String> valueList = Arrays.asList(values);
        List<String> updateList = Arrays.asList(updates);

        for (String column : columnList)
        {
            if (columnList.indexOf(column) == columnList.size() - 1)
            {
                columnBuilder.append(column);
            }
            else
            {
                columnBuilder.append(column).append(", ");
            }
        }

        for (String value : values)
        {
            int index = valueList.indexOf(value);
            int valueListSize = valueList.size() - 1;

            if (index == valueListSize)
            {
                try
                {
                    UUID.fromString(value);
                    valueBuilder.append("'").append(value).append("'");
                } catch (Exception exception)
                {
                    if (isBoolean(value))
                    {
                        valueBuilder.append(value);
                    }
                    else
                    {
                        valueBuilder.append("'").append(value).append("'");
                    }
                }
            }
            else
            {
                try
                {
                    UUID.fromString(value);
                    valueBuilder.append("'").append(value).append("', ");
                } catch (Exception exception)
                {
                    if (isBoolean(value))
                    {
                        valueBuilder.append(value).append(", ");
                    }
                    else
                    {
                        valueBuilder.append("'").append(value).append("', ");
                    }
                }
            }
        }

        updateBuilder.append("ON DUPLICATE KEY UPDATE ");

        int updateListSize = updateList.size();

        for (String update : updateList)
        {
            if (updateList.size() != 1)
            {
                if (updateList.indexOf(update) == 0)
                {
                    updateListSize = updateList.size() - 1;
                }
                else
                {
                    int index = updateList.indexOf(update);

                    if (index != updateListSize)
                    {
                        if (isBoolean(update))
                        {
                            String test = columnList.get(index) + " = " + update + ", ";
                            updateBuilder.append(test);
                        }
                        else
                        {
                            String test = columnList.get(index) + " = '" + update + "', ";
                            updateBuilder.append(test);
                        }
                    }
                    else
                    {
                        if (isBoolean(update))
                        {
                            String test = columnList.get(index) + " = " + update;
                            updateBuilder.append(test);
                        }
                        else
                        {
                            String test = columnList.get(index) + " = '" + update + "'";
                            updateBuilder.append(test);
                        }
                    }

                }
            }
            else
            {
                try
                {
                    UUID.fromString(update);
                } catch (Exception exception)
                {
                    int index = updateList.indexOf(update) + 1;

                    if (isBoolean(update))
                    {
                        String test = columnList.get(index) + " = " + update;
                        updateBuilder.append(test);
                    }
                    else
                    {
                        String test = columnList.get(index) + " = '" + update + "'";
                        updateBuilder.append(test);
                    }
                }
            }
        }

        String query = "INSERT INTO " + table + " (" + columnBuilder.toString() + ") " +
                "VALUES (" + valueBuilder.toString() + ") " +
                updateBuilder.toString();

        executeAsyncUpdate(query, callback);
    }

    public void updateQuery(String table, String[] columns, String[] values, String[] updates, Callback<Integer> callback)
    {
        StringBuilder columnBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();
        StringBuilder updateBuilder = new StringBuilder();

        List<String> columnList = Arrays.asList(columns);
        List<String> valueList = Arrays.asList(values);
        List<String> updateList = Arrays.asList(updates);

        for (String column : columnList)
        {
            if (columnList.indexOf(column) == columnList.size() - 1)
            {
                columnBuilder.append(column);
            }
            else
            {
                columnBuilder.append(column).append(", ");
            }
        }

        for (String value : values)
        {
            int index = valueList.indexOf(value);
            int valueListSize = valueList.size() - 1;

            if (index == valueListSize)
            {
                try
                {
                    UUID.fromString(value);
                    valueBuilder.append("'").append(value).append("'");
                } catch (Exception exception)
                {
                    if (isBoolean(value))
                    {
                        valueBuilder.append(value);
                    }
                    else
                    {
                        valueBuilder.append("'").append(value).append("'");
                    }
                }
            }
            else
            {
                try
                {
                    UUID.fromString(value);
                    valueBuilder.append("'").append(value).append("', ");
                } catch (Exception exception)
                {
                    if (isBoolean(value))
                    {
                        valueBuilder.append(value).append(", ");
                    }
                    else
                    {
                        valueBuilder.append("'").append(value).append("', ");
                    }
                }
            }
        }

        updateBuilder.append("ON DUPLICATE KEY UPDATE ");

        int updateListSize = updateList.size();

        for (String update : updateList)
        {
            if (updateList.size() != 1)
            {
                if (updateList.indexOf(update) == 0)
                {
                    updateListSize = updateList.size() - 1;
                }
                else
                {
                    int index = updateList.indexOf(update);

                    if (index != updateListSize)
                    {
                        if (isBoolean(update))
                        {
                            String test = columnList.get(index) + " = " + update + ", ";
                            updateBuilder.append(test);
                        }
                        else
                        {
                            String test = columnList.get(index) + " = '" + update + "', ";
                            updateBuilder.append(test);
                        }
                    }
                    else
                    {
                        if (isBoolean(update))
                        {
                            String test = columnList.get(index) + " = " + update;
                            updateBuilder.append(test);
                        }
                        else
                        {
                            String test = columnList.get(index) + " = '" + update + "'";
                            updateBuilder.append(test);
                        }
                    }

                }
            }
            else
            {
                try
                {
                    UUID.fromString(update);
                } catch (Exception exception)
                {
                    int index = updateList.indexOf(update) + 1;

                    if (isBoolean(update))
                    {
                        String test = columnList.get(index) + " = " + update;
                        updateBuilder.append(test);
                    }
                    else
                    {
                        String test = columnList.get(index) + " = '" + update + "'";
                        updateBuilder.append(test);
                    }
                }
            }
        }

        String query = "INSERT INTO " + table + " (" + columnBuilder.toString() + ") " +
                "VALUES (" + valueBuilder.toString() + ") " +
                updateBuilder.toString();

        Vowed.LOG.debug(query);

        executeUpdate(query, callback);
    }

    public void selectAllAsyncQuery(String table, String primaryKeyName, String primaryKeyValue, Callback<ResultSet> callback)
    {
        executeAsyncQuery("SELECT * FROM " + table + " WHERE " + primaryKeyName + " = '" + primaryKeyValue + "'", callback);
    }

    public void selectAllQuery(String table, String primaryKeyName, String primaryKeyValue, Callback<ResultSet> callback)
    {
        executeAsyncQuery("SELECT * FROM " + table + " WHERE " + primaryKeyName + " = '" + primaryKeyValue + "'", callback);
    }

    public void deleteQuery(String table, String primaryKeyName, String primaryKeyValue, Callback<Boolean> callback)
    {
        executeAsync("DELETE FROM " + table + " WHERE " + primaryKeyName + " = '" + primaryKeyValue + "'", callback);
    }

    public void onDisable()
    {
        pool.closePool();
    }

    private boolean isBoolean(String bool)
    {
        if (bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("false"))
        {
            return true;
        }

        return false;
    }
}
