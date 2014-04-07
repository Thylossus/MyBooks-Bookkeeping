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
 * Enumerator for user types.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public enum UserType {
    GUEST (1, "Guest"),
    STANDARD_USER (2, "Standard User"),
    PREMIUM_USER (3, "Premium User"),
    NEWS_WRITER (4, "News Writer"),
    ADMINISTRATOR (5, "Administrator");
    
    private int id;
    private String identifier;
    
    UserType(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
    }
    
    /**
     * Get the user type's id.
     * @return The user type's id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Get the user type's identifier.
     * @return The user type's identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    /**
     * Search for a specific user type by its id.
     * @param id The id of the searched user type.
     * @return If the given id matches the id of an user type, the user type is
     * returned. Otherwise the <code>GUEST</code> type is returned.
     */
    public static UserType getUserTypeById(int id) {
        for (UserType ut : UserType.values()) {
            if (ut.getId() == id) {
                return ut;
            }
        }
        
        return GUEST;
    }
    
    /**
     * Search for a specific user type by its identifier.
     * @param identifier The identifier of the searched user type.
     * @return If the given identifier matches the identifier of an user type, 
     * the user type is returned. Otherwise the <code>GUEST</code> type is returned.
     */
    public static UserType getUserTypeByIdentifier(String identifier) {
        for (UserType ut : UserType.values()) {
            if (ut.getIdentifier().equals(identifier)) {
                return ut;
            }
        }
        
        return GUEST;
    }
    
}
