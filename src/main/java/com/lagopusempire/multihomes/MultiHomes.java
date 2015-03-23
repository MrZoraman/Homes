package com.lagopusempire.multihomes;

import com.lagopusempire.multihomes.load.LoadCallback;
import com.lagopusempire.bukkitlcs.BukkitLCS;
import com.lagopusempire.multihomes.commands.*;
import com.lagopusempire.multihomes.commands.user.*;
import com.lagopusempire.multihomes.config.ConfigAccessor;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import com.lagopusempire.multihomes.homeIO.database.DBHomeIO;
import com.lagopusempire.multihomes.homeIO.database.DatabaseSetup;
import com.lagopusempire.multihomes.homeIO.database.Scripts;
import com.lagopusempire.multihomes.homeIO.flatfile.FlatfileHomeIO;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.load.Loader;
import java.io.File;
import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import org.bukkit.event.HandlerList;

/**
 *
 * @author MrZoraman
 */
public class MultiHomes extends JavaPlugin implements LoadCallback
{
    private final Loader loader;
    
    private BukkitLCS commandSystem;
    private HomeIO io;
    private HomeManager homeManager;
    private DatabaseSetup databaseSetup;
    private Connection conn;
    private PluginStateGurantee stateGurantee;
    
    private volatile boolean loaded = false;
    
    public MultiHomes()
    {
        super();
        loader = new Loader(this);
    }
    
    @Override
    public void onEnable()
    {
        loader.addStep(this::unloadDb);
        loader.addStep(this::unregisterEvents);
        loader.addStep(this::setupConfig);
        loader.addStep(this::setupMessages);
        loader.addStep(this::registerGuranteeListener);
        loader.addStep(this::setupScripts);
        loader.addStep(this::setupDbSetup);
        loader.addAsyncStep(this::setupDatabase);
        loader.addStep(this::setupPostDb);
        loader.addStep(this::setupHomeIO);
        loader.addStep(this::setupHomeManager);
        loader.addStep(this::setupEvents);
        loader.addStep(this::loadOnlinePlayers);
        loader.addStep(this::setupCommandSystem);
        loader.addStep(this::setupCommands);
        
        reload(this);
    }
    
    private boolean setupCommands()
    {
        commandSystem.registerCommand("{home set}|sethome", new SetHomeCommand(this, homeManager));
        commandSystem.registerCommand("home reload", new ReloadCommand(this));
        return true;
    }
    
    @Override
    public void onDisable()
    {
        unloadDb();
    }
    
    @Override
    public void reloadFinished(boolean result)
    {
        if(result)
        {
            getLogger().info(getDescription().getName() + " has been loaded successfully.");
            loaded = true;
        }
        else
        {
            disablePlugin();
        }
    }
    
    public void reload(final LoadCallback callback)
    {
        loader.load(this);
    }
    
    public void disablePlugin()
    {
        getLogger().severe("Something went wrong in " + getDescription().getName() + "! Disabling...");
        getServer().getPluginManager().disablePlugin(this);
    }
    
    private boolean loadOnlinePlayers()
    {
        boolean success = true;
        success &= homeManager.loadOnlinePlayerMaps();
        success &= homeManager.loadOnlinePlayerHomes();
        return success;
    }
    
    private boolean setupConfig()
    {
        reloadConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        PluginConfig.setConfig(getConfig());
        PluginConfig.howToSave(this::saveConfig);
        
        return true;
    }
    
    private boolean registerGuranteeListener()
    {
        if(stateGurantee == null)
        {
            stateGurantee = new PluginStateGurantee(this);
        }
        
        getServer().getPluginManager().registerEvents(stateGurantee, this);
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
            this.io = new DBHomeIO(this, conn);
            return true;
        }
        else
        {
            final String fileName = "homes.yml";
            final File homesFile = new File(getDataFolder(), fileName);
            try
            {
                homesFile.createNewFile();
            }
            catch (IOException ex)
            {
                getLogger().severe("Failed to create homes file!");
                ex.printStackTrace();
                return false;
            }
            final ConfigAccessor homes = new ConfigAccessor(this, fileName);
            homes.getConfig().options().copyDefaults(true);
            homes.saveConfig();
            
            this.io = new FlatfileHomeIO(homes);
            return true;
        }
    }
    
    private boolean setupHomeManager()
    {
        this.homeManager = new HomeManager(this, io);
        return true;
    }
    
    private boolean setupCommandSystem()
    {
        commandSystem = new BukkitLCS();
        
        getCommand("home").setExecutor(commandSystem);
        getCommand("sethome").setExecutor(commandSystem);
        
        return true;
    }
    
    private boolean setupScripts()
    {
        Scripts.setPlugin(this);
        return true;
    }
    
    private boolean setupDbSetup()
    {
        databaseSetup = new DatabaseSetup(this);
        return true;
    }

    private boolean setupDatabase()
    {
        return databaseSetup.setup();
    }
    
    private boolean setupEvents()
    {
        getServer().getPluginManager().registerEvents(homeManager, this);
        return true;
    }
    
    private boolean unregisterEvents()
    {
        if(homeManager != null)
        {
            HandlerList.unregisterAll(homeManager);
        }
        
        if(stateGurantee != null)
        {
            HandlerList.unregisterAll(stateGurantee);
        }
        return true;
    }
    
    private boolean setupPostDb()
    {
        final boolean success = databaseSetup.postSetup();
        if(!success) return false;
        
        this.conn = databaseSetup.getConnection();
        return true;
    }
    
    private boolean unloadDb()
    {
        if(io != null)
        {
            return io.close();
        }
        return true;
    }

    public boolean isLoaded()
    {
        return loaded;
    }
}
