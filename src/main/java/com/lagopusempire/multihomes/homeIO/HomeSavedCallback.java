package com.lagopusempire.multihomes.homeIO;

/**
 *
 * @author MrZoraman
 */
@FunctionalInterface
public interface HomeSavedCallback
{
    public void homeSaved(boolean wasUpdate);
}
