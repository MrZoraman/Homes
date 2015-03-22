package com.lagopusempire.multihomes.homeIO;

import java.util.List;

/**
 *
 * @author MrZoraman
 */
@FunctionalInterface
public interface HomeListLoadedCallback
{
    public void homeListLoaded(List<String> list);
}
