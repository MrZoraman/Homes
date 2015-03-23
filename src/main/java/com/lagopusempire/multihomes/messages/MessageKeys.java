package com.lagopusempire.multihomes.messages;

/**
 *
 * @author MrZoraman
 */
public enum MessageKeys
{
    RELOAD_SUCCESS("reload.success"),
    RELOAD_FAILURE("reload.failure"),
    NO_PERMISSION("noPermissions"),
    LOGIN_AND_STILL_LOADING("loginAndStillLoading");

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
