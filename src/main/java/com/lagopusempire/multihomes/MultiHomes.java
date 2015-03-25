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
import java.sql.Connection;
import java.util.Set;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class MultiHomes extends JavaPlugin implements LoadCallback
{
    private Loader loader;
    private BukkitLCS commandSystem;
    private HomeIO io;
    private HomeManager homeManager;
    private DatabaseSetup databaseSetup;
    private Connection conn;
    private Set<String> registeredBukkitCommands;

    private volatile boolean loaded = false;

    @Override
    public void onEnable()
    {
        reload(this);
    }

    public void reload(final LoadCallback callback)
    {
        loaded = false;
        
        setupConfig();
        
        loader = new Loader(this);
        loader.addStep(this::unloadDb);
        loader.addStep(this::unregisterEvents);
        loader.addStep(this::setupMessages);
        loader.addStep(this::setupRegisteredBukkitCommands);
        loader.addStep(this::setupNotLoadedCommand);
        if (needToSetupDatabase())
        {
            loader.addStep(this::setupScripts);
            loader.addStep(this::setupDbSetup);
            loader.addAsyncStep(this::setupDatabase);
            loader.addStep(this::setupPostDb);
        }
        loader.addStep(this::setupHomeIO);
        loader.addStep(this::setupHomeManager);
        loader.addStep(this::setupEvents);
        loader.addStep(this::loadOnlinePlayers);
        loader.addStep(this::setupCommandSystem);
        loader.addStep(this::setupCommands);
        
        loader.load(this);
    }

    @Override
    public void reloadFinished(boolean result)
    {
        if (result)
        {
            getLogger().info(getDescription().getName() + " has been loaded successfully.");
            loaded = true;
        }
        else
        {
            disablePlugin();
        }
    }

    public void disablePlugin()
    {
        getLogger().severe("Something went wrong in " + getDescription().getName() + "! Disabling...");
        getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable()
    {
        unloadDb();
    }

    private boolean unloadDb()
    {
        if (io != null)
        {
            return io.close();
        }
        return true;
    }
    
    private void setupConfig()
    {
        reloadConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();

        PluginConfig.setConfig(getConfig());
        PluginConfig.howToSave(this::saveConfig);
    }

    private boolean unregisterEvents()
    {
        if (homeManager != null)
        {
            HandlerList.unregisterAll(homeManager);
        }
        return true;
    }

    private boolean setupMessages()
    {
        final ConfigAccessor messages = createYamlFile("messages.yml");
        if (messages == null)
        {
            return false;
        }

        Messages.setMessages(messages.getConfig());

        return true;
    }
    
    private boolean setupRegisteredBukkitCommands()
    {
        registeredBukkitCommands = getDescription().getCommands().keySet();
        return true;
    }
    
    private boolean setupNotLoadedCommand()
    {
        registeredBukkitCommands.forEach((command) -> getCommand(command).setExecutor(new NotLoadedCommand()));
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

    private boolean setupPostDb()
    {
        final boolean success = databaseSetup.postSetup();
        if (!success)
        {
            return false;
        }

        this.conn = databaseSetup.getConnection();
        return true;
    }

    private boolean setupHomeIO()
    {
        if (needToSetupDatabase())
        {
            this.io = new DBHomeIO(this, conn);
            return true;
        }
        else
        {
            final ConfigAccessor homes = createYamlFile("homes.yml");
            if (homes == null)
            {
                return false;
            }

            this.io = new FlatfileHomeIO(homes);
            return true;
        }
    }

    private boolean setupHomeManager()
    {
        this.homeManager = new HomeManager(this, io);
        return true;
    }

    private boolean setupEvents()
    {
        getServer().getPluginManager().registerEvents(homeManager, this);
        return true;
    }

    private boolean loadOnlinePlayers()
    {
        boolean success = true;
        success &= homeManager.loadOnlinePlayerMaps();
        success &= homeManager.loadOnlinePlayerHomes();
        return success;
    }

    private boolean setupCommandSystem()
    {
        commandSystem = new BukkitLCS();
        registeredBukkitCommands.forEach((command) -> getCommand(command).setExecutor(commandSystem));

        return true;
    }

    private boolean setupCommands()
    {
        commandSystem.registerCommand("home reload", new ReloadCommand(this));
        
        commandSystem.registerCommand("{home set}|sethome", new SetHomeCommand(this, homeManager));
        commandSystem.registerCommand("home", new GoHomeCommand(this, homeManager));
        commandSystem.registerCommand("home list", new ListHomesCommand(this, homeManager));
        return true;
    }

    private ConfigAccessor createYamlFile(String fileName)
    {
        final File file = new File(getDataFolder(), fileName);
        try
        {
            file.createNewFile();
        }
        catch (IOException ex)
        {
            getLogger().severe("Failed to create " + fileName + "!");
            ex.printStackTrace();
            return null;
        }
        final ConfigAccessor config = new ConfigAccessor(this, fileName);
        config.getConfig().options().copyDefaults(true);
        config.saveConfig();

        return config;
    }
    
    private boolean needToSetupDatabase()
    {
        return PluginConfig.getBoolean(ConfigKeys.USE_DATABASE);
    }

    public boolean isLoaded()
    {
        return loaded;
    }
}
