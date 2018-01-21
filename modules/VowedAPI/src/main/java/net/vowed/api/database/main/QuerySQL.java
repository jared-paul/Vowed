package net.vowed.api.database.main;

import net.vowed.api.database.SQLStorage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by JPaul on 2/1/2016.
 */
public class QuerySQL implements Runnable
{
    private final DataSource dataSource;
    private final String statement;
    private final SQLStorage.Callback<ResultSet> callback;

    public QuerySQL(DataSource dataSource, String statement, SQLStorage.Callback<ResultSet> callback)
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

        run();
    }

    @Override
    public void run()
    {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
                ResultSet resultSet = preparedStatement.executeQuery();
        )
        {

            callback.onSuccess(resultSet);
        } catch (SQLException e)
        {
            callback.onFailure(e);
        }
    }
}
