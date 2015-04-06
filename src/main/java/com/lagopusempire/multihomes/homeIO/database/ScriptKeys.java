package com.lagopusempire.multihomes.homeIO.database;

/**
 *
 * @author MrZoraman
 */
public enum ScriptKeys
{
    CALL_ADD_UUID_PROC   ("scripts/call_add_uuid_proc.sql"),
    CALL_ADD_WORLD_PROC  ("scripts/call_add_world_proc.sql"),
    CREATE_ADD_UUID_PROC ("scripts/create_add_uuid_proc.sql"),
    CREATE_ADD_WORLD_PROC("scripts/create_add_world_proc.sql"),
    CREATE_HOME          ("scripts/create_home.sql"),
    CREATE_HOMES_TABLE   ("scripts/create_homes_table.sql"),
    CREATE_UUIDS_TABLE   ("scripts/create_uuids_table.sql"),
    CREATE_WORLDS_TABLE  ("scripts/create_worlds_table.sql"),
    DELETE_HOME          ("scripts/delete_home.sql"),
    LIST_HOMES           ("scripts/list_homes.sql"),
    LOAD_HOME            ("scripts/load_home.sql"),
    LOAD_HOMES           ("scripts/load_homes.sql"),
    UPDATE_HOME          ("scripts/update_home.sql"),
    GET_HOME_COUNT       ("scripts/get_home_count.sql");
    
    private ScriptKeys(String key)
    {
        this.key = key;
    }
    
    private final String key;
    
    String getKey()
    {
        return key;
    }
}
