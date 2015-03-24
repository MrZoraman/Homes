package com.lagopusempire.multihomes.permissions;

import org.bukkit.permissions.Permissible;

/**
 *
 * @author MrZoraman
 */
public enum Permissions
{
    RELOAD  ("multihomes.reload"),
    HELP    ("multihomes.help"),
    
    SET_HOME("multihomes.set.self",
             "multihomes.sethome");

    private Permissions(String... nodes)
    {
        if(nodes.length < 1) 
            throw new IllegalStateException("Permission " + toString() + " must have at least one node! (This is a programmer error)");
        
        this.nodes = nodes;
    }

    private final String[] nodes;
    
    public String getNode()
    {
        return nodes[0];
    }

    public boolean check(Permissible p)
    {
        boolean hasPermission = false;
        
        for(int ii = 0; ii < nodes.length; ii++)
        {
            hasPermission |= p.hasPermission(nodes[ii]);
        }
        
        return hasPermission;
    }
}
