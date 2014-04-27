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

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This exception will be thrown by all errors related to the database. It is 
 * a wrapper for SQLExceptions that provides additional details.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version
 */
public class DBException extends Exception{

    private String database = "";
    private int errorCode = -1;
    private SQLException sqle = null;
    
    /**
     * Generate a DBException for an error.
     * @param message Error message
     * @param sqle The related SQLException
     * @param database The related database
     * @param errorCode Error code
     */
    public DBException(String message, SQLException sqle, String database, int errorCode) {
        super(message);
        this.sqle = sqle;
        this.database = database;
        this.errorCode = errorCode;
    }
    
    /**
     * Generate a DBException for an error.
     * @param message Error message
     * @param sqle The related SQLException
     * @param errorCode Error code
     */
    public DBException(String message, SQLException sqle, int errorCode) {
        super(message);
        this.sqle = sqle;
        this.errorCode = errorCode;
        try {
            this.database = DBConnection.getInstance().DB_URL;
        } catch (DBException ex) {
            Logger.getLogger(DBException.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Generate a DBException for an error.
     * @param message Error message
     * @param sqle The related SQLException
     */
    public DBException(String message, SQLException sqle) {
        super(message);
        this.sqle = sqle;
        try {
            this.database = DBConnection.getInstance().DB_URL;
        } catch (DBException ex) {
            Logger.getLogger(DBException.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Generate a DBException for an error.
     * @param message Error message
     * @param errorCode Error code
     */
    public DBException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        try {
            this.database = DBConnection.getInstance().DB_URL;
        } catch (DBException ex) {
            Logger.getLogger(DBException.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Generate a DBException for an error.
     * @param message Error message
     */
    public DBException(String message) {
        super(message);
        try {
            this.database = DBConnection.getInstance().DB_URL;
        } catch (DBException ex) {
            Logger.getLogger(DBException.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get the error code of the exception.
     * @return Error code
     */
    public int getErrorCode() {
        return this.errorCode;
    }
    
    /**
     * Get the related database.
     * @return Name of the related database
     */
    public String getDatabase() {
        return this.database;
    }
    
    /**
     * Get the related SQLException
     * @return Related SQLException
     */
    public SQLException getSQLE() {
        return this.sqle;
    }
    
}
