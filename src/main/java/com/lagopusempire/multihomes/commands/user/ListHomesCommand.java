package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.permissions.Permissions;
import java.util.List;
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
        if(!checkPerms(player, Permissions.LIST_HOMES))
            return true;
        
        homeManager.getHomeList(player.getUniqueId(), (List<String> homeList) -> 
        {
            
        });
        
        return true;
    }
}
