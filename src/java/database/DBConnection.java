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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that handles the database connection. The connection is used as a
 * singleton.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class DBConnection {
    /**
     * Singleton instance of the <code>DBConnection</code> class.
     */
    private static DBConnection _instance = null;
    
    /**
     * Database URL
     */
    public final String DB_URL = "jdbc:derby://localhost:1527/MyBooks";
    /**
     * Database user
     */
    private final String DB_USER = "administrator";
    /**
     * Database password
     */
    private final String DB_PASS = "a\"U:iX/&3%=J!7G4N(uF";
    
    /**
     * Database connection
     */
    private Connection connection;
    
    /**
     * Get the singelton instance.
     * @return instance of <code>DBConnection</code>.
     * @throws DBException if there is an error during establishing a database connection.
     */
    public static synchronized DBConnection getInstance() throws DBException {
        if (DBConnection._instance == null || DBConnection._instance.getConnection() == null) {
            DBConnection._instance = new DBConnection();
        }
        
        return DBConnection._instance;
    }
    
    /**
     * Get the actual connection object of the database connection.
     * @return the database connection.
     */
    public Connection getConnection() {
        return this.connection;
    }
    
    /**
     * Construct an instance of <code>DBConnection</code> by establishing a 
     * database connection. To ensure that <code>DBConnection</code> holds
     * the only instance of this class, the constructor is private.
     * @throws DBException if the connection could not be established.
     */
    private DBConnection() throws DBException {
        this.connection = this.establishConnection();
    }
    
    /**
     * Establish a database connection.
     * @return Returns the database connection object.
     * @throws DBException If the connection could not be established, a <code>DBException</code> is thrown.
     */
    private Connection establishConnection() throws DBException
    {
        Connection con = null;
        
        try {
            con = DriverManager.getConnection(this.DB_URL, this.DB_USER, this.DB_PASS);
        } catch (SQLException sqle) {
           
            String msg = "Cannot establish a database connection.";
            throw new DBException(msg, sqle, 0);
        
        } finally {
            return con;
        }
    }

    /**
     * Close a database connection.
     * @param con A database connection object.
     * @throws DBException If the connection could not be closed, a <code>DBException</code> is thrown.
     */
    private void closeDatabaseConnection(Connection con) throws DBException
    {
        try
        {
            con.close();
        }
        catch (SQLException sqle)
        {
            String msg = "Cannot close the database connection.";
            throw new DBException(msg, sqle, 1);
        }
    }
}
