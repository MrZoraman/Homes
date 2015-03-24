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
        final boolean usingImplicitHome = args.length > 0;
        final String homeName = usingImplicitHome 
                ? args[0] 
                : PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        if(!Permissions.SET_HOME.check(player))
        {
            player.sendMessage(getNoPermsMsg(Permissions.SET_HOME));
            return true;
        }
        
        homeManager.setHome(player, player.getUniqueId(), homeName, () -> 
        {
            final MessageKeys key = usingImplicitHome
                    ? MessageKeys.HOME_SET_IMPLICIT
                    : MessageKeys.HOME_SET_EXPLICIT;
            
            final MessageFormatter formatter = Messages.getMessage(key)
                    .colorize()
                    .replace("home", homeName);
            
            player.sendMessage(formatter.toString());
        });
        
        return true;
    }
}
