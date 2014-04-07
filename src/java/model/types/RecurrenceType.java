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
 * Enumerator for recurrence types.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public enum RecurrenceType {
    DAILY (1, "Daily"),
    WEEKLY (2, "Weekly"),
    MONTHLY (3, "Monthly"),
    YEARLY (4, "Yearly");
    
    private int id;
    private String identifier;
    
    RecurrenceType(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
    }
    
    /**
     * Get the recurrence type's id.
     * @return The recurrence type's id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Get the recurrence type's identifier.
     * @return The recurrence type's identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }
    
    /**
     * Search for a specific recurrence type by its id.
     * @param id The id of the searched recurrence type.
     * @return If the given id matches the id of an recurrence type, the recurrence type is
     * returned. Otherwise the <code>DAILY</code> type is returned.
     */
    public static RecurrenceType getRecurrenceTypeById(int id) {
        for (RecurrenceType rt : RecurrenceType.values()) {
            if (rt.getId() == id) {
                return rt;
            }
        }
        
        return DAILY;
    }
    
    /**
     * Search for a specific recurrence type by its identifier.
     * @param identifier The identifier of the searched recurrence type.
     * @return If the given identifier matches the identifier of an recurrence type, 
     * the recurrence type is returned. Otherwise the <code>DAILY</code> type is returned.
     */
    public static RecurrenceType getRecurrenceTypeByIdentifier(String identifier) {
        for (RecurrenceType rt : RecurrenceType.values()) {
            if (rt.getIdentifier().equals(identifier)) {
                return rt;
            }
        }
        
        return DAILY;
    }
    
}
