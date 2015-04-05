package com.lagopusempire.multihomes.jobs;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.load.Loader;
import com.lagopusempire.multihomes.permissions.Permissions;
import com.lagopusempire.multihomes.util.Util;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public abstract class JobBase implements Runnable
{
    private final Loader loader;
    private Permissions[] permissions;
    
    protected final JavaPlugin plugin;
    protected final HomeManager homeManager;
    protected final Player player;
    protected final UUID uuid;
    
    public JobBase(JavaPlugin plugin, HomeManager homeManager, Player player)
    {
        this.plugin = plugin;
        this.homeManager = homeManager;
        this.player = player;
        this.uuid = player.getUniqueId();
        this.loader = new Loader(plugin);
    }
    
    @Override
    public final void run()
    {
        addPreSteps();
        addSteps(loader);
        addPostSteps();
        
        loader.load(null);
    }
    
    protected void setRequiredPermissions(Permissions... permissions)
    {
        this.permissions = permissions;
    }
    
    private void addPreSteps()
    {
        loader.addStep(this::checkPermissions, false);
    }
    
    private void addPostSteps()
    {
        loader.addStep(this::notifyPlayer, false);
    }
    
    private boolean checkPermissions()
    {
        if(permissions == null)
            return true;
        
        for(int ii = 0; ii < permissions.length; ii++)
        {
            if(Util.checkPerms(player, permissions[ii]) == false)
            {
                return false;
            }
        }
        
        return true;
    }
    
    protected abstract void addSteps(Loader loader);
    
    protected abstract boolean notifyPlayer();
}
