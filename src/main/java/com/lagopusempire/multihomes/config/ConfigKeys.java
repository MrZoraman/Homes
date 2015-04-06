package com.lagopusempire.multihomes.config;

/**
 *
 * @author MrZoraman
 */
public enum ConfigKeys
{
    //misc
    USE_DATABASE        ("useDatabase"),
    IMPLICIT_HOME_NAME  ("implicitHomeName"),
    SINGLE_HOME_ONLY    ("single_home_only"),
    
    //MySQL
    SCHEMA_VERSION      ("mysql.schemaVersion"),
    MYSQL_HOST          ("mysql.credentials.host"),
    MYSQL_USERNAME      ("mysql.credentials.username"),
    MYSQL_PASSWORD      ("mysql.credentials.password"),
    MYSQL_PORT          ("mysql.credentials.port"),
    MYSQL_DATABASE      ("mysql.credentials.database"),
    MYSQL_DRIVER        ("mysql.driver"),
    
    UUID_REGEX          ("uuidRegex"),
    UUID_FETCH_URL      ("uuidFetchURL");
    
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
