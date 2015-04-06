package com.lagopusempire.homes.help;

import com.lagopusempire.homes.messages.MessageFormatter;
import com.lagopusempire.homes.permissions.Permissions;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author MrZoraman
 */
public class Help
{
    private static final String PATH = "entries.";
    
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
    
    public static boolean getBoolean(HelpKeys key)
    {
        return helpMessages.getBoolean(key.getKey());
    }
    
    public static int getInt(HelpKeys key)
    {
        return helpMessages.getInt(key.getKey());
    }
    
    public static boolean getEnabled(Permissions perm)
    {
        return helpMessages.getBoolean(PATH + perm.getNode().replace(".", "_") + ".enabled");
    }
    
    public static boolean exists(Permissions perm)
    {
        return helpMessages.contains(PATH + perm.getNode().replace(".", "_"));
    }
    
    public static MessageFormatter getCommand(Permissions perm)
    {
        return MessageFormatter.create(helpMessages.getString(PATH + perm.getNode().replace(".", "_") + ".command"));
    }
    
    public static MessageFormatter getHelp(Permissions perm)
    {
        return MessageFormatter.create(helpMessages.getString(PATH + perm.getNode().replace(".", "_") + ".help"));
    }
}
