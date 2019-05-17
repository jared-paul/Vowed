package net.vowed.api.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionPoolManager
{

    public HikariDataSource dataSource;
    public HikariConfig hikariConfig;

    private String hostname;
    private String port;
    private String database;
    private String username;
    private String password;

    private int minimumConnections;
    private int maximumConnections;
    private long connectionTimeout;
    private long leakDetection;
    private String testQuery;

    public ConnectionPoolManager()
    {
        init();
        setupPool();
    }

    private void init()
    {
        hostname = "localhost";
        port = "3306";
        database = "vowed";
        username = "root";
        password = "YessamLor8";
        maximumConnections = 10;
        connectionTimeout = 6000;
        leakDetection = 100;
        testQuery = "SELECT 1";
    }

    private void setupPool()
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(
                "jdbc:mysql://" +
                        hostname +
                        ":" +
                        port +
                        "/" +
                        database
        );
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(maximumConnections);
        config.setIdleTimeout(120000);
        config.setConnectionTimeout(connectionTimeout);
        config.setLeakDetectionThreshold(leakDetection);
        config.setMinimumIdle(2);

        config.setConnectionTestQuery(testQuery);

        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }

    public HikariDataSource getDataSource()
    {
        return dataSource;
    }

    public void close(@Nullable Connection conn, @Nullable PreparedStatement ps, @Nullable ResultSet res)
    {
        if (conn != null) try
        {
            conn.close();
        } catch (SQLException ignored)
        {
            ignored.printStackTrace();
        }
        if (ps != null) try
        {
            ps.close();
        } catch (SQLException ignored)
        {
            ignored.printStackTrace();
        }
        if (res != null) try
        {
            res.close();
        } catch (SQLException ignored)
        {
            ignored.printStackTrace();
        }
    }

    public void closePool()
    {
        if (dataSource != null && !dataSource.isClosed())
        {
            dataSource.close();
        }
    }

}
