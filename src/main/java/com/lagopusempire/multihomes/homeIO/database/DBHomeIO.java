package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import java.util.Map;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class DBHomeIO implements HomeIO
{
    private final JavaPlugin plugin;
    
    public DBHomeIO(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public void saveHome(Home home)
    {
        DBHome dbHome = plugin.getDatabase().find(DBHome.class)
                .where()
                .ieq("owner", home.getOwner().toString())
                .ieq("home_name", home.getName())
                .findUnique();
        
        if(dbHome == null)
        {
            
        }
    }

    @Override
    public Map<String, Home> loadHomes(UUID uuid)
    {
        return null;
    }

    @Override
    public Home loadHome(UUID uuid, String homeName)
    {
        return null;
    }
    
    private DBHome toDbHome(Home home)
    {
        DBHome dbHome = new DBHome();
        
        dbHome.setOwner(home.getOwner().toString());
        dbHome.setX(home.getLoc().getX());
        dbHome.setY(home.getLoc().getY());
        dbHome.setZ(home.getLoc().getZ());
        dbHome.setYaw(home.getLoc().getYaw());
        dbHome.setPitch(home.getLoc().getPitch());
        dbHome.setWorld_name(home.getLoc().getWorld().getName());
        dbHome.setHome_name(home.getName());
        
        return dbHome;
    }
}
