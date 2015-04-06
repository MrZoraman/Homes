package com.lagopusempire.multihomes.commands;

import com.lagopusempire.bukkitlcs.IBukkitLCSCommand;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.help.Help;
import com.lagopusempire.multihomes.help.HelpKeys;
import com.lagopusempire.multihomes.messages.MessageFormatter;
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
public class HelpCommand implements IBukkitLCSCommand
{
    private final MultiHomes plugin;
    
    public HelpCommand(MultiHomes plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] preArgs, String[] args)
    {
        if(sender == null) return true;
        
        if(!Util.checkPerms(sender, Permissions.HELP))
        {
            return true;
        }
        
        if(!plugin.isLoaded())
        {
            sender.sendMessage(Messages.getMessage(MessageKeys.PLUGIN_NOT_LOADED).colorize().toString());
            return true;
        }
        
        final boolean isPlayer = sender instanceof Player;
        
        final Permissions[] permissions = Permissions.values();
        final MessageFormatter separator = Help.getMessage(HelpKeys.SEPARATOR);
        final boolean headerEnabled = Help.getBoolean(HelpKeys.HEADER_ENABLED);
        if(headerEnabled)
        {
            final MessageFormatter header = Help.getMessage(HelpKeys.HEADER);
            sender.sendMessage(header.colorize().toString());
        }
        
        for(int ii = 0; ii < permissions.length; ii++)
        {
            if(!permissions[ii].check(sender)) continue;
            
            if(!isPlayer && permissions[ii] != Permissions.RELOAD) continue;
            
            boolean enabled = Help.getEnabled(permissions[ii]);
            if(!enabled) continue;
            
            final MessageFormatter command = Help.getCommand(permissions[ii]);
            final MessageFormatter help = Help.getHelp(permissions[ii]);
            
            String message = command
                    .concat(separator)
                    .concat(help)
                    .colorize()
                    .toString();
            
            if(!isPlayer)
            {
                message = message.substring(Help.getInt(HelpKeys.INITIAL_STRIP));
            }
            
            sender.sendMessage(message);
        }
        
        return true;
    }
}
