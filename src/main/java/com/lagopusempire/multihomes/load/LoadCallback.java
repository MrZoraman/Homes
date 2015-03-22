package com.lagopusempire.multihomes.load;

/**
 *
 * @author MrZoraman
 */
@FunctionalInterface
public interface LoadCallback
{
    public void reloadFinished(boolean success);
}
