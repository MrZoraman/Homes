package com.lagopusempire.multihomes.permissions;

import org.bukkit.permissions.Permissible;

/**
 *
 * @author MrZoraman
 */
public enum Permissions
{
    HELP("multihomes.help");

    private Permissions(String node)
    {
        this.node = node;
    }

    private final String node;

    public boolean check(Permissible p)
    {
        return p.hasPermission(node);
    }
}
