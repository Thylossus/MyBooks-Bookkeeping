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
 * This class provides an simple way of creating filters for SQL queries.
 * Constraints can be added and afterwards an SQL WHERE clause is generated.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class DBFilter {

    
    /**
     * Builds an SQL WHERE clause based on the contraints that have been added to the object.
     * @return an SQL WHERE clause or an empty string (no contraints were defined).
     */
    public String buildWhereClause() {
        return "";
    }
    
}
