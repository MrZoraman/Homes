package com.lagopusempire.multihomes.commands;

import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author MrZoraman
 */
public class NotLoadedCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
    {
        sender.sendMessage(Messages.getMessage(MessageKeys.NOT_LOADED).colorize().toString());
        return true;
    }
}
