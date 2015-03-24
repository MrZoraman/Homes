package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.home.LoadResult;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class GoHomeCommand extends CommandBase
{
    public GoHomeCommand(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        if(!Permissions.GO_HOME.check(player))
        {
            player.sendMessage(getNoPermsMsg(Permissions.SET_HOME));
            return true;
        }
        
        final boolean usingExplicitHome = args.length > 0;
        final String homeName = usingExplicitHome 
                ? args[0] 
                : PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        System.out.println("home name: " + homeName);
        
        homeManager.getHome(player.getUniqueId(), homeName, (home) -> 
        {
            System.out.println("call!");
            
            final LoadResult result = home.getLoadResult();
            
            MessageKeys key;
            MessageFormatter formatter;
            
            switch(result)
            {
                case NO_WORLD:
                    System.out.println("no world");
                    key = usingExplicitHome
                            ? MessageKeys.Home_GET_NOT_LOADED_IMPLICIT
                            : MessageKeys.Home_GET_NOT_LOADED_EXPLICIT;
                    formatter = Messages.getMessage(key)
                            .colorize()
                            .replace("home", homeName);
                    player.sendMessage(formatter.toString());
                    break;
                    
                case DOES_NOT_EXIST:
                    System.out.println("does not exist");
                    key = usingExplicitHome
                            ? MessageKeys.HOME_GET_NOEXIST_EXPLICIT
                            : MessageKeys.HOME_GET_NOEXIST_IMPLICIT;
                    formatter = Messages.getMessage(key)
                            .colorize()
                            .replace("home", homeName);
                    player.sendMessage(formatter.toString());
                    break;
                    
                case SUCCESS:
                    System.out.println("success");
                    final Location loc = home.getLoc();
                    player.teleport(loc);
            }
        });
        
        return true;
    }
}
