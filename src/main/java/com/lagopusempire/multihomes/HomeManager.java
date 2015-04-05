package com.lagopusempire.multihomes;

import com.lagopusempire.multihomes.home.Coordinates;
import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.homeIO.HomeIO;
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

/**
 *
 * @author MrZoraman
 */
public class HomeManager implements Listener
{
    private final Map<UUID, Map<String, Home>> homes = new ConcurrentHashMap<>();

    private final HomeIO io;
    private final MultiHomes plugin;

    public HomeManager(MultiHomes plugin, HomeIO io)
    {
        this.plugin = plugin;
        this.io = io;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinAsync(AsyncPlayerPreLoginEvent event)
    {
        final UUID uuid = event.getUniqueId();

        addHomeMap(uuid);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> loadAllHomes(uuid));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        final UUID uuid = event.getPlayer().getUniqueId();
        
        homes.remove(uuid);
    }

    public boolean setHome(UUID owner, String homeName, Coordinates coords, String worldName)
    {
        final Home home = new Home(owner, homeName, coords, worldName);;
        assert(homeName != null);
        assert(home != null);
        if(homes.containsKey(owner))
            homes.get(owner).put(homeName, home);
        return io.saveHome(home);
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
        plugin.getServer().getOnlinePlayers().forEach((player) -> loadAllHomes(player.getUniqueId()));
        return true;
    }

    private void loadAllHomes(UUID uuid)
    {
        homes.get(uuid).putAll(io.loadHomes(uuid));
    }
}
