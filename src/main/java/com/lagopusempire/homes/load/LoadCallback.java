package com.lagopusempire.homes.load;

/**
 *
 * @author MrZoraman
 */
@FunctionalInterface
public interface LoadCallback
{
    public void reloadFinished(boolean success);
}
