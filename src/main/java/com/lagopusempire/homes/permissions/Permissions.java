package com.lagopusempire.homes.permissions;

import org.bukkit.permissions.Permissible;

/**
 *
 * @author MrZoraman
 */
public enum Permissions
{
    RELOAD              ("homes.reload"),
    HELP                ("homes.help"),
    
    RETURN_HOME_ON_DEATH("homes.ondeath"),
    
    //self
    SET_HOME            ("homes.set.self"),
    GO_HOME             ("homes.home.self"),
    LIST_HOMES          ("homes.list.self"),
    DELETE_HOME         ("homes.delete.self"),
    
    //other
    SET_HOME_OTHER      ("homes.set.other"),
    GO_HOME_OTHER       ("homes.home.other"),
    LIST_HOMES_OTHER    ("homes.list.other"),
    DELETE_HOME_OTHER   ("homes.delete.other");
    
    private static final String NODE_PREFIX = "homes";
    
    public static String getNodePrefix()
    {
        return NODE_PREFIX;
    }

    private Permissions(String node)
    {
        this.node = node;
    }

    private final String node;
    
    public String getNode()
    {
        return node;
    }

    public boolean check(Permissible p)
    {
        return p.hasPermission(node);
    }
}
