package com.lagopusempire.multihomes.jobs.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.home.Coordinates;
import com.lagopusempire.multihomes.jobs.JobBase;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.NumeralPermissions;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class SetHomeJob extends JobBase
{
    private final Coordinates coords;
    private final String worldName;
    private final boolean force;
    private final boolean usingExplicitHome;

    private volatile boolean setHomeResult;
    private volatile int homeCount;

    public SetHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player, String homeName, boolean usingExplicitHome, boolean force)
    {
        super(plugin, homeManager, player, homeName);
        this.coords = new Coordinates(player.getLocation());
        this.worldName = player.getLocation().getWorld().getName();
        this.usingExplicitHome = usingExplicitHome;
        this.force = force;

        this.setRequiredPermissions(Permissions.SET_HOME);
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::setHome, homeManager.shouldBeAsync());
    }

    private boolean setHome()
    {
        setHomeResult = homeManager.setHome(uuid, homeName, coords, worldName);
        homeCount = homeManager.getHomeCount(uuid);
        return true;
    }

    @Override
    protected boolean notifyPlayer()
    {
        if (!force || !setHomeResult)//new home
        {
            final int maxHomes = NumeralPermissions.COUNT.getAmount(player);
            if (maxHomes >= 0 && homeCount >= maxHomes)
            {
                final MessageFormatter formatter = Messages.getMessage(MessageKeys.HOME_SET_TOO_MANY)
                        .colorize()
                        .replace("max", String.valueOf(maxHomes));

                player.sendMessage(formatter.toString());
                return true;
            }
        }

        final MessageKeys key = usingExplicitHome
                ? MessageKeys.HOME_SET_EXPLICIT
                : MessageKeys.HOME_SET_IMPLICIT;

        final MessageFormatter formatter = Messages.getMessage(key)
                .colorize()
                .replace("home", homeName);

        player.sendMessage(formatter.toString());
        return true;
    }
}
