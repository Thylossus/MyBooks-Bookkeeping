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
 * Active record for premium membership
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class PremiumMembership extends ActiveRecord implements DBInsertable, DBUpdatable{

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "PREMIUM_MEMBERSHIPS";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "PREMIUM_MEMBERSHIPS";
    
    //Columns
    public static final String CLMN_ID = "ID";
    public static final String CLMN_START_DATE = "START_DATE";
    public static final String CLMN_END_DATE = "END_DATE";
    public static final String CLMN_ACTIVE = "ACTIVE";
    public static final String CLMN_USER_ID = "USER_ID";
    public static final String CLMN_PAYMENT_DETAILS = "PAYMENT_DETAILS";

    //SQL code
    /**
     * SQL query for selecting all PremiumMemberships from the PremiumMemberships table.
     */
    private static final String SELECT_ALL = 
            "SELECT * " + 
            "FROM " + PremiumMembership.SELECT_TABLE;
    
    //Attributes
    /**
     * The premium membership's id.
     */
    private int id;
    /**
     * The premium membership's start date.
     */
    private Date startDate;
    /**
     * The premium membership's end date.
     */
    private Date endDate;
    /**
     * The premium membership's active status.
     */
    private boolean active;
    /**
     * The id of the premium membership's user.
     */
    private int userId;
    /**
     * The id of the payment details for the premium membership.
     */
    private int paymentDetails;
    
    //Construction
    /**
     * Empty constructor for creating non-initialised PremiumMemberships.
     */
    public PremiumMembership(){}
    /**
     * Construct a PremiumMembership from a result set.
     * @param rs a result set containing data about a PremiumMembership.
     * @throws DBException if reading from the result set failed.
     */
    public PremiumMembership(ResultSet rs) throws DBException{
        try {
            this.id = rs.getInt(PremiumMembership.CLMN_ID);
            this.startDate = rs.getDate(PremiumMembership.CLMN_START_DATE);
            this.endDate = rs.getDate(PremiumMembership.CLMN_END_DATE);
            this.active = rs.getBoolean(PremiumMembership.CLMN_ACTIVE);
            this.userId = rs.getInt(PremiumMembership.CLMN_USER_ID);
            this.paymentDetails = rs.getInt(PremiumMembership.CLMN_PAYMENT_DETAILS);
        } catch (SQLException sqle) {
            String msg = "An error occured while reading from a result set.";
            throw new DBException(msg, sqle, 2);
        }
    }
    
    //Selection
    /**
     * Execute an SQL query that is provided as a string.
     * @param SQL a string containing a valid SQL statement.
     * @return A list of PremiumMemberships that may also be empty.
     */
    private static ArrayList<PremiumMembership> executeSelection(String SQL) {
        ArrayList<PremiumMembership> premiumMemberships = new ArrayList<>();
        Connection con;
        
        try {
            con = DBConnection.getInstance().getConnection();
            try(PreparedStatement stmt = con.prepareStatement(SQL)) {
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()) {
                    premiumMemberships.add(new PremiumMembership(rs));
                }
                
                rs.close();
            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(PremiumMembership.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(PremiumMembership.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return premiumMemberships;
    }

    /**
     * Find all PremiumMemberships.
     *
     * @return a list of all PremiumMemberships encapsulated in active record objects.
     */
    public static ArrayList<PremiumMembership> findAll() {
        return PremiumMembership.executeSelection(PremiumMembership.SELECT_ALL);
    }

    /**
     * Find all PremiumMemberships and order the results.
     *
     * @param orderBy an array of column names that will be used for sorting.
     * @return a list of all PremiumMemberships encapsulated in active record objects.
     */
    public static ArrayList<PremiumMembership> findAll(final String[] orderBy) {
        return PremiumMembership.executeSelection(PremiumMembership.SELECT_ALL + ActiveRecord.buildOrderBy(orderBy));
    }

    /**
     * Find all PremiumMemberships and filter the results.
     *
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all PremiumMemberships encapsulated in active record objects.
     */
    public static ArrayList<PremiumMembership> findAll(DBFilter filter) {
        try {
            return PremiumMembership.executeSelection(PremiumMembership.SELECT_ALL + filter.buildWhereClause());
        } catch (DBException ex) {
            Logger.getLogger(PremiumMembership.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Find all PremiumMemberships and order and filter the results.
     *
     * @param orderBy an array of colunn names that will be used for sorting.
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all PremiumMemberships encapsulated in active record objects.
     */
    public static ArrayList<PremiumMembership> findAll(final String[] orderBy, DBFilter filter) {
        try {
            return PremiumMembership.executeSelection(PremiumMembership.SELECT_ALL + filter.buildWhereClause() + ActiveRecord.buildOrderBy(orderBy));
        } catch (DBException ex) {
            Logger.getLogger(PremiumMembership.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Modification 
    /**
     * Insert the <code>PremiumMembership</code> specified in this active record into the database.
     * @return Returns true if the insertion was successfull and false otherwise.
     */
    @Override
    public boolean insert() {
        Connection con;
        String sql = "INSERT INTO " + PremiumMembership.MODIFY_TABLE
                + "(" + PremiumMembership.CLMN_START_DATE + ", " + PremiumMembership.CLMN_END_DATE + ", " + PremiumMembership.CLMN_USER_ID + ", " + PremiumMembership.CLMN_PAYMENT_DETAILS + ")"
                + " VALUES "
                + "(?, ?, ?, ?)";

        try {
            con = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setDate(1, this.startDate);
                stmt.setDate(2, this.endDate);
                stmt.setInt(3, this.userId);
                stmt.setInt(4, this.paymentDetails);

                if (stmt.executeUpdate() != 1) {
                    Logger.getLogger(PremiumMembership.class.getName()).log(Level.WARNING, "Failed to insert new PremiumMembership in DB!");
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
                Logger.getLogger(PremiumMembership.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(PremiumMembership.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
    
    /**
     * Update the <code>PremiumMembership</code> specified in this active record in the database.
     * @return Returns true if the update was successfull and false otherwise.
     */
    @Override
    public boolean update() {
        if (this.id != 0) {
            Connection con;
            String sql = "UPDATE " + PremiumMembership.MODIFY_TABLE
                    + " SET " + PremiumMembership.CLMN_ACTIVE + " = ?"
                    + " WHERE " + PremiumMembership.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    //Set parameters
                    stmt.setBoolean(1, this.active);
                    stmt.setInt(2, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(BalanceSheet.class.getName()).log(Level.WARNING, "Failed to update PremiumMembership in DB!");
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
     * Get the premium membership's id.
     * @return the premium membership's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the premium membership's start date.
     * @return the premium membership's start date.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set the premium membership's start date.
     * Use only initially. <code>update()</code> will ignore this field
     * @param startDate the premium membership's start date.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the premium membership's end date.
     * @return the premium membership's end date.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Set the premium membership's end date.
     * Use only initially. <code>update()</code> will ignore this field
     * @param endDate the premium membership's end date.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Get the premium membership's activity status.
     * @return true if the premium membership is active and false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the premium membership's activity status.
     * Use only initially. <code>update()</code> will ignore this field
     * @param active the premium membership's activity status.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get the id of the premium membership's user.
     * @return the id of the premium membership's user.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the id of the premium membership's user.
     * Use only initially. <code>update()</code> will ignore this field
     * @param userId the id of the premium membership's user.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Get the id of the payment details for the premium membership.
     * @return the id of the payment details for the premium membership.
     */
    public int getPaymentDetails() {
        return paymentDetails;
    }

    /**
     * Set the id of the payment details for the premium membership.
     * Use only initially. <code>update()</code> will ignore this field
     * @param paymentDetails the id of the payment details for the premium membership.
     */
    public void setPaymentDetails(int paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
    
    
}

