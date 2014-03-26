package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class ActiveRecord
{
    static String DBASE_URL =
                "jdbc:derby://localhost:1527/MyBooks";

    protected String databaseUsername, databasePassword;
    
    protected static Connection getDatabaseConnection(String username, String password) throws Exception
    {
        Connection con = null;

        try
        {     
            con = DriverManager.getConnection(DBASE_URL, username, password);
        }
        catch (SQLException sqle)
        {
            String msg = "Cannot establish a connection to the database";
            throw new Exception(msg, sqle);
        }
        finally
        {
            return con;
        }
    }

    protected static void closeDatabaseConnection(Connection con) throws Exception
    {
        try
        {
            con.close();
        }
        catch (SQLException sqle)
        {
            String msg = "Problem closing the connection to the database";
            throw new Exception(msg, sqle);
        }
    }
}
