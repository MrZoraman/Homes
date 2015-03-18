package com.lagopusempire.multihomes;

import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.LoadResult;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author MrZoraman
 */
public class PlayerManager implements Listener
{
    private final Map<UUID, Map<String, Home>> homes = new HashMap<>();
    
    private final HomeIO io;
    
    public PlayerManager(HomeIO io)
    {
        this.io = io;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final UUID uuid = event.getPlayer().getUniqueId();
        final Map<String, Home> playersHomes = io.loadHomes(uuid);
        homes.put(uuid, playersHomes);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        homes.remove(event.getPlayer().getUniqueId());
    }
    
    public void setHome(Player player, UUID owner, String homeName)
    {
        Home home = new Home(owner, homeName, player.getLocation());
        io.saveHome(home);
        homes.get(owner).put(homeName, home);
    }
    
    public Home getHome(UUID owner, String homeName)
    {
        if(homes.containsKey(owner)) //True if the player is online
        {
            final Home home = homes.get(owner).get(homeName);
            if(home != null)
            {
                return home;
            }
            
            return new Home(owner, homeName, LoadResult.DOES_NOT_EXIST);
        }
        
        //The player is offline
        return io.loadHome(owner, homeName);
    }
    
    public List<String> getHomeList(UUID owner)
    {
        if(homes.containsKey(owner))
        {
            final Set<String> homeSet = homes.get(owner).keySet();
            final List<String> homeList = new ArrayList<>(homeSet);
            java.util.Collections.sort(homeList);
            return homeList;
        }
        
        return io.getHomeList(owner);
    }
}
