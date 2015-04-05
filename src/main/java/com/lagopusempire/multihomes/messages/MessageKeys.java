package com.lagopusempire.multihomes.messages;

/**
 *
 * @author MrZoraman
 */
public enum MessageKeys
{
    RELOAD_SUCCESS                  ("reload.success"),
    RELOAD_FAILURE                  ("reload.failure"),
    NO_PERMISSION                   ("noPermissions"),
    MUST_BE_PLAYER                  ("mustBePlayer"),
    PLUGIN_NOT_LOADED               ("notLoaded"),    
    INFINITE_HOMES_REP              ("infiniteHomesRep"),

    //SELF
    HOME_SET_IMPLICIT               ("self.homeSet.implicit"),
    HOME_SET_EXPLICIT               ("self.homeSet.explicit"),
    HOME_SET_TOO_MANY               ("self.homeSet.tooMany"),
    
    HOME_GET_NOEXIST_IMPLICIT       ("self.homeGet.doesNotExist.implicit"),
    HOME_GET_NOEXIST_EXPLICIT       ("self.homeGet.doesNotExist.explicit"),
    HOME_GET_NOT_LOADED_IMPLICIT    ("self.homeGet.worldNotLoaded.implicit"),
    HOME_GET_NOT_LOADED_EXPLICIT    ("self.homeGet.worldNotLoaded.explicit"),
    
    HOME_LIST_INITIAL               ("self.homeList.initial"),
    HOME_LIST_FORMAT                ("self.homeList.format"),
    HOME_LIST_NONE                  ("self.homeList.none"),
    HOME_LIST_END_STRIP_LENGTH      ("self.homeList.endStripLength"),
    
    HOME_DELETE_NOEXIST_IMPLICIT    ("self.homeDelete.noExist.implicit"),
    HOME_DELETE_NOEXIST_EXPLICIT    ("self.homeDelete.noExist.explicit"),
    HOME_DELETE_SUCCESS_IMPLICIT    ("self.homeDelete.success.implicit"),
    HOME_DELETE_SUCCESS_EXPLICIT    ("self.homeDelete.success.explicit"),
    
    //OTHER
    PLAYER_NOT_FOUND                ("other.playerNotFound"),
    MUST_SPECIFY_PLAYER             ("other.mustSpecifyPlayer"),
    
    HOME_SET_OTHER_IMPLICIT         ("other.homeSet.implicit"),
    HOME_SET_OTHER_EXPLICIT         ("other.homeSet.explicit"),
    
    HOME_DELETE_OTHER_NOEXIST_IMPLICIT ("other.homeDelete.noExist.implicit"),
    HOME_DELETE_OTHER_NOEXIST_EXPLICIT ("other.homeDelete.noExist.explicit"),
    HOME_DELETE_OTHER_SUCCESS_IMPLICIT ("other.homeDelete.success.implicit"),
    HOME_DELETE_OTHER_SUCCESS_EXPLICIT ("other.homeDelete.success.explicit"),
    
    HOME_LIST_OTHER_INITIAL            ("other.homeList.initial"),
    HOME_LIST_OTHER_FORMAT             ("other.homeList.format"),
    HOME_LIST_OTHER_NONE               ("other.homeList.none"),
    HOME_LIST_OTHER_END_STRIP_LENGTH   ("other.homeList.endStripLength");
    
    private final String key;

    private MessageKeys(String key)
    {
        this.key = key;
    }

    String getKey()
    {
        return key;
    }
}
