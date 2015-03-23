package com.lagopusempire.multihomes;

import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
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
            plugin.getLogger().warning("Prventing player " + event.getName() 
                    + " from joining the server because " 
                    + plugin.getDescription().getName() 
                    + " is not loaded yet.");
            
            event.setKickMessage(Messages.getMessage(MessageKeys.LOGIN_AND_STILL_LOADING).colorize().stripColors().toString());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }
}
