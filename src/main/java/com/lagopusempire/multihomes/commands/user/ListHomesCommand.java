package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.HomesPlugin;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.jobs.user.ListHomesJob;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class ListHomesCommand extends CommandBase
{
    public ListHomesCommand(HomesPlugin plugin, HomeManager homeManager)
    {
        super(plugin, homeManager, Permissions.LIST_HOMES);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        final ListHomesJob job = new ListHomesJob(plugin, homeManager, player);
        job.run();
        return true;
    }
}
