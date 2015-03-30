package com.lagopusempire.multihomes.commands.user;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.load.Loader;
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
    
    private class HomeSetter
    {
        private final Loader loader;
        
        HomeSetter(JavaPlugin plugin)
        {
            this.loader = new Loader(plugin);
            
        }
    }
}
