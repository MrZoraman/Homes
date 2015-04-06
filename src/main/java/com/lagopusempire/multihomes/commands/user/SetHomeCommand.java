package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.jobs.user.SetHomeJob;
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
        super(plugin, homeManager, Permissions.SET_HOME);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        final boolean usingExplicitHome = args.length > 0;
        String homeName = usingExplicitHome
		? args[0]
		: PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        final HomeAdjustment adjustment = adjustHomeName(homeName, usingExplicitHome);
        final SetHomeJob job = new SetHomeJob(plugin, homeManager, player, adjustment.homeName, adjustment.explicit, false);
        job.run();
        return true;
    }
}
