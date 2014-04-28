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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Active record for records.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Record extends ActiveRecord implements DBDeletable, DBInsertable, DBUpdatable {

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "RECORDS_V1";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "RECORDS";

    //Columns
    public static final String CLMN_BALANCE_SHEET = "BALANCE_SHEET";
    public static final String CLMN_ID = "ID";
    public static final String CLMN_TITLE = "TITLE";
    public static final String CLMN_DESCRIPTION = "DESCRIPTION";
    public static final String CLMN_AMOUNT = "AMOUNT";
    public static final String CLMN_RECORD_DATE = "RECORD_DATE";
    public static final String CLMN_CAT_ID = "CAT_ID";
    //Only for selection
    public static final String CLMN_CAT_NAME = "CAT_NAME";
    public static final String CLMN_CAT_COLOUR = "CAT_COLOUR";
    //For writing the cat_id
    public static final String CLMN_CATEGORY = "CATEGORY";

    //SQL code
    /**
     * SQL query for selecting all Records from the Records table.
     */
    private static final String SELECT_ALL
            = "SELECT * "
            + "FROM " + Record.SELECT_TABLE;

    //Attributes
    /**
     * Id of the balance sheet, which contains the record.
     */
    private int balanceSheet;
    /**
     * Id of the record within the balance sheet.
     */
    private int id;
    /**
     * The record's title.
     */
    private String title;
    /**
     * The record's description.
     */
    private String description;
    /**
     * The record's amount.
     */
    private double amount;
    /**
     * The record's date.
     */
    private Date recordDate;
    /**
     * The id of the record's category.
     */
    private int catId;
    /**
     * The name of the record's category.
     */
    private String catName;
    /**
     * The colour (hex code) of the record's category.
     */
    private String catColour;

    //Construction
    /**
     * Empty constructor for creating non-initialised Records.
     */
    public Record() {
        this.balanceSheet = 0;
        this.id = 0;
        this.title = null;
        this.description = null;
        this.amount = 0.0;
        this.recordDate = null;
        this.catId = 0;
        this.catName = null;
        this.catColour = null;
    }

    /**
     * Construct a Record from a result set.
     *
     * @param rs a result set containing data about a Record.
     * @throws DBException if reading from the result set failed.
     */
    public Record(ResultSet rs) throws DBException {
        try {
            this.balanceSheet = rs.getInt(Record.CLMN_BALANCE_SHEET);
            this.id = rs.getInt(Record.CLMN_ID);
            this.title = rs.getString(Record.CLMN_TITLE);
            this.description = rs.getString(Record.CLMN_DESCRIPTION);
            this.amount = rs.getDouble(Record.CLMN_AMOUNT);
            this.recordDate = rs.getDate(Record.CLMN_RECORD_DATE);
            this.catId = rs.getInt(Record.CLMN_CAT_ID);
            this.catName = rs.getString(Record.CLMN_CAT_NAME);
            this.catColour = rs.getString(Record.CLMN_CAT_COLOUR);
        } catch (SQLException sqle) {
            String msg = "An error occured while reading from a result set.";
            throw new DBException(msg, sqle, 2);
        }
    }

    //Selection
    /**
     * Execute an SQL query that is provided as a string.
     *
     * @param SQL a string containing a valid SQL statement.
     * @return A list of Records that may also be empty.
     */
    private static ArrayList<Record> executeSelection(String SQL) {
        ArrayList<Record> records = new ArrayList<>();
        Connection con;

        try {
            con = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = con.prepareStatement(SQL)) {
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    records.add(new Record(rs));
                }

                rs.close();
            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
        }

        return records;
    }

    /**
     * Find all Records.
     *
     * @return a list of all Records encapsulated in active record objects.
     */
    public static ArrayList<Record> findAll() {
        return Record.executeSelection(Record.SELECT_ALL);
    }

    /**
     * Find all Records and order the results.
     *
     * @param orderBy an array of column names that will be used for sorting.
     * @return a list of all Records encapsulated in active record objects.
     */
    public static ArrayList<Record> findAll(final String[] orderBy) {
        return Record.executeSelection(Record.SELECT_ALL + ActiveRecord.buildOrderBy(orderBy));
    }

    /**
     * Find all Records and filter the results.
     *
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all Records encapsulated in active record objects.
     */
    public static ArrayList<Record> findAll(DBFilter filter) {
        try {
            return Record.executeSelection(Record.SELECT_ALL + filter.buildWhereClause());
        } catch (DBException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Find all Records and order and filter the results.
     *
     * @param orderBy an array of colunn names that will be used for sorting.
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all Records encapsulated in active record objects.
     */
    public static ArrayList<Record> findAll(final String[] orderBy, DBFilter filter) {
        try {
            return Record.executeSelection(Record.SELECT_ALL + filter.buildWhereClause() + ActiveRecord.buildOrderBy(orderBy));
        } catch (DBException ex) {
            Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Modification 
    /**
     * Insert the <code>Record</code> specified in this active record into the
     * database.
     *
     * @return Returns true if the insertion was successfull and false
     * otherwise.
     */
    @Override
    public boolean insert() {
        //Since the id of an record cannot be auto incremented by the database
        //new ids have to be calculated by the insert() method.
        if (this.balanceSheet != 0) {
            //Load existing records
            ArrayList<Record> existingRecords = 
                    Record.executeSelection(
                            "SELECT ID"
                            + " FROM " + Record.SELECT_TABLE
                            + " WHERE " + Record.CLMN_BALANCE_SHEET + " = " + this.balanceSheet
                            + " ORDER BY " + Record.CLMN_ID + " DESC"
                    );
            
            if (existingRecords.isEmpty()) {
                this.id = 1;
            } else {
                this.id = existingRecords.get(0).getId() + 1;
            }

            Connection con;
            String sql = "INSERT INTO " + Record.MODIFY_TABLE
                    + "(" + Record.CLMN_BALANCE_SHEET
                    + ", " + Record.CLMN_ID
                    + ", " + Record.CLMN_TITLE
                    + ", " + Record.CLMN_DESCRIPTION
                    + ", " + Record.CLMN_AMOUNT
                    + ", " + Record.CLMN_CATEGORY + ")"
                    + " VALUES "
                    + "(?, ?, ?, ?, ?, ?)";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                    stmt.setInt(1, this.balanceSheet);
                    stmt.setInt(2, this.id);
                    stmt.setString(3, this.title);
                    stmt.setString(4, this.description);
                    stmt.setDouble(5, this.amount);
                    stmt.setInt(6, this.catId);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(Record.class.getName()).log(Level.WARNING, "Failed to insert new Record in DB!");
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
                    Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Update the <code>Record</code> specified in this active record in the
     * database.
     *
     * @return Returns true if the update was successfull and false otherwise.
     */
    @Override
    public boolean update() {
        if (this.balanceSheet != 0
                && this.id != 0
                && this.title != null
                && this.description != null
                && this.amount >= 0.0
                && this.recordDate != null
                && this.catId != 0) {
            Connection con;
            String sql = "UPDATE " + Record.MODIFY_TABLE
                    + " SET " + Record.CLMN_TITLE + " = ? ,"
                    + " " + Record.CLMN_DESCRIPTION + " = ? ,"
                    + " " + Record.CLMN_AMOUNT + " = ? ,"
                    + " " + Record.CLMN_RECORD_DATE + " = ? ,"
                    + " " + Record.CLMN_CATEGORY + " = ? "
                    + " WHERE " + Record.CLMN_BALANCE_SHEET + " = ?"
                    + " AND " + Record.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    //Set parameters
                    stmt.setString(1, this.title);
                    stmt.setString(2, this.description);
                    stmt.setDouble(3, this.amount);
                    stmt.setDate(4, this.recordDate);
                    stmt.setInt(5, this.catId);
                    stmt.setInt(6, this.balanceSheet);
                    stmt.setInt(7, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(BalanceSheet.class.getName()).log(Level.WARNING, "Failed to update Record in DB!");
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
     * Removes the <code>Record</code> from the database.
     *
     * @return Returns true if the deletion was successfull and false otherwise.
     */
    @Override
    public boolean delete() {
        if (this.balanceSheet != 0
                && this.id != 0) {
            Connection con;
            String sql = "DELETE FROM " + Record.MODIFY_TABLE
                    + " WHERE " + Record.CLMN_BALANCE_SHEET + " = ?"
                    + " AND " + Record.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setInt(1, this.balanceSheet);
                    stmt.setInt(2, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(Record.class.getName()).log(Level.WARNING, "Failed to delete Record from DB!");
                    }
                } catch (SQLException sqle) {
                    //Statement failed
                    String msg = "Failed to prepare the SQL statement.";
                    Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(Record.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }

    //Getter & Setter
    /**
     * Get the id of the balance sheet, which contains the record.
     * @return the balance sheet's id.
     */
    public int getBalanceSheet() {
        return balanceSheet;
    }

    /**
     * Set the id of the balance sheet, which contains the record.
     * Use only for initilisation purposes. The balance sheet id will not be updated.
     * @param balanceSheet the balance sheet's id.
     */
    public void setBalanceSheet(int balanceSheet) {
        this.balanceSheet = balanceSheet;
    }

    /**
     * Get the record's id.
     * @return the record's id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Get the record's title.
     * @return the record's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the record's title.
     * @param title the record's title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the record's description.
     * @return the record's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the record's description.
     * @param description the record's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the record's amount.
     * @return the record's amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the record's amount.
     * @param amount the record's amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Get the record's date.
     * @return the record's date.
     */
    public Date getRecordDate() {
        return recordDate;
    }

    /**
     * Set the record's date.
     * @param recordDate the record's date.
     */
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    /**
     * Set the record's category. The name and the colour will be derived from the category.
     * Thus there are no seperate setter methods for them.
     * @param category a category active record.
     */
    public void setCategory(Category category) {
        this.catId = category.getId();
        this.catName = category.getName();
        this.catColour = category.getColour();
    }
    
    /**
     * Get the id of the record's category.
     * @return the id of the record's category.
     */
    public int getCatId() {
        return catId;
    }

    /**
     * Get the name of the record's category.
     * @return the name of the record's category.
     */
    public String getCatName() {
        return catName;
    }

    /**
     * Get the colour of the record's category.
     * @return the colour of the record's category.
     */
    public String getCatColour() {
        return catColour;
    }
    
}
