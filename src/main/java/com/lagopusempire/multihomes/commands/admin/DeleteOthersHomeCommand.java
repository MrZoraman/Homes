package com.lagopusempire.multihomes.commands.admin;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.HomesPlugin;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.jobs.admin.DeleteOthersHomeJob;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class DeleteOthersHomeCommand extends CommandBase
{
    public DeleteOthersHomeCommand(HomesPlugin plugin, HomeManager homeManager)
    {
        super(plugin, homeManager, Permissions.DELETE_HOME_OTHER);
    }
    
    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        String targetName = null;
        boolean usingExplicitHome = false;
        String homeName = null;

        switch (args.length)
        {
            default:
                homeName = args[1];
                usingExplicitHome = true;
            //FALL THROUGH
            case 1:
                if(homeName == null)
                    homeName = PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
                
                targetName = args[0];
                break;
            case 0:
                final String message = Messages.getMessage(MessageKeys.MUST_SPECIFY_PLAYER).colorize().toString();
                player.sendMessage(message);
                return true;
        }
        
        final HomeAdjustment adjustment = adjustHomeName(homeName, usingExplicitHome);
        final DeleteOthersHomeJob job = new DeleteOthersHomeJob(plugin, homeManager, player, targetName, adjustment.explicit, adjustment.homeName);
        job.run();
        return true;
    }
}
