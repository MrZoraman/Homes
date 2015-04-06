package com.lagopusempire.homes.jobs.user;

import com.lagopusempire.homes.HomeManager;
import com.lagopusempire.homes.jobs.ListHomesJobBase;
import com.lagopusempire.homes.messages.MessageKeys;
import com.lagopusempire.homes.permissions.NumeralPermissions;
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

    @Override
    protected int getTargetMaxHomes()
    {
        return NumeralPermissions.COUNT.getAmount(player);
    }
    @Override
    protected MessageKeys getListImplicitHome()
    {
        return MessageKeys.HOME_LIST_IMPLICIT_HOME;
    }
}
