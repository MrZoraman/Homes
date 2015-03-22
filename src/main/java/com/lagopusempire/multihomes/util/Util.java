package com.lagopusempire.multihomes.util;

import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_DATABASE;
import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_HOST;
import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_PASSWORD;
import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_PORT;
import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_USERNAME;
import com.lagopusempire.multihomes.config.PluginConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author MrZoraman
 */
public class Util
{
    public static Connection createConnection() throws SQLException
    {
        String url = getDatabaseURL();
        final String username = PluginConfig.getString(MYSQL_USERNAME);
        final String password = PluginConfig.getString(MYSQL_PASSWORD);
        
        return DriverManager.getConnection(url, username, password);
    }
    
    public static String getDatabaseURL()
    {
        final String host = PluginConfig.getString(MYSQL_HOST);
        final String port = PluginConfig.getString(MYSQL_PORT);
        final String database = PluginConfig.getString(MYSQL_DATABASE);
        
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }
}
