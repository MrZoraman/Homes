package com.lagopusempire.multihomes.help;

/**
 *
 * @author MrZoraman
 */
public enum HelpKeys
{
    EXAMPLE ("example");
    
    private final String key;

    private HelpKeys(String key)
    {
        this.key = key;
    }

    String getKey()
    {
        return key;
    }
}
