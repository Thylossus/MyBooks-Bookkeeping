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

package model.types;

/**
 * Enumerator for directory types.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public enum Directory {
    CLIENT(0, "client"),
    DOCUMENTS(1, "documents");

    //Member Variables
    private int id;
    private String name;
    
    /**
     * Construct a Directory
     * @param id The constant's id
     * @param name The constant's identifier string
     */
    Directory(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Get the constant's id.
     * @return The constant's id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Get the constant's string identifier.
     * @return The constan's string identifier
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Search for a specific constant by its id.
     * @param id The id of the searched constant.
     * @return If the given id matches the id of a constant, the credit card
     * type is returned. Otherwise the <code>DOCUMENTS</code> type is returned.
     */
    public static Directory getDirectoryById(int id) {
        for (Directory c : Directory.values()) {
            if (c.getId() == id) {
                return c;
            }
        }
        
        return DOCUMENTS;
    }
    
    /**
     * Search for a specific constant by its string identifier.
     * @param name The string identifier of the searched constant.
     * @return If the given string identifier matches the string identifier of a defined constant, 
     * the constant is returned. Otherwise the <code>DOCUMENTS</code> type is returned.
     */
    public static Directory getDirectoryByIdentifier(String name) {
        for (Directory c : Directory.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        
        return DOCUMENTS;
    }
    
}