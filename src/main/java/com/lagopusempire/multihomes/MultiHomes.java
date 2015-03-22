package com.lagopusempire.multihomes;

import com.lagopusempire.bukkitlcs.BukkitLCS;
import com.lagopusempire.multihomes.commands.user.SetHomeCommand;
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
import javax.persistence.PersistenceException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class MultiHomes extends JavaPlugin
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
        loadSteps.add(this::setupConfig);
        loadSteps.add(this::setupDatabase);
        loadSteps.add(this::setupMessages);
        loadSteps.add(this::setupHomeIO);
        loadSteps.add(this::setupHomeManager);
        loadSteps.add(this::setupCommandSystem);
        loadSteps.add(this::setupCommands);
    }
    
    @Override
    public void onEnable()
    {
        final boolean success = reload();
        if(success == false)
        {
            getLogger().severe("Something went wrong while loading " + getDescription().getName() + "! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public boolean reload()
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
    
    private boolean setupConfig()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();

        PluginConfig.setConfig(getConfig());
        
        return true;
    }
    
    private boolean setupDatabase()
    {
        if(PluginConfig.getBoolean(ConfigKeys.USE_DATABASE) || PluginConfig.getBoolean(ConfigKeys.USE_DATABASE))
        {
            return configurePersistance();
        }
        
        return true;
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
        return true;
    }

    private boolean configurePersistance()
    {
        try
        {
            getDatabase().find(com.lagopusempire.multihomes.homeIO.database.DBHome.class).findRowCount();
        }
        catch (PersistenceException ignored)
        {
            getLogger().info("Installing database for " + getDescription().getName() + " due to first time usage");
            try
            {
                installDDL();
                System.out.println("done");
            }
            catch (RuntimeException e)
            {
                getLogger().severe(e.getMessage());
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public List<Class<?>> getDatabaseClasses()
    {
        List<Class<?>> list = new ArrayList<>();
        list.add(com.lagopusempire.multihomes.homeIO.database.DBHome.class);
        return list;
    }
}
