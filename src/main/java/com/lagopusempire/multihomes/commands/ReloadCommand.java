package com.lagopusempire.multihomes.commands;

import com.lagopusempire.bukkitlcs.IBukkitLCSCommand;
import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.ReloadCallback;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if(!(sender instanceof Player) || Permissions.RELOAD.check(sender))
        {
            plugin.reload((result) -> 
            {
                if(result)
                {
                }
            });
        }
        
        return true;
    }
}
