package com.lagopusempire.homes.jobs.user;

import com.lagopusempire.homes.HomeManager;
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
public class DeleteHomeJob extends JobBase
{
    private final boolean usingExplicitHome;
    private final String homeName;
    
    private volatile boolean somethingWasDeleted;

    public DeleteHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player, String homeName, boolean usingExplicitHome)
    {
        super(plugin, homeManager, player);
        this.usingExplicitHome = usingExplicitHome;
        this.homeName = homeName;
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::deleteHome, homeManager.shouldBeAsync());
    }

    private boolean deleteHome()
    {
        somethingWasDeleted = homeManager.deleteHome(uuid, homeName);
        return true;
    }

    @Override
    protected boolean notifyPlayer()
    {
        if (!somethingWasDeleted)//The home didn't exist
        {
            final MessageKeys key = usingExplicitHome
                    ? MessageKeys.HOME_DELETE_NOEXIST_EXPLICIT
                    : MessageKeys.HOME_DELETE_NOEXIST_IMPLICIT;

            final MessageFormatter formatter = Messages.getMessage(key)
                    .colorize()
                    .replace("home", homeName);

            player.sendMessage(formatter.toString());
            return true;
        }

        final MessageKeys key = usingExplicitHome
                ? MessageKeys.HOME_DELETE_SUCCESS_EXPLICIT
                : MessageKeys.HOME_DELETE_SUCCESS_IMPLICIT;

        final MessageFormatter formatter = Messages.getMessage(key)
                .colorize()
                .replace("home", homeName);

        player.sendMessage(formatter.toString());
        return true;
    }
}
