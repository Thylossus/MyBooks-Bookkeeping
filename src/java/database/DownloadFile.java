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
 * Active record for DownloadFiles
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class DownloadFile extends ActiveRecord implements DBDeletable, DBInsertable, DBUpdatable{

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "";
    
    //SQL code
    /**
     * SQL query for selecting all users from the USERS table.
     */
    private static final String SELECT_ALL = 
            "SELECT * " + 
            "FROM " + DownloadFile.SELECT_TABLE;
    
    //Attributes
    
    
    //Functions
    /**
     * Insert the <code>DownloadFile</code> specified in this active record into the database.
     * @return Returns true if the insertion was successfull and false otherwise.
     */
    @Override
    public boolean insert() {
        return false;
    }
    
    /**
     * Update the <code>DownloadFile</code> specified in this active record in the database.
     * @return Returns true if the update was successfull and false otherwise.
     */
    @Override
    public boolean update() {
        return false;
    }
    
    /**
     * Removes the <code>DownloadFile</code> from the database.
     * @return Returns true if the deletion was successfull and false otherwise.
     */
    @Override
    public boolean delete() {
        return false;
    }
    
    //Getter & Setter
    
    
}

