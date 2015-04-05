package com.lagopusempire.multihomes.jobs.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.jobs.ListHomesJobBase;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.permissions.Permissions;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class ListHomesJob extends ListHomesJobBase
{
    public ListHomesJob(JavaPlugin plugin, HomeManager homeManager, Player player)
    {
        super(plugin, homeManager, player, player.getName());
        
        this.setRequiredPermissions(Permissions.LIST_HOMES);
    }

    @Override
    protected UUID getTarget()
    {
        return player.getUniqueId();
    }

    @Override
    protected MessageKeys getHomeListNone()
    {
        return MessageKeys.HOME_LIST_NONE;
    }

    @Override
    protected MessageKeys getHomeListInitial()
    {
        return MessageKeys.HOME_LIST_INITIAL;
    }

    @Override
    protected MessageKeys getHomeListFormat()
    {
        return MessageKeys.HOME_LIST_FORMAT;
    }

    @Override
    protected MessageKeys getStripLength()
    {
        return MessageKeys.HOME_LIST_END_STRIP_LENGTH;
    }
}
