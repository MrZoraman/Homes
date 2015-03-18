package com.lagopusempire.multihomes;

import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import java.util.HashMap;
import java.util.Map;
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
        
    }
}
