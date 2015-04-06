package com.lagopusempire.multihomes.homeIO.database;

import com.lagopusempire.multihomes.HomesPlugin;
import com.lagopusempire.multihomes.home.Coordinates;
import com.lagopusempire.multihomes.home.Home;
import com.lagopusempire.multihomes.home.LoadResult;
import com.lagopusempire.multihomes.homeIO.HomeIO;
import com.lagopusempire.multihomes.util.Util;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.world.WorldLoadEvent;

import static com.lagopusempire.multihomes.homeIO.database.ScriptKeys.*;

/**
 *
 * @author MrZoraman
 */
public class DBHomeIO implements HomeIO, Listener
{
    private final HomesPlugin plugin;
    private Connection conn;
    
    public DBHomeIO(HomesPlugin plugin, Connection conn)
    {
        this.plugin = plugin;
        this.conn = conn;
    }

    @Override
    public void saveHome(final Home home)
    {
        final UUID uuid = home.getOwner();
        
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
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Home> loadHomes(UUID uuid)
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
        finally
        {
            return homes;
        }
    }

    @Override
    public Home loadHome(UUID uuid, String homeName)
    {
        verifyConnection();

        return getHome(uuid, homeName);
    }

    @Override
    public List<String> getHomeList(UUID uuid)
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
        finally
        {
            Collections.sort(homes);
            return homes;
        }
    }
    
    @Override
    public int getHomeCount(UUID uuid)
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
        finally
        {
            return homeCount;
        }
    }

    @Override
    public boolean deleteHome(UUID uuid, String homeName)
    {
        verifyConnection();

        final Home home = getHome(uuid, homeName);
        if(home.getHomeLoadPackage().loadResult == LoadResult.DOES_NOT_EXIST)
        {
            return false;
        }

        final String query = Scripts.getScript(DELETE_HOME);
        try(final PreparedStatement stmt = conn.prepareStatement(query))
        {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, homeName);

            stmt.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            return true;
        }
    }
    
    @Override
    public boolean shouldBeAsync()
    {
        return true;
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
        catch(Exception e)
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
    
    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event)
    {
        try(final PreparedStatement stmt = conn.prepareStatement(Scripts.getScript(CALL_ADD_UUID_PROC)))
        {
            stmt.setString(1, event.getUniqueId().toString());
            
            stmt.executeQuery();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            event.setKickMessage("Failed to log you in due to server internal error.");
            event.setLoginResult(Result.KICK_OTHER);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event)
    {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            try(final PreparedStatement stmt = conn.prepareStatement(Scripts.getScript(CALL_ADD_WORLD_PROC)))
            {
                stmt.setString(1, event.getWorld().getName());

                stmt.executeQuery();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        });
    }
    
    @Override
    public void registerEvents()
    {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void unregisterEvents()
    {
        AsyncPlayerPreLoginEvent.getHandlerList().unregister(this);
        WorldLoadEvent.getHandlerList().unregister(this);
    }
    
    @Override
    public void onLoad()
    {
        final Set<String> worldNames = new HashSet<>();
        plugin.getServer().getWorlds().forEach((world) -> worldNames.add(world.getName()));
        
        final Set<UUID> playerUUIDs = new HashSet<>();
        plugin.getServer().getOnlinePlayers().forEach((player) -> playerUUIDs.add(player.getUniqueId()));
        
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> 
        {
            try
            {
                String script = Scripts.getScript(CALL_ADD_WORLD_PROC);
                for(String worldName : worldNames)
                {
                    try (PreparedStatement stmt = conn.prepareStatement(script))
                    {
                        stmt.setString(1, worldName);
                        stmt.execute();
                    }
                }
                
                script = Scripts.getScript(CALL_ADD_UUID_PROC);
                for(UUID uuid : playerUUIDs)
                {
                    try (PreparedStatement stmt = conn.prepareStatement(script))
                    {
                        stmt.setString(1, uuid.toString());
                        stmt.execute();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }
}
