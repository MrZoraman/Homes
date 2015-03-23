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
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author MrZoraman
 */
public class HomeManager implements Listener
{
    private static final int PLAYER_LOGIN_FAIL_TIMEOUT = 20 * 15;//15 seconds
    
    private final Map<UUID, Map<String, Home>> homes = new HashMap<>();
    
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
        boolean notifiedConsole = false;
        int failCounter = 0;
        //ASYNC
        while(plugin.isEnabled() == false)
        {
            if(!notifiedConsole)
            {
                plugin.getLogger().warning("Prventing player " + event.getName() + " from joining the server because multihomes isn't loaded yet.");
                notifiedConsole = true;
            }
            
            failCounter++;
            
            if(failCounter >= PLAYER_LOGIN_FAIL_TIMEOUT)
            {
                event.setKickMessage("Server is experiencing internal errors. Please try again later.");
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                return;
            }
        }
        
        final UUID uuid = event.getUniqueId();
        
        //the plugin has been loaded at this point
        io.loadHomes(event.getUniqueId(), (loadedHomes) -> 
        {
            //SYNC
            if(homes.get(uuid) == null)
            {
                homes.put(uuid, new HashMap<>());
            }
            
            homes.get(uuid).putAll(loadedHomes);
        });
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setHome(Player player, UUID owner, String homeName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Home getHome(UUID owner, String homeName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<String> getHomeList(UUID owner)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
