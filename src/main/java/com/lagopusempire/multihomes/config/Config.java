package com.lagopusempire.multihomes.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author MrZoraman
 */
public class Config
{
    private Config() { }
    
    private static FileConfiguration config;
    
    public static void setConfig(FileConfiguration config)
    {
        Config.config = config;
    }
    
    public static boolean getValue(ConfigKeys key)
    {
        return config.getBoolean(key.getKey());
    }
}
