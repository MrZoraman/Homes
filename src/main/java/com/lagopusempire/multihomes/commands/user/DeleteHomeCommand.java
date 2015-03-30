package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class DeleteHomeCommand extends CommandBase
{
    public DeleteHomeCommand(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
//        if(!checkPerms(player, Permissions.DELETE_HOME))
//            return true;
//        
//        //Get home name
//        final boolean usingExplicitHome = args.length > 0;
//        final String homeName = usingExplicitHome
//                ? args[0]
//                : PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
//        
//        homeManager.deleteHome(player.getUniqueId(), homeName, (deleted) -> 
//        {
//            if(!deleted)//The home didn't exist
//            {
//                final MessageKeys key = usingExplicitHome
//                        ? MessageKeys.HOME_DELETE_NOEXIST_EXPLICIT
//                        : MessageKeys.HOME_DELETE_NOEXIST_IMPLICIT;
//                
//                final MessageFormatter formatter = Messages.getMessage(key)
//                        .colorize()
//                        .replace("home", homeName);
//                
//                player.sendMessage(formatter.toString());
//                return;
//            }
//            
//            final MessageKeys key = usingExplicitHome
//                    ? MessageKeys.HOME_DELETE_SUCCESS_EXPLICIT
//                    : MessageKeys.HOME_DELETE_SUCCESS_IMPLICIT;
//            
//            final MessageFormatter formatter = Messages.getMessage(key)
//                    .colorize()
//                    .replace("home", homeName);
//            
//            player.sendMessage(formatter.toString());
//        });
        
        return true;
    }
}
