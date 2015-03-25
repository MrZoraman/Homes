package com.lagopusempire.multihomes.commands;

import com.lagopusempire.bukkitlcs.IBukkitLCSCommand;
import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
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
    
    protected boolean checkPerms(CommandSender sender, Permissions perm)
    {
        if(!perm.check(sender))
        {
            sender.sendMessage(getNoPermsMsg(perm));
            return false;
        }
        
        return true;
    }
    
    private String getNoPermsMsg(Permissions perm)
    {
        return Messages.getMessage(MessageKeys.NO_PERMISSION)
                .colorize()
                .replace("node", perm.getNode())
                .toString();
    }
}
