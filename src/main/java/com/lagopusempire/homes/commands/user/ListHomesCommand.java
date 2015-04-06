package com.lagopusempire.homes.commands.user;

import com.lagopusempire.homes.HomeManager;
import com.lagopusempire.homes.HomesPlugin;
import com.lagopusempire.homes.commands.CommandBase;
import com.lagopusempire.homes.jobs.user.ListHomesJob;
import com.lagopusempire.homes.permissions.Permissions;
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
