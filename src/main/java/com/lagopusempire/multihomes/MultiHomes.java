package com.lagopusempire.multihomes;

import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import javax.persistence.PersistenceException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class MultiHomes extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        reload();
    }

    @Override
    public void onDisable()
    {

    }

    public void reload()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();

        PluginConfig.setConfig(getConfig());
        
        if(PluginConfig.getBoolean(ConfigKeys.USE_DATABASE) || PluginConfig.getBoolean(ConfigKeys.USE_DATABASE))
        {
            setupDatabase();
        }
    }

    private boolean setupDatabase()
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
            }
            catch (RuntimeException e)
            {
                getLogger().severe(e.getMessage());
                return false;
            }
        }
        
        return true;
    }
}
