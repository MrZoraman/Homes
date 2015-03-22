package com.lagopusempire.multihomes;

/**
 *
 * @author MrZoraman
 */
@FunctionalInterface
public interface ReloadCallback
{
    public void reloadFinished(boolean success);
}
