package com.lagopusempire.homes.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author MrZoraman
 */
public class PluginConfig
{
    private PluginConfig() { }
    
    private static FileConfiguration config;
    private static Runnable howToSave;
    
    public static void setConfig(FileConfiguration config)
    {
        PluginConfig.config = config;
    }
    
    public static void howToSave(Runnable r)
    {
        howToSave = r;
    }
    
    public static void save()
    {
        howToSave.run();
    }
    
    public static boolean getBoolean(ConfigKeys key)
    {
        return config.getBoolean(key.getKey());
    }
    
    public static String getString(ConfigKeys key)
    {
        return config.getString(key.getKey());
    }
    
    public static int getInt(ConfigKeys key)
    {
        return config.getInt(key.getKey());
    }
    
    public static void setInt(ConfigKeys key, int value)
    {
        config.set(key.getKey(), value);
    }
}
