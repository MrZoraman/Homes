package com.lagopusempire.homes.home;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author MrZoraman
 */
public class Coordinates
{
    public boolean isEmpty = false;

    public double x, y, z;
    public float yaw, pitch;

    public Coordinates(Location loc)
    {
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.pitch = loc.getPitch();
        this.yaw = loc.getYaw();
    }
    
    public Coordinates() { }

    public Location toLoc(String worldName)
    {
        if (isEmpty)
        {
            return null;
        }

        final World world = Bukkit.getWorld(worldName);
        if (world == null)
        {
            return null;
        }

        return toLoc(world);
    }
    
    public Location toLoc(World world)
    {
        return new Location(world, x, y, z, yaw, pitch);
    }
}
