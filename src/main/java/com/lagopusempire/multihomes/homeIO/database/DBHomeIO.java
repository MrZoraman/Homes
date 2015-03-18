package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
            dbHome = new DBHome();
        }
        
        dbHome.writeData(home);
        
        plugin.getDatabase().save(home);
    }

    @Override
    public Map<String, Home> loadHomes(UUID uuid)
    {
        final Set<DBHome> dbHomes = plugin.getDatabase().find(DBHome.class)
                .where()
                .ieq("owner", uuid.toString())
                .findSet();
        
        final Map<String, Home> homes = new HashMap<>();
        
        final Iterator<DBHome> it = dbHomes.iterator();
        while(it.hasNext())
        {
            final Home home = it.next().toHome();
            homes.put(home.getName(), home);
        }
        
        return homes;
    }

    @Override
    public Home loadHome(UUID uuid, String homeName)
    {
        return null;
    }
}
