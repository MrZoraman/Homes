package com.lagopusempire.multihomes.load;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class Loader
{
    private final List<LoadStep> preLoadSteps = new ArrayList<>();
    private final Set<Integer> preAsyncSteps = new HashSet<>();
    
    private List<LoadStep> loadSteps;
    private Set<Integer> asyncSteps;
    
    private final JavaPlugin plugin;
    
    private volatile boolean isLoading = false;
    
    private int stepIndex = 0;
    
    public Loader(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    public void addStep(LoadStep step, boolean async)
    {
        if(isLoading) throw new IllegalStateException("Cannot call addStep after loading has started!");
        
        preLoadSteps.add(stepIndex, step);
        stepIndex++;
        
        if(async)
        {
            preAsyncSteps.add(stepIndex);
        }
    }
    
    public void load(final LoadCallback callback)
    {
        if(isLoading) throw new IllegalStateException("Loader is already loading!");
        
        loadSteps = Collections.unmodifiableList(preLoadSteps);
        asyncSteps = Collections.unmodifiableSet(preAsyncSteps);
        
        isLoading = true;
        
        load(0, callback, false);
    }
    
    private void load(final int stepIndex, final LoadCallback callback, final boolean onAsyncThread)
    {
        if(stepIndex >= loadSteps.size())
        {
            //We've completed the last step. If we've gotten to this point, then all has been executed successfully
            callCallback(true, callback, onAsyncThread);
            return;
        }
        
        //first, check if we need to switch threads
        final boolean taskIsAsync = isAsync(stepIndex);
        if(taskIsAsync != onAsyncThread)
        {
            //If we do need to switch threads, we do so
            if(taskIsAsync)
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> load(stepIndex, callback, !onAsyncThread));
            else
                plugin.getServer().getScheduler().runTask(plugin, () -> load(stepIndex, callback, !onAsyncThread));
        }
        else
        {
            //we are on the correct thread, do the task
            final boolean success = loadSteps.get(stepIndex).doStep();
            if(!success)
            {
                //task has failed
                callCallback(success, callback, onAsyncThread);
            }
            else
            {
                //The task executed successfully, do next step
                load(stepIndex + 1, callback, onAsyncThread);
            }
        }
    }
    
    private void callCallback(final boolean success, final LoadCallback callback, final boolean onAsyncThread)
    {
        if(callback == null)
            return;
        
        if(onAsyncThread)
        {
            //move to the right thread, then call callback
            plugin.getServer().getScheduler().runTask(plugin, () -> callback.reloadFinished(success));
        }
        else
        {
            callback.reloadFinished(success);
        }
    }
    
    private boolean isAsync(int stepIndex)
    {
        if(asyncSteps == null) return preAsyncSteps.contains(stepIndex);
        return asyncSteps.contains(stepIndex);
    }
}
