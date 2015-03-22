package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class DatabaseSetup
{
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    
    private final List<DbSetupStep> steps = new ArrayList<>();
    private final JavaPlugin plugin;
    private final String url;
    private final String user;
    private final String password;
    
    private final Logger logger;
    
    private volatile int schemaVersion = 1;
    
    @FunctionalInterface
    private interface DbSetupStep
    {
        boolean doStep(Connection conn);
    }
    
    public DatabaseSetup(JavaPlugin plugin, String databaseString, String user, String password)
    {
        this.plugin = plugin;
        this.url = databaseString;
        this.user = user;
        this.password = password;
        
        this.schemaVersion = PluginConfig.getInt(ConfigKeys.SCHEMA_VERSION);
        
        this.logger = plugin.getLogger();
        
        addSteps();
    }
    
    private void addSteps()
    {
        steps.add((conn) ->
        {
            final String query = Scripts.getScript(ScriptKeys.CREATE_HOMES_TABLE);
            
            try (PreparedStatement statement = conn.prepareStatement(query))
            {
                statement.executeUpdate();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                return false;
            }
            
            logger.info("Database created successfully.");
            return true;
        });
    }
    
    public boolean setup()
    {
        try
        {
            Class.forName(MYSQL_DRIVER);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        
        final Connection conn;
        try
        {
            logger.info("Connecting to '" + url + "'...");
            conn = DriverManager.getConnection(url, user, password);
            logger.info("Connection established successfully.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        int ii = schemaVersion;
        for(; ii < steps.size(); ii++)
        {
            final boolean result = steps.get(ii).doStep(conn);
            if(result == false) return false;
        }
        schemaVersion = ii;
        
        return true;
    }
    
    public boolean postSetup()
    {
        PluginConfig.setInt(ConfigKeys.SCHEMA_VERSION, schemaVersion);
        PluginConfig.save();
        return true;
    }
}
