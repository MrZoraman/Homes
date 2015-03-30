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
import java.util.HashSet;
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
}
