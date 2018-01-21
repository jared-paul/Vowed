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
public class QuerySQLTest implements Runnable
{
    private final DataSource dataSource;
    private final String statement;
    private ResultSet resultSet;

    public QuerySQLTest(DataSource dataSource, String statement)
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

        run();
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

            this.resultSet = resultSet;
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
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

    public ResultSet getResultSet()
    {
        return resultSet;
    }
}
