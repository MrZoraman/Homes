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
public class SetHomeCommand extends CommandBase
{
    public SetHomeCommand(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        final boolean usingExplicitHome = args.length > 0;
        final String homeName = usingExplicitHome 
                ? args[0] 
                : PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        if(!Permissions.SET_HOME.check(player))
        {
            player.sendMessage(getNoPermsMsg(Permissions.SET_HOME));
            return true;
        }
        
        homeManager.setHome(player, player.getUniqueId(), homeName, () -> 
        {
            final MessageKeys key = usingExplicitHome
                    ? MessageKeys.HOME_SET_EXPLICIT
                    : MessageKeys.HOME_SET_IMPLICIT;
            
            final MessageFormatter formatter = Messages.getMessage(key)
                    .colorize()
                    .replace("home", homeName);
            
            player.sendMessage(formatter.toString());
        });
        
        return true;
    }
}
