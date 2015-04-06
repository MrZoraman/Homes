package com.lagopusempire.multihomes.jobs.admin;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.jobs.ListHomesJobBase;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import com.lagopusempire.multihomes.util.Util;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class ListOthersHomeJob extends ListHomesJobBase
{
    private final Set<? extends Player> onlinePlayers;
    
    private volatile UUID target;
    
    public ListOthersHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player, String targetName)
    {
        super(plugin, homeManager, player, targetName);
        this.onlinePlayers = new HashSet<>(plugin.getServer().getOnlinePlayers());
        
        this.setRequiredPermissions(Permissions.LIST_HOMES_OTHER);
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::retrieveTarget, true);
        loader.addStep(this::verifyTarget, false);
        
        super.addSteps(loader);
    }

    private boolean retrieveTarget()
    {
        target = Util.getPlayer(targetName, plugin.getLogger(), onlinePlayers);
        return true;
    }

    private boolean verifyTarget()
    {
        if (target == null)
        {
            Util.sendMessage(player, Messages.getMessage(MessageKeys.PLAYER_NOT_FOUND)
                    .replace("player", targetName)
                    .colorize());
            return false;
        }

        return true;
    }

    @Override
    protected UUID getTarget()
    {
        return target;
    }

    @Override
    protected MessageKeys getHomeListNone()
    {
        return MessageKeys.HOME_LIST_OTHER_NONE;
    }

    @Override
    protected MessageKeys getHomeListInitial()
    {
        return MessageKeys.HOME_LIST_OTHER_INITIAL;
    }

    @Override
    protected MessageKeys getHomeListFormat()
    {
        return MessageKeys.HOME_LIST_OTHER_FORMAT;
    }

    @Override
    protected MessageKeys getStripLength()
    {
        return MessageKeys.HOME_LIST_OTHER_END_STRIP_LENGTH;
    }

    @Override
    protected int getTargetMaxHomes()
    {
        return -1;
    }

    @Override
    protected MessageKeys getListImplicitHome()
    {
        return MessageKeys.HOME_LIST_OTHER_IMPLICIT_HOME;
    }
}
