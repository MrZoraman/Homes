package com.lagopusempire.multihomes.permissions;

import org.bukkit.permissions.Permissible;

/**
 *
 * @author MrZoraman
 */
public enum Permissions
{
    RELOAD              ("multihomes.reload"),
    HELP                ("multihomes.help"),
    
    SET_HOME            ("multihomes.set.self"),
    GO_HOME             ("multihomes.home.self"),
    LIST_HOMES          ("multihomes.list.self"),
    DELETE_HOME         ("multihomes.delete.self"),
    SET_HOME_OTHER      ("multihomes.set.other"),
    DELETE_HOME_OTHER   ("multihomes.delete.other"),
    LIST_HOMES_OTHER    ("multihomes.list.other"),
    GO_HOME_OTHER       ("multihomes.home.other");

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
