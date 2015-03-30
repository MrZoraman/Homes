package com.lagopusempire.multihomes.homeIO;

import com.lagopusempire.multihomes.home.Home;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author MrZoraman
 */
public interface HomeIO
{
    public boolean saveHome(Home home);
    public Map<String, Home> loadHomes(UUID uuid);
    public Home loadHome(UUID uuid, String homeName);
    public List<String> getHomeList(UUID uuid);
    public int getHomeCount(UUID uuid);
    public boolean deleteHome(UUID uuid, String homename);
    
    public default boolean close() 
    {
        return true;
    }
}
