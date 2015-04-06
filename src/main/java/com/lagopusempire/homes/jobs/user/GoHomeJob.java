package com.lagopusempire.homes.jobs.user;

import com.lagopusempire.homes.HomeManager;
import com.lagopusempire.homes.home.Home;
import com.lagopusempire.homes.home.HomeLoadPackage;
import com.lagopusempire.homes.jobs.JobBase;
import com.lagopusempire.homes.load.Loader;
import com.lagopusempire.homes.messages.MessageFormatter;
import com.lagopusempire.homes.messages.MessageKeys;
import com.lagopusempire.homes.messages.Messages;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class GoHomeJob extends JobBase
{
    private final String homeName;
    private final boolean usingExplicitHome;

    private volatile Home home;

    public GoHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player, String homeName, boolean usingExplicitHome)
    {
        super(plugin, homeManager, player);
        this.homeName = homeName;
        this.usingExplicitHome = usingExplicitHome;
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::getHome, homeManager.shouldBeAsync());
    }

    private boolean getHome()
    {
        home = homeManager.getHome(uuid, homeName);
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
                        ? MessageKeys.HOME_GET_NOT_LOADED_EXPLICIT
                        : MessageKeys.HOME_GET_NOT_LOADED_IMPLICIT;
                formatter = Messages.getMessage(key)
                        .colorize()
                        .replace("home", homeName)
                        .replace("world", home.getWorldName());
                player.sendMessage(formatter.toString());
                break;

            case DOES_NOT_EXIST:
                key = usingExplicitHome
                        ? MessageKeys.HOME_GET_NOEXIST_EXPLICIT
                        : MessageKeys.HOME_GET_NOEXIST_IMPLICIT;
                formatter = Messages.getMessage(key)
                        .colorize()
                        .replace("home", homeName);
                player.sendMessage(formatter.toString());
                break;

            case SUCCESS:
                player.teleport(pack.loc);
        }
        return true;
    }
}
