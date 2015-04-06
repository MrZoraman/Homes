package com.lagopusempire.homes.commands.user;

import com.lagopusempire.homes.HomeManager;
import com.lagopusempire.homes.HomesPlugin;
import com.lagopusempire.homes.commands.CommandBase;
import com.lagopusempire.homes.config.ConfigKeys;
import com.lagopusempire.homes.config.PluginConfig;
import com.lagopusempire.homes.jobs.user.DeleteHomeJob;
import com.lagopusempire.homes.permissions.Permissions;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class DeleteHomeCommand extends CommandBase
{
    public DeleteHomeCommand(HomesPlugin plugin, HomeManager homeManager)
    {
        super(plugin, homeManager, Permissions.DELETE_HOME);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        final boolean usingExplicitHome = args.length > 0;
        String homeName = usingExplicitHome
                ? args[0]
                : PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        final HomeAdjustment adjustment = adjustHomeName(homeName, usingExplicitHome);
        final DeleteHomeJob job = new DeleteHomeJob(plugin, homeManager, player, adjustment.homeName, adjustment.explicit);
        job.run();
        return true;
    }
}
