package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.jobs.user.DeleteHomeJob;
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
//        //Get home name
        final boolean usingExplicitHome = args.length > 0;
        final String homeName = usingExplicitHome
                ? args[0]
                : PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        final DeleteHomeJob job = new DeleteHomeJob(plugin, homeManager, player, homeName, usingExplicitHome);
        job.run();
        return true;
    }
}
