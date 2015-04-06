package com.lagopusempire.homes.homeIO;

import com.lagopusempire.homes.home.Home;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author MrZoraman
 */
public interface HomeIO
{
    public void saveHome(Home home);
    public Map<String, Home> loadHomes(UUID uuid);
    public Home loadHome(UUID uuid, String homeName);
    public List<String> getHomeList(UUID uuid);
    public int getHomeCount(UUID uuid);
    public boolean deleteHome(UUID uuid, String homename);
    
    public boolean shouldBeAsync();
    
    public default boolean close() 
    {
        return true;
    }
    
    public default void registerEvents()
    {
    }
    
    public default void unregisterEvents()
    {
    }
    
    public default void onLoad()
    {
    }
}
