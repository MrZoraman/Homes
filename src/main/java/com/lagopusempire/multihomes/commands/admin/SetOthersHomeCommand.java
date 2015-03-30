package com.lagopusempire.multihomes.commands.admin;

import com.lagopusempire.multihomes.HomeManager;
import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.commands.CommandBase;
import com.lagopusempire.multihomes.messages.MessageFormatter;
import com.lagopusempire.multihomes.messages.MessageKeys;
import com.lagopusempire.multihomes.messages.Messages;
import com.lagopusempire.multihomes.permissions.NumeralPermissions;
import com.lagopusempire.multihomes.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author MrZoraman
 */
public class SetOthersHomeCommand extends CommandBase
{
    public SetOthersHomeCommand(MultiHomes plugin, HomeManager homeManager)
    {
        super(plugin, homeManager);
    }
    
    @Override
    protected boolean onCommand(Player player, String[] args)
    {
//        // /home set other [playername] (homename)
//        // /home set other [playername] (homename) force
//        
//        if(!checkPerms(player, Permissions.SET_HOME_OTHER))
//            return true;
//        
//        String targetName_m = null;
//        boolean usingImplicitHome_m = true;
//        String homeName_m = null;
//        boolean force_m = false;
//        
//        switch(args.length)
//        {
//            default:
//                if(args[2].equalsIgnoreCase("force"))
//                    force_m = true;
//                //FALL THROUGH
//            case 2:
//                if(args[1].equalsIgnoreCase("force"))
//                {
//                    force_m = true;
//                    break;
//                }
//                else
//                {
//                    homeName_m = args[1];
//                    usingImplicitHome_m = false;
//                }
//                //FALL THROUGH
//            case 1:
//                targetName_m = args[0];
//                //FALL THROUGH
//            case 0:
//                final String message = Messages.getMessage(MessageKeys.MUST_SPECIFY_PLAYER).colorize().toString();
//                player.sendMessage(message);
//                return true;
//        }
//        
//        final String targetName_i = targetName_m;
//        final boolean usingImplicitHome_i = usingImplicitHome_m;
//        final String homeName_i = homeName_m;
//        final boolean force_i = force_m;
//        
//        //get the target
//        getPlayer(targetName_i, (targetName, target) -> 
//        {
//            if(target == null)
//            {
//                player.sendMessage(Messages.getMessage(MessageKeys.PLAYER_NOT_FOUND).colorize().toString());
//                return;
//            }
//            
//            final Player targetPlayer = Bukkit.getPlayer(target);
//            
//            //save the home
//            homeManager.setHome(player, target, homeName_i, (wasUpdate) -> 
//            {
//                //get how many homes target has
//                homeManager.getHomeCount(target, (amount) -> 
//                {
//                    if(!wasUpdate && !force_i)//new home, and it is not being forced
//                    {
//                        //Check if player has space for another home
//                        final int maxHomes = NumeralPermissions.COUNT.getAmount(targetPlayer);
//                        if (maxHomes >= 0 && amount >= maxHomes)
//                        {
//                            MessageFormatter formatter = Messages.getMessage(MessageKeys.HOME_SET_OTHER_TOOMANY_MESSAGE)
//                                    .colorize()
//                                    .replace("max", String.valueOf(maxHomes))
//                                    .replace("player", targetName);
//
//                            player.sendMessage(formatter.toString());
//                            
//                            if(Permissions.SET_HOME_OTHER_FORCE.check(player))
//                            {
//                                formatter = Messages.getMessage(MessageKeys.HOME_SET_OTHER_TOOMANY_SUGGEST)
//                                        .colorize();
//                                player.sendMessage(formatter.toString());
//                            }
//                            return;
//                        }
//                    }
//                    
//                    final MessageKeys key = usingImplicitHome_i
//                        ? MessageKeys.HOME_SET_OTHER_EXPLICIT
//                        : MessageKeys.HOME_SET_OTHER_IMPLICIT;
//
//                    final MessageFormatter formatter = Messages.getMessage(key)
//                            .colorize()
//                            .replace("home", homeName_i)
//                            .replace("player", targetName);
//
//                    player.sendMessage(formatter.toString());
//                });
//            });
//        });
        
        return true;
    }
}
