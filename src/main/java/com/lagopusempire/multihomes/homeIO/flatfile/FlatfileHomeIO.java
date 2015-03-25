package com.lagopusempire.multihomes.homeIO.flatfile;

import com.lagopusempire.multihomes.config.ConfigAccessor;
import com.lagopusempire.multihomes.home.Coordinates;
import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.homeIO.HomeCountCallback;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import com.lagopusempire.multihomes.homeIO.HomeListLoadedCallback;
import com.lagopusempire.multihomes.homeIO.HomeLoadedCallback;
import com.lagopusempire.multihomes.homeIO.HomesLoadedCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author MrZoraman
 */
public class FlatfileHomeIO implements HomeIO
{
    private final ConfigAccessor homesFile;
    final FileConfiguration config;
    
    public FlatfileHomeIO(ConfigAccessor homesFile)
    {
        this.homesFile = homesFile;
        this.config = homesFile.getConfig();
    }
    
    @Override
    public void saveHome(Home home, Runnable callback)
    {
        final Coordinates coords = home.getCoords();
        final String ownerName = home.getOwner().toString();
        
        final String path = ownerName + "." + home.getName() + ".";
        
        config.set(path + "x", coords.x);
        config.set(path + "y", coords.y);
        config.set(path + "z", coords.z);
        config.set(path + "yaw", coords.yaw);
        config.set(path + "pitch", coords.pitch);
        config.set(path + "worldName", home.getWorldName());
        
        homesFile.saveConfig();
        
        if(callback != null)
            callback.run();
    }

    @Override
    public void loadHomes(UUID uuid, HomesLoadedCallback callback)
    {
        final String ownerName = uuid.toString();
        
        final Map<String, Home> homes = new HashMap<>();
        
        final ConfigurationSection section = config.getConfigurationSection(ownerName);
        if(section != null)
        {
            final Set<String> homeNames = section.getKeys(false);

            homeNames.forEach((homeName) -> homes.put(homeName, getHome(uuid, homeName)));
        }
        
        callback.homesLoaded(homes);
    }

    @Override
    public void loadHome(UUID uuid, String homeName, HomeLoadedCallback callback)
    {
        final Home home = getHome(uuid, homeName);
        
        callback.homeLoaded(home);
    }

    @Override
    public void getHomeList(UUID uuid, HomeListLoadedCallback callback)
    {
        final ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        if(section != null)
        {
            final Set<String> homeNames = config.getConfigurationSection(uuid.toString()).getKeys(false);
            final List<String> homeList = new ArrayList<>(homeNames);
            Collections.sort(homeList);
            callback.homeListLoaded(homeList);
        }
        else
        {
            callback.homeListLoaded(new ArrayList<>());
        }
    }

    @Override
    public void getHomeCount(UUID uuid, HomeCountCallback callback, boolean syncCallback)
    {
        final ConfigurationSection section = config.getConfigurationSection(uuid.toString());
        callback.gotHomeCount(section == null 
                ? 0 
                : section.getKeys(false).size()
        );
    }
    
    private Home getHome(UUID uuid, String homeName)
    {
        final String path = uuid.toString() + "." + homeName + ".";
        
        if(!config.contains(path))
        {
            return new Home(uuid, homeName);
        }
        
        final String worldName = config.getString(path + ".worldName");
        
        final Coordinates coords = new Coordinates();
        
        coords.x = config.getDouble(path + "x");
        coords.y = config.getDouble(path + "y");
        coords.z = config.getDouble(path + "z");
        coords.yaw = (float) config.getDouble(path + "yaw");
        coords.pitch = (float) config.getDouble(path + "pitch");

        return new Home(uuid, homeName, coords, worldName);
    }
}
