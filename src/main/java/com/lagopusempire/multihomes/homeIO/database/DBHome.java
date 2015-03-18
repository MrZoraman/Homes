package com.lagopusempire.multihomes.homeIO.database;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.LoadResult;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @author MrZoraman
 */
@Entity()
@Table(name="multihomes")
public class DBHome
{
    @Id
    private int id;
    
    @Length(max=36)
    @NotEmpty
    private String owner;
    
    @NotNull
    private double x;
    
    @NotNull
    private double y;
    
    @NotNull
    private double z;
    
    @NotNull
    private float yaw;
    
    @NotNull
    private float pitch;
    
    @NotEmpty
    private String world_name;
    
    @NotEmpty
    private String home_name;
    
    public Home toHome()
    {
        final World world = Bukkit.getWorld(world_name);
        final UUID ownerUUID = UUID.fromString(owner);
        if(world == null)
        {
            return new Home(ownerUUID, home_name, LoadResult.NO_WORLD);
        }
        
        final Location loc = new Location(world, x, y, z, yaw, pitch);
        
        return new Home(ownerUUID, home_name, loc);
    }
    
    public void writeData(Home home)
    {
        this.owner = home.getOwner().toString();
        this.x = home.getLoc().getX();
        this.y = home.getLoc().getY();
        this.z = home.getLoc().getZ();
        this.yaw = home.getLoc().getYaw();
        this.pitch = home.getLoc().getPitch();
        this.world_name = home.getLoc().getWorld().getName();
        this.home_name = home.getName();
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

    public float getYaw()
    {
        return yaw;
    }

    public void setYaw(float yaw)
    {
        this.yaw = yaw;
    }

    public float getPitch()
    {
        return pitch;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getWorld_name()
    {
        return world_name;
    }

    public void setWorld_name(String world_name)
    {
        this.world_name = world_name;
    }

    public String getHome_name()
    {
        return home_name;
    }

    public void setHome_name(String home_name)
    {
        this.home_name = home_name;
    }
}
