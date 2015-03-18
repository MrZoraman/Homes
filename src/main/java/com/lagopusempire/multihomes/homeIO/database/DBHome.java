package com.lagopusempire.multihomes.homeIO.database;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import com.lagopusempire.multihomes.home.Home;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.bukkit.Bukkit;
import org.bukkit.Location;

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
    private String worldName;
    
    @NotEmpty
    private String homeName;
    
    public DBHome(Home home)
    {
        this.owner = home.getOwner().toString();
        this.x = home.getLoc().getX();
        this.y = home.getLoc().getY();
        this.z = home.getLoc().getZ();
        this.yaw = home.getLoc().getYaw();
        this.pitch = home.getLoc().getPitch();
        this.worldName = home.getLoc().getWorld().getName();
        this.homeName = home.getName();
    }
    
    public Home toHome()
    {
        return new Home(UUID.fromString(owner), homeName, toLoc());
    }
    
    private Location toLoc()
    {
        return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUUID()
    {
        return owner;
    }

    public void setUUID(String UUID)
    {
        this.owner = UUID;
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

    public String getWorldName()
    {
        return worldName;
    }

    public void setWorldName(String worldName)
    {
        this.worldName = worldName;
    }

    public String getHomeName()
    {
        return homeName;
    }

    public void setHomeName(String homeName)
    {
        this.homeName = homeName;
    }
}
