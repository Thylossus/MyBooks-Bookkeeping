/*
 * Copyright (C) 2014 Tobias Kahse <tobias.kahse@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package controller;

import javax.servlet.http.HttpServletRequest;

/**
 * The scope handler provides a central point for handling storing data in or 
 * loading data from the different available scopes. For dealing with parameters
 * the "parameter" scope is introduced. Thus, the scopes "application", "session",
 * "request", and "parameter" are supported. However, the scope "parameter" is only
 * supported for loading, not for storing.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class ScopeHandler {

    /**
     * There is only one instance of the scope handler. That instance is stored
     * in this variable.
     */
    private static ScopeHandler _instance = new ScopeHandler();
    
    /**
     * Get the scope handler instance.
     * @return the scope handler instance.
     */
    public static ScopeHandler getInstance() {
        return ScopeHandler._instance;
    }
    
    /**
     * Construct a scope handler instance. Constructor is private to prevent
     * other classes from creating more scope handlers.
     */
    private ScopeHandler() {
        
    }
    
    /**
     * Store a value in a specified scope with the given key.
     * @param request request object.
     * @param key a string that identifies the stored value.
     * @param value the value to store.
     * @param scope the scope in which the value will be stored.
     */
    public void store(HttpServletRequest request, String key, Object value, String scope) {
        
    }
    
    /**
     * Store a value in the request scope with the given key.
     * @param request request object.
     * @param key a string that identifies the stored value.
     * @param value the value to store.
     */
    public void store(HttpServletRequest request, String key, Object value) {
        
    }
    
    /**
     * Load a value from the specified scope with the given key.
     * @param request request object.
     * @param key a string that identifies a stored value.
     * @param scope the scope from which the value will be loaded.
     * @return the loaded value.
     */
    public Object load(HttpServletRequest request, String key, String scope) {
        return null;
    }
    
    /**
     * Load a value from the request scope with the given key.
     * @param request request object.
     * @param key a string that identifies a stored value.
     * @return the loaded value.
     */
    public Object load(HttpServletRequest request, String key) {
        return null;
    }
    
}
