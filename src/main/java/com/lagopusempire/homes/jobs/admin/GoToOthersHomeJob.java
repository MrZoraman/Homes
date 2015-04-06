package com.lagopusempire.homes.jobs.admin;

import com.lagopusempire.homes.HomeManager;
import com.lagopusempire.homes.home.Home;
import com.lagopusempire.homes.home.HomeLoadPackage;
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
public class GoToOthersHomeJob extends JobBase
{
    private final String homeName;
    private final boolean usingExplicitHome;
    private final String targetName;
    private final Set<? extends Player> onlinePlayers;

    private volatile Home home;
    private volatile UUID target;
    
    public GoToOthersHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player, String targetName, String homeName, boolean usingExplicitHome)
    {
        super(plugin, homeManager, player);
        this.homeName = homeName;
        this.usingExplicitHome = usingExplicitHome;
        this.targetName = targetName;
        this.onlinePlayers = new HashSet<>(plugin.getServer().getOnlinePlayers());
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::getTarget, true);
        loader.addStep(this::verifyTarget, false);
        loader.addStep(this::getHome, homeManager.shouldBeAsync());
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
    
    private boolean getHome()
    {
        home = homeManager.getHome(target, homeName);
        return true;
    }

    @Override
    protected boolean notifyPlayer()
    {
        final HomeLoadPackage pack = home.getHomeLoadPackage();

        MessageKeys key;
        MessageFormatter formatter;

        switch (pack.loadResult)
        {
            case NO_WORLD:
                key = usingExplicitHome
                        ? MessageKeys.HOME_GET_OTHER_NOT_LOADED_EXPLICIT
                        : MessageKeys.HOME_GET_OTHER_NOT_LOADED_IMPLICIT;
                formatter = Messages.getMessage(key)
                        .colorize()
                        .replace("home", homeName)
                        .replace("player", targetName)
                        .replace("world", home.getWorldName());
                player.sendMessage(formatter.toString());
                break;

            case DOES_NOT_EXIST:
                key = usingExplicitHome
                        ? MessageKeys.HOME_GET_OTHER_NOEXIST_EXPLICIT
                        : MessageKeys.HOME_GET_OTHER_NOEXIST_IMPLICIT;
                formatter = Messages.getMessage(key)
                        .colorize()
                        .replace("home", homeName)
                        .replace("player", targetName);
                player.sendMessage(formatter.toString());
                break;

            case SUCCESS:
                player.teleport(pack.loc);
        }
        return true;
    }
}
