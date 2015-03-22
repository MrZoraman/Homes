package com.lagopusempire.multihomes.homeIO.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author MrZoraman
 */
public class DatabaseSetup
{
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    
    private final List<DbSetupStep> steps = new ArrayList<>();
    private final JavaPlugin plugin;
    private final String url;
    private final String user;
    private final String password;
    
    @FunctionalInterface
    private interface DbSetupStep
    {
        boolean doStep(Connection conn);
    }
    
    public DatabaseSetup(JavaPlugin plugin, String databaseString, String user, String password)
    {
        this.plugin = plugin;
        this.url = databaseString;
        this.user = user;
        this.password = password;
        
        addSteps();
    }
    
    private void addSteps()
    {
        steps.add((conn) -> 
        {
            final InputStream queryStream = plugin.getResource("scripts/create_homes_table.sql");
            final String query = inputStreamToString(queryStream);
            try
            {
                queryStream.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                return false;
            }
            
            try (PreparedStatement statement = conn.prepareStatement(query))
            {
                statement.executeUpdate();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                return false;
            }
            
            return true;
        });
    }
    
    public boolean setup()
    {
        try
        {
            Class.forName(MYSQL_DRIVER);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        
        final Connection conn;
        try
        {
            System.out.println("conn: " + url);
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        for(int ii = 0; ii < steps.size(); ii++)
        {
            final boolean result = steps.get(ii).doStep(conn);
            if(result == false) return false;
        }
        
        return true;
    }
    
    private String inputStreamToString(InputStream stream)
    {
        try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
            return s.hasNext() ? s.next() : "";
        }
    }
}
