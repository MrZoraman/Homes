package com.lagopusempire.homes.commands;

import com.lagopusempire.bukkitlcs.IBukkitLCSCommand;
import com.lagopusempire.homes.HomesPlugin;
import com.lagopusempire.homes.messages.MessageFormatter;
import com.lagopusempire.homes.messages.MessageKeys;
import com.lagopusempire.homes.messages.Messages;
import com.lagopusempire.homes.permissions.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author MrZoraman
 */
public class ReloadCommand implements IBukkitLCSCommand
{
    private final HomesPlugin plugin;
    
    public ReloadCommand(HomesPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] preArgs, String[] args)
    {
        if(sender == null)
            throw new IllegalStateException("Sender cannot be null!");
        
        if(Permissions.RELOAD.check(sender))
        {
            plugin.reload((result) -> 
            {
                final MessageFormatter f = Messages.getMessage(result ? MessageKeys.RELOAD_SUCCESS : MessageKeys.RELOAD_FAILURE);
                f.colorize();
                sender.sendMessage(f.toString());
            });
        }
        else
        {
            sender.sendMessage(Messages.getMessage(MessageKeys.NO_PERMISSION).colorize().toString());
        }
        
        return true;
    }
}
