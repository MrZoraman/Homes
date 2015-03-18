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
    public void saveHome(Home home);
    public Map<String, Home> loadHomes(UUID uuid);
    public Home loadHome(UUID uuid, String homeName);
    public List<String> getHomeList(UUID uuid);
}
