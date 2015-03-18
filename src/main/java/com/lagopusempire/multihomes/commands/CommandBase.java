package com.lagopusempire.multihomes.commands;

import com.lagopusempire.multihomes.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public abstract class CommandBase implements CommandExecutor
{
    protected final PlayerManager playerManager;
    
    public CommandBase(PlayerManager playerManager)
    {
        this.playerManager = playerManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "You must be a player to use that command!");
            return true;
        }
        
        return onCommand((Player) sender, args);
    }
    
    protected abstract boolean onCommand(Player player, String[] args);
}
