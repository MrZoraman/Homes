package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.commands.CommandFunctionBase;
import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import com.lagopusempire.multihomes.util.Util;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class DeleteHomeCommand extends CommandBase
{
    public DeleteHomeCommand(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
    }

    @Override
    protected boolean onCommand(Player player, String[] args)
    {
//        //Get home name
        final boolean usingExplicitHome = args.length > 0;
        final String homeName = usingExplicitHome
                ? args[0]
                : PluginConfig.getString(ConfigKeys.IMPLICIT_HOME_NAME);
        
        (new HomeDeleter(plugin, player, homeName, usingExplicitHome)).run();
        
        return true;
    }
    
    private class HomeDeleter extends CommandFunctionBase
    {
        private final Loader loader;
        private final Player player;
        private final UUID uuid;
        private final String homeName;
        private final boolean usingExplicitHome;
        
        private volatile boolean somethingWasDeleted = false;
        
        public HomeDeleter(JavaPlugin plugin, Player player, String homeName, boolean usingExplicitHome)
        {
            super(plugin);
            this.loader = new Loader(plugin);
            this.player = player;
            this.uuid = player.getUniqueId();
            this.homeName = homeName;
            this.usingExplicitHome = usingExplicitHome;
            
            final boolean isAsync = homeManager.shouldBeAsync();
            
            loader.addStep(this::checkPermissions, false);
            loader.addStep(this::verifyLoaded, false);
            loader.addStep(this::deleteHome, isAsync);
            loader.addStep(this::notifyPlayer, false);
        }
        
        void run()
        {
            loader.load(null);
        }
        
        private boolean checkPermissions()
        {
            return Util.checkPerms(player, Permissions.DELETE_HOME);
        }
        
        private boolean verifyLoaded()
        {
//            if(!homeManager.isLoaded(uuid))
//            {
//                final MessageFormatter formatter = Messages.getMessage(MessageKeys.NOT_LOADED_SELF)
//                        .colorize();
//                Util.sendMessage(player, formatter);
//                return false;
//            }
//            
            return true;
        }
        
        private boolean deleteHome()
        {
            somethingWasDeleted = homeManager.deleteHome(uuid, homeName);
            return true;
        }
        
        private boolean notifyPlayer()
        {
            if(!somethingWasDeleted)//The home didn't exist
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
}
