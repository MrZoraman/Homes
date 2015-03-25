package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
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
        homeManager.getHomeCount(player.getUniqueId(), (count) -> 
        {
            System.out.println("home count for " + player.getName() + ": " + count);
        });
        
        return true;
    }
}
