package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.NumeralPermissions;
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
        if(!checkPerms(player, Permissions.SET_HOME))
            return true;

        //Get home name
        final boolean usingExplicitHome = args.length > 0;
        final String homeName = usingExplicitHome
                ? args[0]
                : PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);

        //Set their home
        homeManager.setHome(player, player.getUniqueId(), homeName, (wasUpdate) ->
        {
            //Get the amount of homes this player has
            homeManager.getHomeCount(player.getUniqueId(), (amount) ->
            {
                if(!wasUpdate)//new home
                {
                    //Check if player has space for another home
                    final int maxHomes = NumeralPermissions.COUNT.getAmount(player);
                    if (maxHomes >= 0 && amount >= maxHomes)
                    {
                        final MessageFormatter formatter = Messages.getMessage(MessageKeys.HOME_SET_TOO_MANY)
                                .colorize()
                                .replace("max", String.valueOf(maxHomes));

                        player.sendMessage(formatter.toString());
                        return;
                    }
                }

                final MessageKeys key = usingExplicitHome
                        ? MessageKeys.HOME_SET_EXPLICIT
                        : MessageKeys.HOME_SET_IMPLICIT;

                final MessageFormatter formatter = Messages.getMessage(key)
                        .colorize()
                        .replace("home", homeName);

                player.sendMessage(formatter.toString());
            });
        });

        return true;
    }
}
