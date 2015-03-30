package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.commands.CommandFunctionBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.home.Coordinates;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.NumeralPermissions;
import com.lagopusempire.multihomes.permissions.Permissions;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class SetHomeCommand extends CommandBase
{
    public SetHomeCommand(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
        final boolean usingExplicitHome = args.length > 0;
        final String homeName = usingExplicitHome
		? args[0]
		: PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        (new HomeSetter(plugin, player, homeName, usingExplicitHome, false)).run();
        return true;
    }
    
    private class HomeSetter extends CommandFunctionBase
    {
        private final Loader loader;
        private final Player player;
        private final String homeName;
        private final boolean usingExplicitHome;
        private final boolean force;
        private final Coordinates coords;
        private final String worldName;
        private final UUID uuid;
        
        private volatile boolean setHomeResult;
        private volatile int homeCount;
        
        HomeSetter(JavaPlugin plugin, Player player, String homeName, boolean usingExplicitHome, boolean force)
        {
            super(plugin);
            this.loader = new Loader(plugin);
            this.player = player;
            this.homeName = homeName;
            this.usingExplicitHome = usingExplicitHome;
            this.force = force;
            this.coords = new Coordinates(player.getLocation());
            this.worldName = player.getLocation().getWorld().getName();
            this.uuid = player.getUniqueId();
            
            final boolean isAsync = homeManager.shouldBeAsync();
            System.out.println("isAsync: " + isAsync);
            
            loader.addStep(this::checkPermissions, false);
            loader.addStep(this::setHome, isAsync);
            loader.addStep(this::notifyPlayer, false);
        }
        
        void run()
        {
            loader.load(null);
        }
        
        private boolean checkPermissions()
        {
            return checkPerms(player, Permissions.SET_HOME);
        }
        
        private boolean setHome()
        {
            setHomeResult = homeManager.setHome(uuid, homeName, coords, worldName);
            homeCount = homeManager.getHomeCount(uuid);
            return true;
        }
        
        private boolean notifyPlayer()
        {
            if(!force || !setHomeResult)//new home
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
}
