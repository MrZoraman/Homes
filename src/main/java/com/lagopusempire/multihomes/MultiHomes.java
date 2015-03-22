package com.lagopusempire.multihomes;

import com.lagopusempire.bukkitlcs.BukkitLCS;
import com.lagopusempire.multihomes.commands.*;
import com.lagopusempire.multihomes.commands.user.*;
import com.lagopusempire.multihomes.config.ConfigAccessor;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import com.lagopusempire.multihomes.homeIO.database.DBHomeIO;
import com.lagopusempire.multihomes.messages.Messages;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class MultiHomes extends JavaPlugin implements ReloadCallback
{
    private final List<LoadStep> loadSteps = new ArrayList<>();
    
    private BukkitLCS commandSystem;
    private HomeIO io;
    private HomeManager homeManager;
    
    @FunctionalInterface
    private interface LoadStep
    {
        public boolean doStep();
    }
    
    public MultiHomes()
    {
        loadSteps.add(this::setupMessages);
        loadSteps.add(this::setupHomeIO);
        loadSteps.add(this::setupHomeManager);
        loadSteps.add(this::setupCommandSystem);
        loadSteps.add(this::setupCommands);
    }
    
    @Override
    public void onEnable()
    {
        reload(this);
    }
    
    @Override
    public void reloadFinished(boolean result)
    {
        if(result)
        {
            getLogger().info(getDescription().getName() + " has been loaded successfully.");
        }
    }
    
    public void reload(final ReloadCallback callback)
    {
        //SETUP CONFIGURATION (sync)
        setupConfig();
        
        final boolean needToSetupDatabase = PluginConfig.getBoolean(ConfigKeys.USE_DATABASE) 
                || PluginConfig.getBoolean(ConfigKeys.USE_DATABASE);
        
        getServer().getScheduler().runTaskAsynchronously(this, () -> 
        {
            //SETUP DATABASE (async)
            final boolean databaseSetUpSuccessfully = needToSetupDatabase
                    ? setupDatabase() 
                    : true;
            
            //SETUP EVERYTHING ELSE (sync)
            getServer().getScheduler().runTask(this, () ->
            {
                boolean result = true;
                
                result &= databaseSetUpSuccessfully;
                result &= setupSyncItems();
                
                callback.reloadFinished(result);
                
                if(!result)
                    disablePlugin();
            });
        });
    }
    
    private boolean setupSyncItems()
    {
        for(int ii = 0; ii < loadSteps.size(); ii++)
        {
            final boolean result = loadSteps.get(ii).doStep();
            if(result == false)
            {
                return false;
            }
        }
        
        return true;
    }
    
    private void disablePlugin()
    {
        getLogger().severe("Something went wrong while loading " + getDescription().getName() + "! Disabling...");
        getServer().getPluginManager().disablePlugin(this);
    }
    
    private void setupConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();

        PluginConfig.setConfig(getConfig());
    }
    
    private boolean setupMessages()
    {
        final String fileName = "messages.yml";
        final File messagesFile = new File(getDataFolder(), fileName);
        try
        {
            messagesFile.createNewFile();
        }
        catch (IOException ex)
        {
            getLogger().severe("Failed to create messages file!");
            ex.printStackTrace();
            return false;
        }
        final ConfigAccessor messages = new ConfigAccessor(this, fileName);
        messages.getConfig().options().copyDefaults(true);
        messages.saveConfig();
        
        Messages.setMessages(messages.getConfig());
        
        return true;
    }
    
    private boolean setupHomeIO()
    {
        if(PluginConfig.getBoolean(ConfigKeys.USE_DATABASE))
        {
            this.io = new DBHomeIO(this);
            return true;
        }
        else
        {
            getLogger().severe("Flatfile home io not implemented yet!");
            return false;
        }
    }
    
    private boolean setupHomeManager()
    {
        this.homeManager = new HomeManager(io);
        return true;
    }
    
    private boolean setupCommandSystem()
    {
        commandSystem = new BukkitLCS();
        
        getCommand("home").setExecutor(commandSystem);
        getCommand("sethome").setExecutor(commandSystem);
        
        return true;
    }
    
    private boolean setupCommands()
    {
        commandSystem.registerCommand("{home set}|sethome", new SetHomeCommand(homeManager));
        commandSystem.registerCommand("home reload", new ReloadCommand(this));
        return true;
    }

    private boolean setupDatabase()
    {
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
        
        return true;
    }
}
