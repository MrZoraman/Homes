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
import java.util.List;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class ListHomesCommand extends CommandBase
{
    public ListHomesCommand(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        if(!checkPerms(player, Permissions.LIST_HOMES))
            return true;
        
        final String implicitHomeName = PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        final int maxHomes = NumeralPermissions.COUNT.getAmount(player);
        final String maxHomesString = maxHomes >= 0
                ? String.valueOf(maxHomes)
                : Messages.getMessage(MessageKeys.INFINITE_HOMES_REP).colorize().toString();
        
        homeManager.getHomeList(player.getUniqueId(), (List<String> homeList) -> 
        {
            final int amountOfHomes = homeList.size();
            
            if(amountOfHomes <= 1) //1 because it will still say 'no homes to list' if they only have their main home set
            {
                final MessageFormatter formatter = Messages.getMessage(MessageKeys.HOME_LIST_NONE)
                        .colorize()
                        .replace("max", maxHomesString);
                
                player.sendMessage(formatter.toString());
                return;
            }
            
            final StringBuilder builder = new StringBuilder();
            builder.append(Messages.getMessage(MessageKeys.HOME_LIST_INITIAL)
                    .colorize()
                    .replace("count", String.valueOf(amountOfHomes))
                    .replace("max", maxHomesString));
            
            final MessageFormatter homeFormatterTemplate = Messages.getMessage(MessageKeys.HOME_LIST_FORMAT).colorize();
            
            for(int ii = 0; ii < amountOfHomes; ii++)
            {
                final String homeName = homeList.get(ii);
                
                if(homeName.equalsIgnoreCase(implicitHomeName))
                    continue;
                
                builder.append(homeFormatterTemplate
                        .dup()
                        .replace("home", homeName)
                        .toString()
                );
            }
            
            final int endStripLength = Messages.getInt(MessageKeys.HOME_LIST_END_STRIP_LENGTH);
            if(endStripLength > 0)
            {
                builder.setLength(builder.length() - endStripLength);
            }
            
            player.sendMessage(builder.toString());
        });
        
        return true;
    }
}
