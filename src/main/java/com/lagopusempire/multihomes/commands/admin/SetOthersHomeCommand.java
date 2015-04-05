package com.lagopusempire.multihomes.commands.admin;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.jobs.admin.SetOthersHomeJob;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class SetOthersHomeCommand extends CommandBase
{

    public SetOthersHomeCommand(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
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
        
        final SetOthersHomeJob job = new SetOthersHomeJob(plugin, homeManager, player,
                targetName, usingExplicitHome, homeName);
        job.run();
        return true;
    }
}
