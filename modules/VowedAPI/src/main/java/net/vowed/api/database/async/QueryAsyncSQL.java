package net.vowed.api.database.async;

import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import org.bukkit.scheduler.BukkitRunnable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JPaul on 2/1/2016.
 */
public class QueryAsyncSQL extends BukkitRunnable
{
    private final DataSource dataSource;
    private final String statement;
    private final SQLStorage.Callback<ResultSet> callback;

    public QueryAsyncSQL(DataSource dataSource, String statement, SQLStorage.Callback<ResultSet> callback)
    {
        if (dataSource == null)
        {
            //TODO: IllegalArgumentException
        }

        if (statement == null)
        {
            //TODO: IllegalArgumentException
        }

        if (callback == null)
        {
            //TODO: IllegalArgumentException
        }

        this.dataSource = dataSource;
        this.statement = statement;
        this.callback = callback;

        runTaskAsynchronously(Vowed.getPlugin());
    }

    @Override
    public void run()
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try
        {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.execute();
            resultSet = preparedStatement.getResultSet();

            callback.onSuccess(resultSet);
        }
        catch (SQLException e)
        {
            callback.onFailure(e);
        }
        finally
        {
            if (resultSet != null)
            {
                try
                {
                    resultSet.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null)
            {
                try
                {
                    preparedStatement.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            if (connection != null)
            {
                try
                {
                    connection.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
