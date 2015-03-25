package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class ListHomes extends CommandBase
{
    public ListHomes(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        
        
        return true;
    }
}
