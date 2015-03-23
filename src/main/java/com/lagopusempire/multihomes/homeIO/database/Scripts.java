package com.lagopusempire.multihomes.homeIO.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class Scripts
{
    private Scripts() { }
    
    private static final Map<ScriptKeys, String> scriptCache = new HashMap<>();
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
            final InputStream queryStream = plugin.getResource(key.getKey());
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
