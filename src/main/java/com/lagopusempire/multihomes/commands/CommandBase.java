package com.lagopusempire.multihomes.commands;

import com.lagopusempire.bukkitlcs.IBukkitLCSCommand;
import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import com.lagopusempire.multihomes.util.Util;
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
    protected final MultiHomes plugin;
    
    private final Permissions[] permissions;
    
    public CommandBase(MultiHomes plugin, HomeManager homeManager, Permissions... permissions)
    {
        this.plugin = plugin;
        this.homeManager = homeManager;
        this.permissions = permissions;
        
        if(permissions == null || permissions.length < 1)
        {
            throw new IllegalStateException("Permissionless command detected! (Programmer error)");
        }
    }
    
    protected abstract boolean onCommand(Player player, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] preArgs, String[] args)
    {
        if(!plugin.isLoaded())
        {
            sender.sendMessage(Messages.getMessage(MessageKeys.PLUGIN_NOT_LOADED).colorize().toString());
            return true;
        }
        
        if(!(sender instanceof Player))
        {
            sender.sendMessage(Messages.getMessage(MessageKeys.MUST_BE_PLAYER).colorize().toString());
            return true;
        }
        
        final Player player = (Player) sender;
        
        if(!checkPermissions(player))
        {
            return true;
        }
        
        return onCommand(player, args);
    }
    
    private boolean checkPermissions(Player player)
    {
        if(permissions == null)
            return true;
        
        for(int ii = 0; ii < permissions.length; ii++)
        {
            if(Util.checkPerms(player, permissions[ii]) == false)
            {
                return false;
            }
        }
        
        return true;
    }
}
