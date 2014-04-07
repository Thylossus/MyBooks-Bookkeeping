package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ActiveRecord
{
    public final static String DB_URL =
                "jdbc:derby://localhost:1527/MyBooks";

    private final static String DB_USER = "administrator";
    private final static String DB_PASS = "a\"U:iX/&3%=J!7G4N(uF";
    
    /**
     * Establish a database connection.
     * @return Returns the database connection object.
     * @throws DBException If the connection could not be established, a <code>DBException</code> is thrown.
     */
    protected static Connection getDatabaseConnection() throws DBException
    {
        Connection con = null;

        try
        {     
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        }
        catch (SQLException sqle)
        {
            String msg = "Cannot establish a database connection.";
            throw new DBException(msg, sqle, 0);
        }
        finally
        {
            return con;
        }
    }

    /**
     * Close a database connection.
     * @param con A database connection object.
     * @throws DBException If the connection could not be closed, a <code>DBException</code> is thrown.
     */
    protected static void closeDatabaseConnection(Connection con) throws DBException
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
