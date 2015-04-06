package com.lagopusempire.homes.home;

import org.bukkit.Location;

/**
 *
 * @author MrZoraman
 */
public class HomeLoadPackage
{
    public final Location loc;
    public final LoadResult loadResult;
    
    HomeLoadPackage(Location loc, LoadResult loadResult)
    {
        this.loc = loc;
        this.loadResult = loadResult;
    }
}
