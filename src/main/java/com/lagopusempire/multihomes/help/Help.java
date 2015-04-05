package com.lagopusempire.multihomes.help;

import com.lagopusempire.multihomes.messages.MessageFormatter;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author MrZoraman
 */
public class Help
{
    private Help() { }
    
    private static FileConfiguration helpMessages;
    
    public static void setHelpMessages(FileConfiguration helpMessages)
    {
        Help.helpMessages = helpMessages;
    }
    
    public static MessageFormatter getMessage(HelpKeys key)
    {
        return MessageFormatter.create(helpMessages.getString(key.getKey()));
    }
}
