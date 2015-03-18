package com.lagopusempire.multihomes.permissions;

import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 *
 * @author MrZoraman
 */
public enum NumeralPermissions
{
    COUNT("multihomes.maxhomes.");

    private NumeralPermissions(String node)
    {
        this.node = node;
    }

    private final String node;

    public int getAmount(Permissible permissible)
    {
        for (PermissionAttachmentInfo perm : permissible.getEffectivePermissions())
        {
            if (perm.getPermission().startsWith(node))
            {
                final String[] permParts = perm.getPermission().split("\\.");
                final String amount = permParts[permParts.length - 1];
                return Integer.parseInt(amount.substring(0, amount.length()));
            }
        }

        return -1;
    }
}
