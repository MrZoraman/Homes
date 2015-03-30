package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.commands.CommandFunctionBase;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.permissions.Permissions;
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
        return true;
    }
    
    private class HomeSetter extends CommandFunctionBase
    {
        private final Loader loader;
        private final Player player;
        
        HomeSetter(JavaPlugin plugin, Player player)
        {
            super(plugin);
            this.loader = new Loader(plugin);
            this.player = player;
            
            loader.addStep(this::checkPermissions);
        }
        
        void run()
        {
            loader.load(null);
        }
        
        private boolean checkPermissions()
        {
            return checkPerms(player, Permissions.SET_HOME);
        }
    }
}
