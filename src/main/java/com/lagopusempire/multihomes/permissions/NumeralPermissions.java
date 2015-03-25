package com.lagopusempire.multihomes.permissions;

import java.util.logging.Level;
import org.bukkit.Bukkit;
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
        int largestAmountFound = -1;
        
        for (PermissionAttachmentInfo perm : permissible.getEffectivePermissions())
        {
            if (perm.getPermission().startsWith(node))
            {
                final String[] permParts = perm.getPermission().split("\\.");
                final String amount = permParts[permParts.length - 1];
                try
                {
                    int amountInt = Integer.parseInt(amount);
                    if(amountInt > largestAmountFound)
                        largestAmountFound = amountInt;
                }
                catch (NumberFormatException e)
                {
                    Bukkit.getLogger().log(Level.WARNING, perm.getPermission() + " contains an invalid amount! (" + amount + ")");
                }
            }
        }

        return largestAmountFound;
    }
}
