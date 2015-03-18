package com.lagopusempire.multihomes.home;

import java.util.UUID;
import org.bukkit.Location;

/**
 *
 * @author MrZoraman
 */
public class Home
{
    private String name;
    private Location loc;
    
    private final UUID owner;
    private final LoadResult loadResult;
    
    public Home(UUID owner, String name, LoadResult loadResult)
    {
        this.owner = owner;
        this.name = name;
        this.loadResult = loadResult;
    }
    
    public Home(UUID owner, String name, Location loc)
    {
        this(owner, name, LoadResult.SUCCESS);
        this.loc = loc;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Location getLoc()
    {
        return loc;
    }

    public void setLoc(Location loc)
    {
        this.loc = loc;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public LoadResult getLoadResult()
    {
        return loadResult;
    }
}
