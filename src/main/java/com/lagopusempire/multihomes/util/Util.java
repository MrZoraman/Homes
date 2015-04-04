package com.lagopusempire.multihomes.util;

import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_DATABASE;
import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_HOST;
import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_PASSWORD;
import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_PORT;
import static com.lagopusempire.multihomes.config.ConfigKeys.MYSQL_USERNAME;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class Util
{
    private static final int DB_LOGIN_TIMEOUT = 5;
    
    static
    {
        DriverManager.setLoginTimeout(DB_LOGIN_TIMEOUT);
    }
    
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
    
    public static void sendMessage(Player player, MessageFormatter formatter)
    {
        if(player != null && player.isOnline())
        {
            player.sendMessage(formatter.colorize().toString());
        }
    }
    
    public static MessageFormatter getNoPermsMsg(Permissions perm)
    {
        return Messages.getMessage(MessageKeys.NO_PERMISSION)
                .replace("node", perm.getNode());
    }
    
    public static boolean checkPerms(Player player, Permissions perm)
    {
        if(!perm.check(player))
        {
            Util.sendMessage(player, getNoPermsMsg(perm));
            return false;
        }
        
        return true;
    }
}
