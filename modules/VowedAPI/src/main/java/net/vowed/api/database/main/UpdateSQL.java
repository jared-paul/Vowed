package net.vowed.api.database.main;

import net.vowed.api.database.SQLStorage;

import javax.annotation.Nullable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by JPaul on 2/2/2016.
 */
public class UpdateSQL implements Runnable
{
    private final DataSource dataSource;
    private final String statement;
    private final SQLStorage.Callback<Integer> callback;

    public UpdateSQL(DataSource dataSource, String statement, @Nullable SQLStorage.Callback<Integer> callback)
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

        run();
    }

    @Override
    public void run()
    {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(statement);
        )
        {

            callback.onSuccess(preparedStatement.executeUpdate());
        } catch (SQLException e)
        {
            callback.onFailure(e);
        }
    }
}
