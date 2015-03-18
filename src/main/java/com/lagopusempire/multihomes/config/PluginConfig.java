package com.lagopusempire.multihomes.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author MrZoraman
 */
public class PluginConfig
{
    private PluginConfig() { }
    
    private static FileConfiguration config;
    
    public static void setConfig(FileConfiguration config)
    {
        PluginConfig.config = config;
    }
    
    public static boolean getBoolean(ConfigKeys key)
    {
        return config.getBoolean(key.getKey());
    }
    
    public static String getString(ConfigKeys key)
    {
        return config.getString(key.getKey());
    }
}
