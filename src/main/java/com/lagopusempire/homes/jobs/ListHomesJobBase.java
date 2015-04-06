package com.lagopusempire.homes.jobs;

import com.lagopusempire.homes.HomeManager;
import com.lagopusempire.homes.config.ConfigKeys;
import com.lagopusempire.homes.config.PluginConfig;
import com.lagopusempire.homes.load.Loader;
import com.lagopusempire.homes.messages.MessageFormatter;
import com.lagopusempire.homes.messages.MessageKeys;
import com.lagopusempire.homes.messages.Messages;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public abstract class ListHomesJobBase extends JobBase
{
    protected final String targetName;
    
    private volatile List<String> homeList = null;
    
    public ListHomesJobBase(JavaPlugin plugin, HomeManager homeManager, Player player, String targetName)
    {
        super(plugin, homeManager, player);
        this.targetName = targetName;
    }
    
    protected abstract UUID getTarget();
    protected abstract int getTargetMaxHomes();
    
    protected abstract MessageKeys getHomeListNone();
    protected abstract MessageKeys getHomeListInitial();
    protected abstract MessageKeys getHomeListFormat();
    protected abstract MessageKeys getStripLength();
    protected abstract MessageKeys getListImplicitHome();
    
    @Override
    protected void addSteps(Loader loader)
    {
        loader.addStep(this::getHomeList, homeManager.shouldBeAsync());
    }
    
    private boolean getHomeList()
    {
        homeList = homeManager.getHomeList(getTarget());
        return true;
    }

    @Override
    protected final boolean notifyPlayer()
    {
        final String implicitHomeName = PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);

        final boolean listImplicitHome = Messages.getBoolean(getListImplicitHome());

        final int maxHomes = getTargetMaxHomes();
        final String maxHomesString = maxHomes >= 0
                ? String.valueOf(maxHomes)
                : Messages.getMessage(MessageKeys.INFINITE_HOMES_REP).colorize().toString();

        final int amountOfHomes = homeList.size();

        if (amountOfHomes <= 0)
        {
            final MessageFormatter formatter = Messages.getMessage(getHomeListNone())
                    .colorize()
                    .replace("max", maxHomesString)
                    .replace("player", targetName);

            player.sendMessage(formatter.toString());
            return true;
        }

        final StringBuilder builder = new StringBuilder();
        builder.append(Messages.getMessage(getHomeListInitial())
                .colorize()
                .replace("count", String.valueOf(amountOfHomes))
                .replace("max", maxHomesString)
                .replace("player", targetName));

        final MessageFormatter homeFormatterTemplate = Messages.getMessage(getHomeListFormat()).colorize();

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

        final int endStripLength = Messages.getInt(getStripLength());
        if (endStripLength > 0)
        {
            builder.setLength(builder.length() - endStripLength);
        }

        player.sendMessage(builder.toString());
        return true;
    }
}
