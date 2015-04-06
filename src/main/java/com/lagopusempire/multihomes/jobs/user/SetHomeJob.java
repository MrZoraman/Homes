package com.lagopusempire.multihomes.jobs.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.home.Coordinates;
import com.lagopusempire.multihomes.jobs.JobBase;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.NumeralPermissions;
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
    private final boolean usingExplicitHome;
    private final String homeName;

    private volatile int homeCount;

    public SetHomeJob(JavaPlugin plugin, HomeManager homeManager, Player player, String homeName, boolean usingExplicitHome, boolean force)
    {
        super(plugin, homeManager, player);
        this.coords = new Coordinates(player.getLocation());
        this.worldName = player.getLocation().getWorld().getName();
        this.usingExplicitHome = usingExplicitHome;
        this.homeName = homeName;
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::getHomeCount, homeManager.shouldBeAsync());
        loader.addStep(this::verifyCanHaveHome, false);
        loader.addStep(this::setHome, homeManager.shouldBeAsync());
    }
    
    private boolean getHomeCount()
    {
        homeCount = homeManager.getHomeCount(uuid);
        return true;
    }
    
    private boolean verifyCanHaveHome()
    {
        final int maxHomes = NumeralPermissions.COUNT.getAmount(player);
        if (maxHomes >= 0 && homeCount >= maxHomes)
        {
            final MessageFormatter formatter = Messages.getMessage(MessageKeys.HOME_SET_TOO_MANY)
                    .colorize()
                    .replace("max", String.valueOf(maxHomes));

            player.sendMessage(formatter.toString());
            return false;
        }
        
        return true;
    }

    private boolean setHome()
    {
        homeManager.setHome(uuid, homeName, coords, worldName);
        return true;
    }

    @Override
    protected boolean notifyPlayer()
    {
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
