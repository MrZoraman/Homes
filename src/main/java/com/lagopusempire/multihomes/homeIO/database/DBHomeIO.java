package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.LoadResult;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import com.lagopusempire.multihomes.homeIO.HomeListLoadedCallback;
import com.lagopusempire.multihomes.homeIO.HomeLoadedCallback;
import com.lagopusempire.multihomes.homeIO.HomesLoadedCallback;
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
    public void saveHome(Home home, Runnable callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            
            
            plugin.getServer().getScheduler().runTask(plugin, () -> 
            {
                
            });
        });
    }

    @Override
    public void loadHomes(UUID uuid, HomesLoadedCallback callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            
            
            plugin.getServer().getScheduler().runTask(plugin, () -> 
            {
                
            });
        });
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadHome(UUID uuid, String homeName, HomeLoadedCallback callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            
            
            plugin.getServer().getScheduler().runTask(plugin, () -> 
            {
                
            });
        });
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getHomeList(UUID uuid, HomeListLoadedCallback callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            
            
            plugin.getServer().getScheduler().runTask(plugin, () -> 
            {
                
            });
        });
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
