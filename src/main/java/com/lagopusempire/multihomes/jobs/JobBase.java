package com.lagopusempire.multihomes.jobs;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.load.Loader;
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
        addSteps(loader);
        addPostSteps();
        
        loader.load(null);
    }
    
    private void addPostSteps()
    {
        loader.addStep(this::notifyPlayer, false);
    }
    
    protected abstract void addSteps(Loader loader);
    
    protected abstract boolean notifyPlayer();
}
