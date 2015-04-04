package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.jobs.user.SetHomeJob;
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
        
        //public SetHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player, String homeName, boolean usingExplicitHome, boolean force)
        final SetHomeJob job = new SetHomeJob(plugin, homeManager, player, homeName, usingExplicitHome, false);
        job.run();
        return true;
    }
}
