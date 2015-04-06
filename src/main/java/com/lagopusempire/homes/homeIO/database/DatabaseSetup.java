package com.lagopusempire.homes.homeIO.database;

import com.lagopusempire.homes.config.ConfigKeys;
import com.lagopusempire.homes.config.PluginConfig;
import com.lagopusempire.homes.util.Util;
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
    private final List<DbSetupStep> steps = new ArrayList<>();
    private final Logger logger;
    private final String mysqlDriver;
    
    private volatile int schemaVersion;
    
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
        this.mysqlDriver = PluginConfig.getString(ConfigKeys.MYSQL_DRIVER);
        
        addSteps();
    }
    
    private void addSteps()
    {
        steps.add(() ->
        {
            try
            {
                PreparedStatement statement = conn.prepareStatement(Scripts.getScript(ScriptKeys.CREATE_UUIDS_TABLE));
                statement.executeUpdate();
                statement.close();
                logger.info("homes_uuids table created successfully.");
                
                statement = conn.prepareStatement(Scripts.getScript(ScriptKeys.CREATE_WORLDS_TABLE));
                statement.executeUpdate();
                statement.close();
                logger.info("homes_worlds table created successfully.");
                        
                statement = conn.prepareStatement(Scripts.getScript(ScriptKeys.CREATE_HOMES_TABLE));
                statement.executeUpdate();
                statement.close();
                logger.info("homes table created successfully.");
                
                statement = conn.prepareStatement(Scripts.getScript(ScriptKeys.CREATE_ADD_UUID_PROC));
                statement.executeUpdate();
                statement.close();
                logger.info("homes_add_uuid_proc procedure created successfully.");
                
                statement = conn.prepareStatement(Scripts.getScript(ScriptKeys.CREATE_ADD_WORLD_PROC));
                statement.executeUpdate();
                statement.close();
                logger.info("homes_add_world_proc procedure created successfully.");
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                return false;
            }
            
            return true;
        });
    }
    
    public boolean setup()
    {
        try
        {
            Class.forName(mysqlDriver);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        
        try
        {
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
