package com.lagopusempire.multihomes.homeIO;

import com.lagopusempire.multihomes.home.Home;
import java.util.UUID;

/**
 *
 * @author MrZoraman
 */
public interface HomeIO
{
    public void saveHome(Home home, Runnable callback);
    public void loadHomes(UUID uuid, HomesLoadedCallback callback);
    public void loadHome(UUID uuid, String homeName, HomeLoadedCallback callback);
    public void getHomeList(UUID uuid, HomeListLoadedCallback callback);
}
