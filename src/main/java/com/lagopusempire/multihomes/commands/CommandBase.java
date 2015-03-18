package com.lagopusempire.multihomes.commands;

import com.lagopusempire.bukkitlcs.IBukkitLCSCommand;
import com.lagopusempire.multihomes.HomeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public abstract class CommandBase implements IBukkitLCSCommand
{
    protected final HomeManager playerManager;
    
    public CommandBase(HomeManager playerManager)
    {
        this.playerManager = playerManager;
    }
    
    protected abstract boolean onCommand(Player player, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] preArgs, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You must be a player to use that command!");
            return true;
        }
        
        return onCommand((Player) sender, args);
    }
}
