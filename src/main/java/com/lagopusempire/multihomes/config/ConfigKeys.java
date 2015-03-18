package com.lagopusempire.multihomes.config;

/**
 *
 * @author MrZoraman
 */
public enum ConfigKeys
{
    USE_DATABASE ("useDatabase"),
    MIGRATE_DATA ("migrateData"),
    IMPLICIT_HOME_NAME ("implicitHomeName");
    
    private final String key;
    
    private ConfigKeys(String key)
    {
        this.key = key;
    }

    String getKey()
    {
        return key;
    }
}
