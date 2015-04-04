package com.lagopusempire.multihomes.jobs.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.HomeLoadPackage;
import com.lagopusempire.multihomes.jobs.JobBase;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
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

        this.setRequiredPermissions(Permissions.GO_HOME);
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
                        ? MessageKeys.HOME_GET_NOT_LOADED_IMPLICIT
                        : MessageKeys.HOME_GET_NOT_LOADED_EXPLICIT;
                formatter = Messages.getMessage(key)
                        .colorize()
                        .replace("home", homeName);
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
