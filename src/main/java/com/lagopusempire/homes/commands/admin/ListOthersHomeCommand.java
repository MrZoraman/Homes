package com.lagopusempire.homes.commands.admin;

import com.lagopusempire.homes.HomeManager;
import com.lagopusempire.homes.HomesPlugin;
import com.lagopusempire.homes.commands.CommandBase;
import com.lagopusempire.homes.jobs.admin.ListOthersHomeJob;
import com.lagopusempire.homes.messages.MessageKeys;
import com.lagopusempire.homes.messages.Messages;
import com.lagopusempire.homes.permissions.Permissions;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class ListOthersHomeCommand extends CommandBase
{
    public ListOthersHomeCommand(HomesPlugin plugin, HomeManager homeManager)
    {
        super(plugin, homeManager, Permissions.LIST_HOMES_OTHER);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        if(args.length < 1)
        {
            final String message = Messages.getMessage(MessageKeys.MUST_SPECIFY_PLAYER).colorize().toString();
            player.sendMessage(message);
            return true;
        }
        
        final String targetName = args[0];
        final ListOthersHomeJob job = new ListOthersHomeJob(plugin, homeManager, player, targetName);
        job.run();
        return true;
    }
}
