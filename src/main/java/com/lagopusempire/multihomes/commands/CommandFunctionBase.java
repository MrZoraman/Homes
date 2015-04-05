package com.lagopusempire.multihomes.commands;

import com.lagopusempire.multihomes.config.ConfigKeys;
import com.lagopusempire.multihomes.config.PluginConfig;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.Permissions;
import com.lagopusempire.multihomes.util.UUIDFetcher;
import com.lagopusempire.multihomes.util.Util;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class CommandFunctionBase
{
    
    
    protected final JavaPlugin plugin;
    
    public CommandFunctionBase(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }
    
    
    
    
}
