package com.lagopusempire.multihomes.messages;

import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author MrZoraman
 */
public class Messages
{
    private Messages() { }
    
    private static FileConfiguration messages;
    
    public static void setMessages(FileConfiguration messages)
    {
        Messages.messages = messages;
    }
    
    public static MessageFormatter getMessage()
    {
        return null;
    }
}
