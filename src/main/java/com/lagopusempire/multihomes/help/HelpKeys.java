package com.lagopusempire.multihomes.help;

/**
 *
 * @author MrZoraman
 */
public enum HelpKeys
{
    SEPARATOR       ("separator"),
    HEADER          ("header.message"),
    HEADER_ENABLED  ("header.enabled"),
    INITIAL_STRIP   ("consoleStartStipLength");
    
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
