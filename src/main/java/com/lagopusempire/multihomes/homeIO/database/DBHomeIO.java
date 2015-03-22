package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.LoadResult;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class DBHomeIO implements HomeIO
{
    private final JavaPlugin plugin;
    
    public DBHomeIO(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public void saveHome(Home home)
    {
    }

    @Override
    public Map<String, Home> loadHomes(UUID uuid)
    {
        return null;
    }

    @Override
    public Home loadHome(UUID uuid, String homeName)
    {
        return null;
    }
    
    @Override
    public List<String> getHomeList(UUID uuid)
    {
        return null;
    }
}
