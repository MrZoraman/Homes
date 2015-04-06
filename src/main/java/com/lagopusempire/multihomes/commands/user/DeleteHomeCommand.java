package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.HomesPlugin;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.jobs.user.DeleteHomeJob;
import com.lagopusempire.multihomes.permissions.Permissions;
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
