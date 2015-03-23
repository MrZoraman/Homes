package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.LoadResult;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import com.lagopusempire.multihomes.homeIO.HomeListLoadedCallback;
import com.lagopusempire.multihomes.homeIO.HomeLoadedCallback;
import com.lagopusempire.multihomes.homeIO.HomesLoadedCallback;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import static com.lagopusempire.multihomes.homeIO.database.ScriptKeys.*;

/**
 *
 * @author MrZoraman
 */
public class DBHomeIO implements HomeIO
{
    private final JavaPlugin plugin;
    private final Connection conn;
    
    public DBHomeIO(JavaPlugin plugin, Connection conn)
    {
        this.plugin = plugin;
        this.conn = conn;
    }

    @Override
    public void saveHome(final Home home, final Runnable callback)
    {
        final UUID uuid = home.getOwner();
        
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            if(getHome(uuid, home.getName()) == null)
            {
                //home does not exist
                final String query = Scripts.getScript(CREATE_HOME);
                try(final PreparedStatement stmt = conn.prepareStatement(query))
                {
                    stmt.setString(1, home.getOwner().toString());
                    stmt.setString(2, home.getName());
                    stmt.setDouble(3, home.getLoc().getX());
                    stmt.setDouble(4, home.getLoc().getY());
                    stmt.setDouble(5, home.getLoc().getZ());
                    stmt.setFloat (6, home.getLoc().getYaw());
                    stmt.setFloat (7, home.getLoc().getPitch());
                    stmt.setString(8, home.getLoc().getWorld().getName());
                    
                    stmt.execute();
                    
                    plugin.getServer().getScheduler().runTask(plugin, () -> callback.run());
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                //home exists, update it
                final String query = Scripts.getScript(UPDATE_HOME);
                try(final PreparedStatement stmt = conn.prepareStatement(query))
                {
                    stmt.setDouble(1, home.getLoc().getX());
                    stmt.setDouble(2, home.getLoc().getY());
                    stmt.setDouble(3, home.getLoc().getZ());
                    stmt.setFloat (4, home.getLoc().getYaw());
                    stmt.setFloat (5, home.getLoc().getPitch());
                    stmt.setString(6, home.getLoc().getWorld().getName());
                    
                    stmt.execute();
                    
                    plugin.getServer().getScheduler().runTask(plugin, () -> callback.run());
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadHomes(UUID uuid, HomesLoadedCallback callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            final Map<String, Home> homes = new HashMap<>();
            
            final String query = Scripts.getScript(LOAD_HOMES);
            try(final PreparedStatement stmt = conn.prepareStatement(query))
            {
                stmt.setString(1, uuid.toString());

                try(final ResultSet rs = stmt.executeQuery())
                {
                    while(rs.next())
                    {
                        final String homeName = rs.getString("home_name");
                        final double x = rs.getDouble("x");
                        final double y = rs.getDouble("y");
                        final double z = rs.getDouble("z");
                        final float yaw = rs.getFloat("yaw");
                        final float pitch = rs.getFloat("pitch");
                        final String worldName = rs.getString("world_name");
                        
                        final World world = Bukkit.getWorld(worldName);
                        final Home home;
                        if(world == null)
                        {
                            home = new Home(uuid, homeName, LoadResult.NO_WORLD);
                        }
                        else
                        {
                            final Location loc = new Location(world, x, y, z, yaw, pitch);
                            
                            home = new Home(uuid, homeName, loc);
                        }
                        
                        homes.put(homeName, home);
                    }
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
            
            plugin.getServer().getScheduler().runTask(plugin, () -> callback.homesLoaded(homes));
        });
    }

    @Override
    public void loadHome(UUID uuid, String homeName, HomeLoadedCallback callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            final Home home = getHome(uuid, homeName);
            
            plugin.getServer().getScheduler().runTask(plugin, () -> callback.homeLoaded(home));
        });
    }

    @Override
    public void getHomeList(UUID uuid, HomeListLoadedCallback callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            
            
            plugin.getServer().getScheduler().runTask(plugin, () -> 
            {
                
            });
        });
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Home getHome(UUID uuid, String homeName)
    {
        final String loadHomeQuery = Scripts.getScript(LOAD_HOME);
        try(final PreparedStatement stmt = conn.prepareStatement(loadHomeQuery))
        {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, homeName);

            try(final ResultSet rs = stmt.executeQuery())
            {
                if(rs.next())
                {
                    final double x = rs.getDouble("x");
                    final double y = rs.getDouble("y");
                    final double z = rs.getDouble("z");
                    final float yaw = rs.getFloat("yaw");
                    final float pitch = rs.getFloat("pitch");
                    final String worldName = rs.getString("world_name");

                    final World world = Bukkit.getWorld(worldName);
                    if(world == null)
                    {
                        return new Home(uuid, homeName, LoadResult.NO_WORLD);
                    }
                    else
                    {
                        final Location loc = new Location(world, x, y, z, yaw, pitch);
                        return new Home(uuid, homeName, loc);
                    }
                }
                else
                {
                    return new Home(uuid, homeName, LoadResult.DOES_NOT_EXIST);
                }
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
