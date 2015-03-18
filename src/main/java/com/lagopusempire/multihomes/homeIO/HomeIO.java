package com.lagopusempire.multihomes.homeIO;

import com.lagopusempire.multihomes.Home;
import java.util.UUID;

/**
 *
 * @author MrZoraman
 */
public interface HomeIO
{
    public void saveHome(Home home);
    public void loadHomes(UUID uuid);
}
