package com.lagopusempire.homes.permissions;

import org.bukkit.permissions.Permissible;

/**
 *
 * @author MrZoraman
 */
public enum Permissions
{
    RELOAD              ("multihomes.reload"),
    HELP                ("multihomes.help"),
    
    //self
    SET_HOME            ("multihomes.set.self"),
    GO_HOME             ("multihomes.home.self"),
    LIST_HOMES          ("multihomes.list.self"),
    DELETE_HOME         ("multihomes.delete.self"),
    
    //other
    SET_HOME_OTHER      ("multihomes.set.other"),
    GO_HOME_OTHER       ("multihomes.home.other"),
    LIST_HOMES_OTHER    ("multihomes.list.other"),
    DELETE_HOME_OTHER   ("multihomes.delete.other");
    
    private static final String NODE_PREFIX = "multihomes";
    
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
