package com.lagopusempire.homes.homeIO.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class Scripts
{
    private Scripts() { }
    
    private static final Map<ScriptKeys, String> scriptCache = new ConcurrentHashMap<>();
    private static JavaPlugin plugin;
    
    public static void setPlugin(JavaPlugin plugin)
    {
        Scripts.plugin = plugin;
    }
    
    public static String getScript(ScriptKeys key)
    {
        if(scriptCache.containsKey(key))
        {
            return scriptCache.get(key);
        }
        else
        {
            final InputStream queryStream = plugin.getResource(key.getKey());//bukkit method called asynchornously. Hope getResource is ok...
            final String query = inputStreamToString(queryStream);
            try
            {
                queryStream.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                return null;
            }
            scriptCache.put(key, query);
            return query;
        }
    }
    
    private static String inputStreamToString(InputStream stream)
    {
        try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}
