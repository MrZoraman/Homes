package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.MultiHomes;
import com.lagopusempire.multihomes.home.Coordinates;
import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.LoadResult;
import com.lagopusempire.multihomes.homeIO.HomeCountCallback;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import com.lagopusempire.multihomes.homeIO.HomeListLoadedCallback;
import com.lagopusempire.multihomes.homeIO.HomeLoadedCallback;
import com.lagopusempire.multihomes.homeIO.HomesLoadedCallback;
import com.lagopusempire.multihomes.util.Util;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static com.lagopusempire.multihomes.homeIO.database.ScriptKeys.*;

/**
 *
 * @author MrZoraman
 */
public class DBHomeIO implements HomeIO
{
    private final MultiHomes plugin;
    private Connection conn;
    
    public DBHomeIO(MultiHomes plugin, Connection conn)
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
            verifyConnection();
            
            if(getHome(uuid, home.getName()).getHomeLoadPackage().loadResult == LoadResult.DOES_NOT_EXIST)
            {
                //home does not exist
                final String query = Scripts.getScript(CREATE_HOME);
                try(final PreparedStatement stmt = conn.prepareStatement(query))
                {
                    stmt.setString(1, home.getOwner().toString());
                    stmt.setString(2, home.getName());
                    stmt.setDouble(3, home.getCoords().x);
                    stmt.setDouble(4, home.getCoords().y);
                    stmt.setDouble(5, home.getCoords().z);
                    stmt.setFloat (6, home.getCoords().yaw);
                    stmt.setFloat (7, home.getCoords().pitch);
                    stmt.setString(8, home.getWorldName());
                    
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
                    stmt.setDouble(1, home.getCoords().x);
                    stmt.setDouble(2, home.getCoords().y);
                    stmt.setDouble(3, home.getCoords().z);
                    stmt.setFloat (4, home.getCoords().yaw);
                    stmt.setFloat (5, home.getCoords().pitch);
                    stmt.setString(6, home.getWorldName());
                    
                    stmt.setString(7, uuid.toString());
                    stmt.setString(8, home.getName());
                    
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
            verifyConnection();
            
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
                        
                        final Coordinates coords = new Coordinates();
                        
                        coords.x = rs.getDouble("x");
                        coords.y = rs.getDouble("y");
                        coords.z = rs.getDouble("z");
                        coords.yaw = rs.getFloat("yaw");
                        coords.pitch = rs.getFloat("pitch");
                        
                        final String worldName = rs.getString("world_name");
                        
                        final Home home = new Home(uuid, homeName, coords, worldName);
                        
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
            verifyConnection();
            
            final Home home = getHome(uuid, homeName);
            
            plugin.getServer().getScheduler().runTask(plugin, () -> callback.homeLoaded(home));
        });
    }

    @Override
    public void getHomeList(UUID uuid, HomeListLoadedCallback callback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            verifyConnection();
            
            final List<String> homes = new ArrayList<>();
            
            final String query = Scripts.getScript(LIST_HOMES);
            try(final PreparedStatement stmt = conn.prepareStatement(query))
            {
                stmt.setString(1, uuid.toString());

                try(final ResultSet rs = stmt.executeQuery())
                {
                    while(rs.next())
                    {
                        homes.add(rs.getString("home_name"));
                    }
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
            
            Collections.sort(homes);
            
            plugin.getServer().getScheduler().runTask(plugin, () -> callback.homeListLoaded(homes));
        });
    }
    
    @Override
    public void getHomeCount(UUID uuid, HomeCountCallback callback, boolean syncCallback)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            verifyConnection();
            
            int homeCount = 0;
            
            final String query = Scripts.getScript(GET_HOME_COUNT);
            try(final PreparedStatement stmt = conn.prepareStatement(query))
            {
                stmt.setString(1, uuid.toString());

                try(final ResultSet rs = stmt.executeQuery())
                {
                    while(rs.next())
                    {
                        homeCount = rs.getInt(1);
                    }
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
            
            if(syncCallback)
            {
                final int _homeCount = homeCount;
                plugin.getServer().getScheduler().runTask(plugin, () -> callback.gotHomeCount(_homeCount));
            }
            else
            {
                callback.gotHomeCount(homeCount);
            }
        });
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
                    final Coordinates coords = new Coordinates();

                    coords.x = rs.getDouble("x");
                    coords.y = rs.getDouble("y");
                    coords.z = rs.getDouble("z");
                    coords.yaw = rs.getFloat("yaw");
                    coords.pitch = rs.getFloat("pitch");

                    final String worldName = rs.getString("world_name");

                    return new Home(uuid, homeName, coords, worldName);
                }
                else
                {
                    return new Home(uuid, homeName);
                }
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    private void verifyConnection()
    {
        try
        {
            if(conn == null || conn.isClosed() || !conn.isValid(10))
            {
                conn = Util.createConnection();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            plugin.disablePlugin();
        }
    }
    
    @Override
    public boolean close()
    {
        try
        {
            if(conn != null)
            {
                conn.close();
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
}
