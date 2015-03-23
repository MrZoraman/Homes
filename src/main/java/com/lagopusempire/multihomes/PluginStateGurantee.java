package com.lagopusempire.multihomes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 *
 * @author MrZoraman
 */
public class PluginStateGurantee implements Listener
{
    private final MultiHomes plugin;
    
    public PluginStateGurantee(MultiHomes plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerJoinAsync(AsyncPlayerPreLoginEvent event)
    {
        if(!plugin.isLoaded())
        {
            plugin.getLogger().warning("Prventing player " + event.getName() + " from joining the server because multihomes isn't loaded yet.");
            event.setKickMessage("Server is still loading! Please try logging in again.");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }
}
