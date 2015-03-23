package com.lagopusempire.multihomes.homeIO.flatfile;

import com.lagopusempire.multihomes.config.ConfigAccessor;
import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.LoadResult;
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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
        final Location loc = home.getLoc();
        final String ownerName = home.getOwner().toString();
        
        final String path = ownerName + "." + home.getName() + ".";
        
        config.set(path + "x", loc.getX());
        config.set(path + "y", loc.getY());
        config.set(path + "z", loc.getZ());
        config.set(path + "yaw", loc.getYaw());
        config.set(path + "pitch", loc.getPitch());
        config.set(path + "worldName", loc.getWorld().getName());
        
        homesFile.saveConfig();
        
        if(callback != null)
            callback.run();
    }

    @Override
    public void loadHomes(UUID uuid, HomesLoadedCallback callback)
    {
        final String ownerName = uuid.toString();
        
        final Map<String, Home> homes = new HashMap<>();
        
        final Set<String> homeNames = config.getConfigurationSection(ownerName).getKeys(false);
        
        homeNames.forEach((homeName) -> homes.put(homeName, loadHome(uuid, homeName)));
        
        callback.homesLoaded(homes);
    }

    @Override
    public void loadHome(UUID uuid, String homeName, HomeLoadedCallback callback)
    {
        final Home home = loadHome(uuid, homeName);
        
        callback.homeLoaded(home);
    }

    @Override
    public void getHomeList(UUID uuid, HomeListLoadedCallback callback)
    {
        final Set<String> homeNames = config.getConfigurationSection(uuid.toString()).getKeys(false);
        final List<String> homeList = new ArrayList<>(homeNames);
        Collections.sort(homeList);
        callback.homeListLoaded(homeList);
    }
    
    private Home loadHome(UUID uuid, String homeName)
    {
        final String path = uuid.toString() + "." + homeName + ".";
        
        final World world = Bukkit.getWorld(config.getString(path + "worldName"));
        if(world == null)
        {
            return new Home(uuid, homeName, LoadResult.NO_WORLD);
        }
        
        if(!config.contains(path))
        {
            return new Home(uuid, homeName, LoadResult.DOES_NOT_EXIST);
        }
        
        final double x = config.getDouble(path + "x");
        final double y = config.getDouble(path + "y");
        final double z = config.getDouble(path + "z");
        final float yaw = (float) config.getDouble(path + "yaw");
        final float pitch = (float) config.getDouble(path + "pitch");

        final Location loc = new Location(world, x, y, z, yaw, pitch);

        return new Home(uuid, homeName, loc);
    }
}
