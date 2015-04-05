package com.lagopusempire.multihomes.jobs.admin;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.jobs.JobBase;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.messages.MessageFormatter;
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
public class DeleteOthersHomeJob extends JobBase
{
    private final String targetName;
    private final boolean usingExplicitHome;
    private final String homeName;
    private final Set<? extends Player> onlinePlayers;

    private volatile UUID target;
    private volatile boolean somethingWasDeleted;

    public DeleteOthersHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player,
            String targetName, boolean usingExplicitHome, String homeName)
    {
        super(plugin, homeManager, player);
        this.targetName = targetName;
        this.usingExplicitHome = usingExplicitHome;
        this.homeName = homeName;
        this.onlinePlayers = new HashSet<>(plugin.getServer().getOnlinePlayers());

        this.setRequiredPermissions(Permissions.DELETE_HOME_OTHER);
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::getTarget, true);
        loader.addStep(this::verifyTarget, false);
        loader.addStep(this::deleteHome, homeManager.shouldBeAsync());
    }

    private boolean getTarget()
    {
        target = Util.getPlayer(targetName, plugin.getLogger(), onlinePlayers);
        return true;
    }

    private boolean verifyTarget()
    {
        if (target == null)
        {
            Util.sendMessage(player, Messages.getMessage(MessageKeys.PLAYER_NOT_FOUND).colorize());
            return false;
        }
        
        System.out.println("target uuid: " + target.toString());

        return true;
    }

    private boolean deleteHome()
    {
        somethingWasDeleted = homeManager.deleteHome(target, homeName);
        return true;
    }

    @Override
    protected boolean notifyPlayer()
    {
        MessageFormatter formatter;
        MessageKeys key = null;

        if (!somethingWasDeleted)
        {
            key = usingExplicitHome
                    ? MessageKeys.HOME_DELETE_OTHER_NOEXIST_EXPLICIT
                    : MessageKeys.HOME_DELETE_OTHER_NOEXIST_IMPLICIT;
        }
        else
        {
            key = usingExplicitHome
                    ? MessageKeys.HOME_DELETE_OTHER_SUCCESS_EXPLICIT
                    : MessageKeys.HOME_DELETE_OTHER_SUCCESS_IMPLICIT;
        }

        formatter = Messages.getMessage(key)
                .colorize()
                .replace("home", homeName)
                .replace("player", targetName);

        player.sendMessage(formatter.toString());

        return true;
    }
}
