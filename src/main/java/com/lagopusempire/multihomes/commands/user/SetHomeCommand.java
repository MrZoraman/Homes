package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class SetHomeCommand extends CommandBase
{
    public SetHomeCommand(HomeManager homeManager)
    {
        super(homeManager);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
//        String homeName;
//        if(args.length > 0)
//        {
//            homeName = args[0];
//        }
//        else
//        {
//            homeName = PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
//        }
        
        System.out.println("Success!");
        
        return true;
    }
}
