package com.lagopusempire.multihomes.commands;

import com.lagopusempire.bukkitlcs.IBukkitLCSCommand;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 *
 * @author MrZoraman
 */
public class ReloadCommand implements IBukkitLCSCommand
{
    private final MultiHomes plugin;
    
    public ReloadCommand(MultiHomes plugin)
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
