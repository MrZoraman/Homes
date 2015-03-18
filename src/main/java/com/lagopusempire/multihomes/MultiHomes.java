package com.lagopusempire.multihomes;

import com.lagopusempire.multihomes.config.PluginConfig;
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
    }
}
