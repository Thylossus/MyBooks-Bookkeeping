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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Active record for balance sheets
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class BalanceSheet extends ActiveRecord implements DBDeletable, DBInsertable, DBUpdatable{

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "BALANCE_SHEETS";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "BALANCE_SHEETS";
    
    //Columns
    public static final String CLMN_ID = "ID";
    public static final String CLMN_TITLE = "TITLE";
    public static final String CLMN_DATE_OF_LAST_CHANGE = "DATA_OF_LAST_CHANGE";
    public static final String CLMN_DATE_OF_CREATION = "DATE_OF_CREATION";
    public static final String CLMN_OWNER = "OWNER";

    //SQL code
    /**
     * SQL query for selecting all users from the USERS table.
     */
    private static final String SELECT_ALL = 
            "SELECT * " + 
            "FROM " + BalanceSheet.SELECT_TABLE;
    
    //Attributes
    /**
     * The balance sheet's id.
     */
    private int id;
    /**
     * The balance sheet's title.
     */
    private String title;
    /**
     * The balance sheet's date of last change.
     */
    private Timestamp dateOfLastChange;
    /**
     * The balance sheet's date of creation.
     */
    private Timestamp dateOfCreation;
    /**
     * The balance sheet's owner id.
     */
    private int owner;
    
    //Construction
    /**
     * Empty constructor for creating non-initialised BalanceSheetss.
     */
    public BalanceSheet(){
        this.id = 0;
        this.title = null;
        this.dateOfLastChange = null;
        this.dateOfCreation = null;
        this.owner = 0;
    }
    /**
     * Construct a BalanceSheet from a result set.
     * @param rs a result set containing data about a BalanceSheet.
     * @throws DBException if reading from the result set failed.
     */
    public BalanceSheet(ResultSet rs) throws DBException{
        try {
            this.id = rs.getInt(BalanceSheet.CLMN_ID);
            this.title = rs.getString(BalanceSheet.CLMN_TITLE);
            this.dateOfLastChange = rs.getTimestamp(BalanceSheet.CLMN_DATE_OF_LAST_CHANGE);
            this.dateOfCreation = rs.getTimestamp(BalanceSheet.CLMN_DATE_OF_CREATION);
            this.owner = rs.getInt(BalanceSheet.CLMN_OWNER);
        } catch (SQLException sqle) {
            String msg = "An error occured while reading from a result set.";
            throw new DBException(msg, sqle, 2);
        }
    }
    
    //Selection
    /**
     * Execute an SQL query that is provided as a string.
     * @param SQL a string containing a valid SQL statement.
     * @return A list of BalanceSheets that may also be empty.
     */
    private static ArrayList<BalanceSheet> executeSelection(String SQL) {
        ArrayList<BalanceSheet> balanceSheets = new ArrayList<>();
        Connection con;
        
        try {
            con = DBConnection.getInstance().getConnection();
            try(PreparedStatement stmt = con.prepareStatement(SQL)) {
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()) {
                    balanceSheets.add(new BalanceSheet(rs));
                }
                
                rs.close();
            } catch (SQLException ex) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, new DBException(msg, ex, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return balanceSheets;
    }
    /**
     * Find all balance sheets.
     *
     * @return a list of all balance sheets encapsulated in active record objects.
     */
    public static ArrayList<BalanceSheet> findAll() {
        return BalanceSheet.executeSelection(BalanceSheet.SELECT_ALL);
    }

    /**
     * Find all balance sheets and order the results.
     *
     * @param orderBy an array of column names that will be used for sorting.
     * @return a list of all balance sheets encapsulated in active record objects.
     */
    public static ArrayList<BalanceSheet> findAll(final String[] orderBy) {
        return BalanceSheet.executeSelection(BalanceSheet.SELECT_ALL + ActiveRecord.buildOrderBy(orderBy));
    }

    /**
     * Find all balance sheets and filter the results.
     *
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all balance sheets encapsulated in active record objects.
     */
    public static ArrayList<BalanceSheet> findAll(DBFilter filter) {
        try {
            return BalanceSheet.executeSelection(BalanceSheet.SELECT_ALL + filter.buildWhereClause());
        } catch (DBException ex) {
            Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Find all balance sheets and order and filter the results.
     *
     * @param orderBy an array of colunn names that will be used for sorting.
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all balance sheets encapsulated in active record objects.
     */
    public static ArrayList<BalanceSheet> findAll(final String[] orderBy, DBFilter filter) {
        try {
            return BalanceSheet.executeSelection(BalanceSheet.SELECT_ALL + filter.buildWhereClause() + ActiveRecord.buildOrderBy(orderBy));
        } catch (DBException ex) {
            Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Modification 
    /**
     * Insert the <code>BalanceSheet</code> specified in this active record into the database.
     * @return Returns true if the insertion was successfull and false otherwise.
     */
    @Override
    public boolean insert() {
        Connection con;
        String sql = "INSERT INTO " + BalanceSheet.MODIFY_TABLE
                + "(" + BalanceSheet.CLMN_TITLE + ", " + BalanceSheet.CLMN_OWNER + ")"
                + " VALUES "
                + "(?, ?)";

        try {
            con = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, this.title);
                stmt.setInt(2, this.owner);

                if (stmt.executeUpdate() != 1) {
                    Logger.getLogger(BalanceSheet.class.getName()).log(Level.WARNING, "Failed to insert new balance sheet in DB!");
                } else {
                    //Get the id of the new user
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        this.id = rs.getInt(1);
                    }
                }

            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
    
    /**
     * Update the <code>BalanceSheet</code> specified in this active record in the database.
     * @return Returns true if the update was successfull and false otherwise.
     */
    @Override
    public boolean update() {
        if (this.id != 0
                && this.title != null
                && this.dateOfLastChange != null) {
            Connection con;
            String sql = "UPDATE " + BalanceSheet.MODIFY_TABLE
                    + " SET " + BalanceSheet.CLMN_TITLE + " = ? ,"
                    + " " + BalanceSheet.CLMN_DATE_OF_LAST_CHANGE + " = ? "
                    + " WHERE " + BalanceSheet.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setString(1, this.title);
                    stmt.setTimestamp(2, this.dateOfLastChange);
                    stmt.setInt(3, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(BalanceSheet.class.getName()).log(Level.WARNING, "Failed to update balance sheet in DB!");
                    }
                } catch (SQLException sqle) {
                    //Statement failed
                    String msg = "Failed to prepare the SQL statement.";
                    Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Removes the <code>BalanceSheet</code> from the database.
     * @return Returns true if the deletion was successfull and false otherwise.
     */
    @Override
    public boolean delete() {
        if (this.id != 0) {
            Connection con;
            String sql = "DELETE FROM " + BalanceSheet.MODIFY_TABLE
                    + " WHERE " + BalanceSheet.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setInt(1, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(BalanceSheet.class.getName()).log(Level.WARNING, "Failed to delete balance sheet from DB!");
                    }
                } catch (SQLException sqle) {
                    //Statement failed
                    String msg = "Failed to prepare the SQL statement.";
                    Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }
    
    //Getter & Setter
    /**
     * Get the balance sheet's id.
     * @return the balance sheet's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the balance sheet's title.
     * @return title as a string.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the balance sheet's title.
     * @param title title as a string.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the timestamp of the last change.
     * @return timestamp of the last change.
     */
    public Timestamp getDateOfLastChange() {
        return dateOfLastChange;
    }

    /**
     * Set the timestamp of the last change.
     * @param dateOfLastChange timestamp of the last change.
     */
    public void setDateOfLastChange(Timestamp dateOfLastChange) {
        this.dateOfLastChange = dateOfLastChange;
    }

    /**
     * Get the date of creation.
     * @return timestamp of date of creation.
     */
    public Timestamp getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Get the id of the balance sheet's owner.
     * @return the balance sheet's owner id.
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Set the balance sheet's owner id. Use only intially. Will be ignored by <code>update()</code>.
     * @param owner the owner's id.
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }
    
    
}

