package com.lagopusempire.homes.jobs.admin;

import com.lagopusempire.homes.HomeManager;
import com.lagopusempire.homes.home.Coordinates;
import com.lagopusempire.homes.jobs.JobBase;
import com.lagopusempire.homes.load.Loader;
import com.lagopusempire.homes.messages.MessageFormatter;
import com.lagopusempire.homes.messages.MessageKeys;
import com.lagopusempire.homes.messages.Messages;
import com.lagopusempire.homes.util.Util;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class SetOthersHomeJob extends JobBase
{
    private final String targetName;
    private final boolean usingExplicitHome;
    private final String homeName;
    private final Coordinates coords;
    private final String worldName;
    private final Set<? extends Player> onlinePlayers;

    private volatile UUID target;

    public SetOthersHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player,
            String targetName, boolean usingExplicitHome, String homeName)
    {
        super(plugin, homeManager, player);
        this.targetName = targetName;
        this.usingExplicitHome = usingExplicitHome;
        this.homeName = homeName;
        this.coords = new Coordinates(player.getLocation());
        this.worldName = player.getLocation().getWorld().getName();
        this.onlinePlayers = new HashSet<>(plugin.getServer().getOnlinePlayers());
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::getTarget, true);
        loader.addStep(this::verifyTarget, false);
        loader.addStep(this::setHome, homeManager.shouldBeAsync());
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
            Util.sendMessage(player, Messages.getMessage(MessageKeys.PLAYER_NOT_FOUND)
                    .replace("player", targetName)
                    .colorize());
            return false;
        }

        return true;
    }

    private boolean setHome()
    {
        homeManager.setHome(target, homeName, coords, worldName);
        return true;
    }

    @Override
    protected boolean notifyPlayer()
    {
        final MessageKeys key = usingExplicitHome
                ? MessageKeys.HOME_SET_OTHER_EXPLICIT
                : MessageKeys.HOME_SET_OTHER_IMPLICIT;

        final MessageFormatter formatter = Messages.getMessage(key)
                .colorize()
                .replace("home", homeName)
                .replace("player", targetName);

        player.sendMessage(formatter.toString());

        return true;
    }
}
