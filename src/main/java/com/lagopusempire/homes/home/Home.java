package com.lagopusempire.homes.home;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author MrZoraman
 */
public class Home
{
    private String name;
    
    private final UUID owner;
    
    private Coordinates coords = null;
    private String worldName = null;
    
    public Home(UUID owner, String name)
    {
        this.owner = owner;
        this.name = name;
    }
    
    public Home(UUID owner, String name, Coordinates coords, String worldName)
    {
        this(owner, name);
        this.coords = coords;
        this.worldName = worldName;
    }
    
    public HomeLoadPackage getHomeLoadPackage()
    {
        if(coords == null)
            return new HomeLoadPackage(null, LoadResult.DOES_NOT_EXIST);
        
        final World world = Bukkit.getWorld(worldName);
        if(world == null)
            return new HomeLoadPackage(null, LoadResult.NO_WORLD);
        
        return new HomeLoadPackage(coords.toLoc(world), LoadResult.SUCCESS);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Coordinates getCoords()
    {
        return coords;
    }

    public void setCoords(Coordinates coords)
    {
        this.coords = coords;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public String getWorldName()
    {
        return worldName;
    }

    public void setWorldName(String worldName)
    {
        this.worldName = worldName;
    }
}
