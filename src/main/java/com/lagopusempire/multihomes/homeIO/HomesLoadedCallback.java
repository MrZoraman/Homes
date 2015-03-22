package com.lagopusempire.multihomes.homeIO;

import com.lagopusempire.multihomes.home.Home;
import java.util.Map;

/**
 *
 * @author MrZoraman
 */
@FunctionalInterface
public interface HomesLoadedCallback
{
    public void homesLoaded(Map<String, Home> homes);
}
