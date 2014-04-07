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

package database;

/**
 * Construct specific active record objects by their select table/view.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public abstract class ActiveRecordFactory {

    /**
     * Finds the class of an active record for a given table/view name.
     * @param table A string that identifies a table or view.
     * @return the identified active record class.
     * @throws Exception when the provided table does not match with any of the select tables of the implemented active records.
     */
    public static Class<?> createActiveRecord(String table) throws Exception{
        
        if (DownloadFile.SELECT_TABLE.equalsIgnoreCase(table)) {
            return DownloadFile.class;
        }
        
        if (User.SELECT_TABLE.equalsIgnoreCase(table)) {
            return User.class;
        }
        
        //If no active records has the specified select table, throw an exception.
        throw new Exception("Could not find any active record, which uses the specified select table!");
    }
    
}
