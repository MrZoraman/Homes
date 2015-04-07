package com.lagopusempire.homes;

import com.lagopusempire.homes.config.ConfigKeys;
import com.lagopusempire.homes.config.PluginConfig;
import com.lagopusempire.homes.home.Coordinates;
import com.lagopusempire.homes.home.Home;
import com.lagopusempire.homes.home.HomeLoadPackage;
import com.lagopusempire.homes.home.LoadResult;
import com.lagopusempire.homes.homeIO.HomeIO;
import com.lagopusempire.homes.permissions.Permissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 *
 * @author MrZoraman
 */
public class HomeManager implements Listener
{
    private final Map<UUID, Map<String, Home>> homes = new ConcurrentHashMap<>();

    private final HomeIO io;
    private final HomesPlugin plugin;

    public HomeManager(HomesPlugin plugin, HomeIO io)
    {
        this.plugin = plugin;
        this.io = io;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinAsync(AsyncPlayerPreLoginEvent event)
    {
        final UUID uuid = event.getUniqueId();

        addHomeMap(uuid);
        loadAllHomes(uuid);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        final UUID uuid = event.getPlayer().getUniqueId();
        
        homes.remove(uuid);
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if(!Permissions.RETURN_HOME_ON_DEATH.check(event.getPlayer())) return;
        
        final UUID uuid = event.getPlayer().getUniqueId();
        if(!homes.containsKey(uuid)) return;
            
        final Home home = homes.get(uuid).get(PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME));
        if(home == null) return;
        
        final HomeLoadPackage pack = home.getHomeLoadPackage();
        if(pack.loadResult != LoadResult.SUCCESS) return;
        
        event.setRespawnLocation(pack.loc);
    }

    public void setHome(UUID owner, String homeName, Coordinates coords, String worldName)
    {
        final Home home = new Home(owner, homeName, coords, worldName);
        if(homes.containsKey(owner))
            homes.get(owner).put(homeName, home);
        io.saveHome(home);
    }

    public Home getHome(UUID owner, String homeName)
    {
        if (homes.containsKey(owner)) //True if the player is online
        {
            final Home home = homes.get(owner).get(homeName);
            if (home != null)
            {
                return home;
            }
            return new Home(owner, homeName);
        }

        //The player is offline
        return io.loadHome(owner, homeName);
    }

    public List<String> getHomeList(UUID owner)
    {
        if (homes.containsKey(owner))
        {
            final Set<String> homeSet = homes.get(owner).keySet();
            final List<String> homeList = new ArrayList<>(homeSet);
            java.util.Collections.sort(homeList);
            return homeList;
        }

        return io.getHomeList(owner);
    }

    public int getHomeCount(UUID owner)
    {
        if (homes.containsKey(owner))
        {
            return homes.get(owner).size();
        }
        else
        {
            return io.getHomeCount(owner);
        }
    }

    public boolean deleteHome(UUID owner, String homeName)
    {
        if (homes.containsKey(owner))
        {
            homes.get(owner).remove(homeName);
        }

        return io.deleteHome(owner, homeName);
    }
    
    public boolean shouldBeAsync()
    {
        return io.shouldBeAsync();
    }

    private void addHomeMap(UUID uuid)
    {
        if (!homes.containsKey(uuid))
        {
            homes.put(uuid, new HashMap<>());
        }
    }

    boolean loadOnlinePlayerMaps()
    {
        plugin.getServer().getOnlinePlayers().forEach((player) -> addHomeMap(player.getUniqueId()));
        return true;
    }

    boolean loadOnlinePlayerHomes()
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            plugin.getServer().getOnlinePlayers().forEach((player) -> loadAllHomes(player.getUniqueId()));
        });
        return true;
    }

    private void loadAllHomes(UUID uuid)
    {
        homes.get(uuid).putAll(io.loadHomes(uuid));
    }
}
