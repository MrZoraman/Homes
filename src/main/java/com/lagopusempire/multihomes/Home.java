package com.lagopusempire.multihomes;

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
    
    public Home(UUID owner, Location loc, String name)
    {
        this.owner = owner;
        this.name = name;
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
}
