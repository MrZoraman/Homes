package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.util.Util;
import java.sql.Connection;
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
    
    private final Logger logger;
    
    private volatile int schemaVersion = 1;
    
    private Connection conn;
    
    @FunctionalInterface
    private interface DbSetupStep
    {
        boolean doStep();
    }
    
    public DatabaseSetup(JavaPlugin plugin)
    {
        this.schemaVersion = PluginConfig.getInt(ConfigKeys.SCHEMA_VERSION);
        
        this.logger = plugin.getLogger();
        
        addSteps();
    }
    
    private void addSteps()
    {
        steps.add(() ->
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
            
            logger.info("Table created successfully.");
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
        
        try
        {
            System.out.println("sleeping for 10 seconds on thread " + Thread.currentThread().getId());
            Thread.sleep(10000);
            logger.info("Connecting to '" + Util.getDatabaseURL() + "'...");
            conn = Util.createConnection();
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
            final boolean result = steps.get(ii).doStep();
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
    
    public Connection getConnection()
    {
        return conn;
    }
}
