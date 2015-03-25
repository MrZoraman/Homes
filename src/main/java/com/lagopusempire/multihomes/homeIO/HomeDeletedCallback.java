package com.lagopusempire.multihomes.homeIO;

/**
 *
 * @author MrZoraman
 */
@FunctionalInterface
public interface HomeDeletedCallback
{
    public void homeDeleted(boolean somethingGotDeleted);
}
