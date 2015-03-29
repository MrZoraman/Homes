package com.lagopusempire.multihomes.commands;

import com.lagopusempire.bukkitlcs.IBukkitLCSCommand;
import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import com.lagopusempire.multihomes.util.UUIDFetcher;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public abstract class CommandBase implements IBukkitLCSCommand
{
    private static String uuid_regex = null;
    
    static
    {
        uuid_regex = PluginConfig.getString(ConfigKeys.UUID_REGEX);
        System.out.println("UUID REGEX: " + uuid_regex);
    }
    
    @FunctionalInterface
    protected interface PlayerLookupCallback
    {
        public void playerFound(UUID uuid);
    }
    
    protected final HomeManager homeManager;
    private final MultiHomes plugin;
    
    public CommandBase(MultiHomes plugin, HomeManager homeManager)
    {
        this.plugin = plugin;
        this.homeManager = homeManager;
    }
    
    protected abstract boolean onCommand(Player player, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] preArgs, String[] args)
    {
        if(!plugin.isLoaded())
        {
            sender.sendMessage(Messages.getMessage(MessageKeys.NOT_LOADED).colorize().toString());
            return true;
        }
        
        if(!(sender instanceof Player))
        {
            sender.sendMessage(Messages.getMessage(MessageKeys.MUST_BE_PLAYER).colorize().toString());
            return true;
        }
        
        return onCommand((Player) sender, args);
    }
    
    protected boolean checkPerms(CommandSender sender, Permissions perm)
    {
        if(!perm.check(sender))
        {
            sender.sendMessage(getNoPermsMsg(perm));
            return false;
        }
        
        return true;
    }
    
    protected void getPlayer(String playerName, Set<? extends Player> onlinePlayers, PlayerLookupCallback callback)
    {
        if(playerName.matches(uuid_regex))
        {
            callback.playerFound(UUID.fromString(playerName));
            return;
        }
        
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            for(Player player : onlinePlayers)
            {
                if(player.getName().equalsIgnoreCase(playerName))
                {
                    plugin.getServer().getScheduler().runTask(plugin, () -> callback.playerFound(player.getUniqueId()));
                    return;
                }
            }
            
            final UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(playerName));
            Map<String, UUID> response = null;
            try
            {
                response = fetcher.call();
            }
            catch (Exception e)
            {
                plugin.getLogger().warning("Failed to lookup uuid for player '" + playerName + "'!");
                e.printStackTrace();
                plugin.getServer().getScheduler().runTask(plugin, () -> callback.playerFound(null));
                return;
            }
            
            final UUID uuid = response.get(playerName);
            plugin.getServer().getScheduler().runTask(plugin, () -> callback.playerFound(uuid));
        });
    }
    
    private String getNoPermsMsg(Permissions perm)
    {
        return Messages.getMessage(MessageKeys.NO_PERMISSION)
                .colorize()
                .replace("node", perm.getNode())
                .toString();
    }
}
