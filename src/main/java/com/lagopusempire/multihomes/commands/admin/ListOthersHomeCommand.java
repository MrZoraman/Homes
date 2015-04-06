package com.lagopusempire.multihomes.commands.admin;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.jobs.admin.ListOthersHomeJob;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class ListOthersHomeCommand extends CommandBase
{
    public ListOthersHomeCommand(MultiHomes plugin, HomeManager homeManager)
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
