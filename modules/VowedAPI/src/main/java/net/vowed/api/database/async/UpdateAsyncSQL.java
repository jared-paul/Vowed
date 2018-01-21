package net.vowed.api.database.async;

import net.vowed.api.database.SQLStorage;
import net.vowed.api.plugin.Vowed;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by JPaul on 2/2/2016.
 */
public class UpdateAsyncSQL extends BukkitRunnable
{
    private final DataSource dataSource;
    private final String statement;
    private final SQLStorage.Callback<Integer> callback;

    public UpdateAsyncSQL(DataSource dataSource, String statement, @Nullable SQLStorage.Callback<Integer> callback)
    {
        if (dataSource == null)
        {
            //TODO: IllegalArgumentException
        }

        if (statement == null)
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

        try
        {
            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(statement);

            if (callback != null)
            {
                callback.onSuccess(preparedStatement.executeUpdate());
            }
        } catch (SQLException e)
        {
            if (callback != null)
            {
                callback.onFailure(e);
            }
        } finally
        {
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
