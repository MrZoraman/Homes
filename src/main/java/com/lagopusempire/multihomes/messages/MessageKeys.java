package com.lagopusempire.multihomes.messages;

/**
 *
 * @author MrZoraman
 */
public enum MessageKeys
{
    TEST ("test");

    private final String key;

    private MessageKeys(String key)
    {
        this.key = key;
    }

    String getKey()
    {
        return key;
    }
}
