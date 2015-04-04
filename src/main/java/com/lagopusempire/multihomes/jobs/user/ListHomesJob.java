package com.lagopusempire.multihomes.jobs.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.jobs.JobBase;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.NumeralPermissions;
import com.lagopusempire.multihomes.permissions.Permissions;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class ListHomesJob extends JobBase
{
    private volatile List<String> homeList = null;

    public ListHomesJob(JavaPlugin plugin, HomeManager homeManager, Player player)
    {
        super(plugin, homeManager, player);
        
        this.setRequiredPermissions(Permissions.LIST_HOMES);
    }

    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::getHomeList, homeManager.shouldBeAsync());
    }

    private boolean getHomeList()
    {
        homeList = homeManager.getHomeList(uuid);
        return true;
    }

    @Override
    protected boolean notifyPlayer()
    {
        final String implicitHomeName = PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);

        final boolean listImplicitHome = PluginConfig.getBoolean(ConfigKeys.LIST_IMPLICIT_HOME);

        final int maxHomes = NumeralPermissions.COUNT.getAmount(player);
        final String maxHomesString = maxHomes >= 0
                ? String.valueOf(maxHomes)
                : Messages.getMessage(MessageKeys.INFINITE_HOMES_REP).colorize().toString();

        final int amountOfHomes = homeList.size();

        if (amountOfHomes <= 1) //1 because it will still say 'no homes to list' if they only have their main home set
        {
            final MessageFormatter formatter = Messages.getMessage(MessageKeys.HOME_LIST_NONE)
                    .colorize()
                    .replace("max", maxHomesString);

            player.sendMessage(formatter.toString());
            return true;
        }

        final StringBuilder builder = new StringBuilder();
        builder.append(Messages.getMessage(MessageKeys.HOME_LIST_INITIAL)
                .colorize()
                .replace("count", String.valueOf(amountOfHomes))
                .replace("max", maxHomesString));

        final MessageFormatter homeFormatterTemplate = Messages.getMessage(MessageKeys.HOME_LIST_FORMAT).colorize();

        for (int ii = 0; ii < amountOfHomes; ii++)
        {
            final String homeName = homeList.get(ii);

            if (!listImplicitHome && homeName.equalsIgnoreCase(implicitHomeName))
            {
                continue;
            }

            builder.append(homeFormatterTemplate
                    .dup()
                    .replace("home", homeName)
                    .toString()
            );
        }

        final int endStripLength = Messages.getInt(MessageKeys.HOME_LIST_END_STRIP_LENGTH);
        if (endStripLength > 0)
        {
            builder.setLength(builder.length() - endStripLength);
        }

        player.sendMessage(builder.toString());
        return true;
    }
}
