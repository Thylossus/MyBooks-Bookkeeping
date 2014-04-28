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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Active record for categories.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Category extends ActiveRecord implements DBDeletable, DBInsertable, DBUpdatable{

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "CATEGORIES";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "CATEGORIES";
    
    //Columns
    public static final String CLMN_ID = "ID";
    public static final String CLMN_NAME = "NAME";
    public static final String CLMN_DESCRIPTION = "DESCRIPTION";
    public static final String CLMN_COLOUR = "COLOUR";
    public static final String CLMN_SYSTEM_CATEGORY = "SYSTEM_CATEGORY";
    public static final String CLMN_CREATOR = "CREATOR";

    //SQL code
    /**
     * SQL query for selecting all Categorys from the Categorys table.
     */
    private static final String SELECT_ALL = 
            "SELECT * " + 
            "FROM " + Category.SELECT_TABLE;
    
    //Attributes
    /**
     * The category's id.
     */
    private int id;
    /**
     * The category's name.
     */
    private String name;
    /**
     * The category's description.
     */
    private String description;
    /**
     * The category's colour (hex code).
     */
    private String colour;
    /**
     * True if the category is a system category and false otherwise.
     */
    private boolean systemCategory;
    /**
     * The id of the category's creator.
     */
    private int creator;
    
    //Construction
    /**
     * Empty constructor for creating non-initialised Categorys.
     */
    public Category(){
        this.id = 0;
        this.name = null;
        this.description = null;
        this.colour = null;
        this.systemCategory = false;
        this.creator = 0;
    }
    /**
     * Construct a Category from a result set.
     * @param rs a result set containing data about a Category.
     * @throws DBException if reading from the result set failed.
     */
    public Category(ResultSet rs) throws DBException{
        try {
            this.id = rs.getInt(Category.CLMN_ID);
            this.name = rs.getString(Category.CLMN_NAME);
            this.description = rs.getString(Category.CLMN_DESCRIPTION);
            this.colour = rs.getString(Category.CLMN_COLOUR);
            this.systemCategory = rs.getBoolean(Category.CLMN_SYSTEM_CATEGORY);
            this.creator = rs.getInt(Category.CLMN_CREATOR);
        } catch (SQLException sqle) {
            String msg = "An error occured while reading from a result set.";
            throw new DBException(msg, sqle, 2);
        }
    }
    
    //Selection
    /**
     * Execute an SQL query that is provided as a string.
     * @param SQL a string containing a valid SQL statement.
     * @return A list of Categorys that may also be empty.
     */
    private static ArrayList<Category> executeSelection(String SQL) {
        ArrayList<Category> categories = new ArrayList<>();
        Connection con;
        
        try {
            con = DBConnection.getInstance().getConnection();
            try(PreparedStatement stmt = con.prepareStatement(SQL)) {
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()) {
                    categories.add(new Category(rs));
                }
                
                rs.close();
            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return categories;
    }

    /**
     * Find all Categorys.
     *
     * @return a list of all Categorys encapsulated in active record objects.
     */
    public static ArrayList<Category> findAll() {
        return Category.executeSelection(Category.SELECT_ALL);
    }

    /**
     * Find all Categorys and order the results.
     *
     * @param orderBy an array of column names that will be used for sorting.
     * @return a list of all Categorys encapsulated in active record objects.
     */
    public static ArrayList<Category> findAll(final String[] orderBy) {
        return Category.executeSelection(Category.SELECT_ALL + ActiveRecord.buildOrderBy(orderBy));
    }

    /**
     * Find all Categorys and filter the results.
     *
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all Categorys encapsulated in active record objects.
     */
    public static ArrayList<Category> findAll(DBFilter filter) {
        try {
            return Category.executeSelection(Category.SELECT_ALL + filter.buildWhereClause());
        } catch (DBException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Find all Categorys and order and filter the results.
     *
     * @param orderBy an array of colunn names that will be used for sorting.
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all Categorys encapsulated in active record objects.
     */
    public static ArrayList<Category> findAll(final String[] orderBy, DBFilter filter) {
        try {
            return Category.executeSelection(Category.SELECT_ALL + filter.buildWhereClause() + ActiveRecord.buildOrderBy(orderBy));
        } catch (DBException ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Modification 
    /**
     * Insert the <code>Category</code> specified in this active record into the database.
     * @return Returns true if the insertion was successfull and false otherwise.
     */
    @Override
    public boolean insert() {
        Connection con;
        String sql = "INSERT INTO " + Category.MODIFY_TABLE
                + "(" + Category.CLMN_NAME 
                + ", " + Category.CLMN_DESCRIPTION
                + ", " + Category.CLMN_COLOUR
                + ", " + Category.CLMN_CREATOR
                + ")"
                + " VALUES "
                + "(?, ?, ?, ?)";

        try {
            con = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, this.name);
                stmt.setString(2, this.description);
                stmt.setString(3, this.colour);
                stmt.setInt(4, this.creator);

                if (stmt.executeUpdate() != 1) {
                    Logger.getLogger(Category.class.getName()).log(Level.WARNING, "Failed to insert new Category in DB!");
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
                Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
    
    /**
     * Update the <code>Category</code> specified in this active record in the database.
     * @return Returns true if the update was successfull and false otherwise.
     */
    @Override
    public boolean update() {
        if (this.id != 0
                && this.name != null
                && this.description != null
                && this.colour != null) {
            Connection con;
            String sql = "UPDATE " + Category.MODIFY_TABLE
                    + " SET " + Category.CLMN_NAME + " = ? ,"
                    + " " + Category.CLMN_DESCRIPTION + " = ? ,"
                    + " " + Category.CLMN_COLOUR + " = ? "
                    + " WHERE " + BalanceSheet.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    //Set parameters
                    stmt.setString(1, this.name);
                    stmt.setString(2, this.description);
                    stmt.setString(3, this.colour);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(BalanceSheet.class.getName()).log(Level.WARNING, "Failed to update Category in DB!");
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
     * Removes the <code>Category</code> from the database.
     * @return Returns true if the deletion was successfull and false otherwise.
     */
    @Override
    public boolean delete() {
        if (this.id != 0) {
            Connection con;
            String sql = "DELETE FROM " + Category.MODIFY_TABLE
                    + " WHERE " + Category.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setInt(1, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(Category.class.getName()).log(Level.WARNING, "Failed to delete Category from DB!");
                    }
                } catch (SQLException sqle) {
                    //Statement failed
                    String msg = "Failed to prepare the SQL statement.";
                    Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }
    
    //Getter & Setter
    /**
     * Get the category's id.
     * @return the category's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the category's name.
     * @return the category's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the category's name.
     * @param name the category's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the category's description.
     * @return the category's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the category's description.
     * @param description the category's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the category's colour.
     * @return the category's colour as a hex code.
     */
    public String getColour() {
        return colour;
    }

    /**
     * Set the category's colour.
     * @param colour a six character hex code representing the colour.
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * Check whether the category is a system category or not.
     * @return true if the category is a system category, false otherwise.
     */
    public boolean isSystemCategory() {
        return systemCategory;
    }

    /**
     * Get the id of the category's creator.
     * @return the id of the category's creator.
     */
    public int getCreator() {
        return creator;
    }

    /**
     * Set the id of the category's creator. Use only for initilisation 
     * purposes because the creator id will be ignored by <code>update()</code>.
     * @param creator the id of the category's creator.
     */
    public void setCreator(int creator) {
        this.creator = creator;
    }
    
    
}

