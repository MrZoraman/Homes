package com.lagopusempire.multihomes.homeIO;

import com.lagopusempire.multihomes.home.Home;

/**
 *
 * @author MrZoraman
 */
@FunctionalInterface
public interface HomeLoadedCallback
{
    public void homeLoaded(Home home);
}
