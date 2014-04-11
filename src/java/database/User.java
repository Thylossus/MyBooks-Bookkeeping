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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.types.UserType;

/**
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class User extends ActiveRecord implements DBDeletable, DBInsertable, DBUpdatable{

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "USERS";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "USERS";
    
    //Columns
    public static final String CLMN_ID = "ID";
    public static final String CLMN_MAIL = "MAIL";
    public static final String CLMN_PASSWORD = "PASSWORD";
    public static final String CLMN_LASTNAME = "LASTNAME";
    public static final String CLMN_FIRSTNAME = "FIRSTNAME";
    public static final String CLMN_SIGN_UP_DATE = "SIGN_UP_DATE";
    public static final String CLMN_LAST_SIGN_IN_DATE = "LAST_SIGN_IN_DATE";
    public static final String CLMN_USER_TYPE = "USER_TYPE";
    
    //SQL code
    /**
     * SQL query for selecting all users from the USERS table.
     */
    private static final String SELECT_ALL = 
            "SELECT * " + 
            "FROM " + User.SELECT_TABLE;
    
    /**
     * User ID (read-only)
     */
    private int id;
    /**
     * The user's e-mail address
     */
    private String mail;
    /**
     * A hash of the user's password
     */
    private byte[] password;
    /**
     * The user's surname
     */
    private String lastname;
    /**
     * The user's forename
     */
    private String firstname;
    /**
     * The date of sign up (read-only)
     */
    private Date signUpDate;
    /**
     * A timestamp of the last login
     */
    private Timestamp lastSignInDate;
    /**
     * The user's type
     */
    private UserType userType;

    //Construction
    /**
     * Empty constructor for creating non-initialised Users.
     */
    public User(){}
    /**
     * Construct a User from a result set.
     * @param rs a result set containing data about a User.
     * @throws DBException if reading from the result set failed.
     */
    public User(ResultSet rs) throws DBException{
        try {
            this.id = rs.getInt(User.CLMN_ID);
            this.mail = rs.getString(User.CLMN_MAIL);
            this.password = rs.getBytes(User.CLMN_PASSWORD);
            this.firstname = rs.getString(User.CLMN_LASTNAME);
            this.lastname = rs.getString(User.CLMN_FIRSTNAME);
            this.signUpDate = rs.getDate(User.CLMN_SIGN_UP_DATE);
            this.lastSignInDate = rs.getTimestamp(User.CLMN_LAST_SIGN_IN_DATE);
            this.userType = UserType.getUserTypeById(rs.getInt(User.CLMN_USER_TYPE));
        } catch (SQLException sqle) {
            String msg = "An error occured while reading from a result set.";
            throw new DBException(msg, sqle, 3);
        }
    }
    
    //Selection
    /**
     * Execute an SQL query that is provided as a string.
     * @param SQL a string containing a valid SQL statement.
     * @return A list of Users that may also be empty.
     */
    private static ArrayList<User> executeSelection(String SQL) {
        ArrayList<User> users = new ArrayList<>();
        Connection con;
        
        try {
            con = DBConnection.getInstance().getConnection();
            try(PreparedStatement stmt = con.prepareStatement(SQL); ResultSet rs = stmt.executeQuery()) {
                
                while(rs.next()) {
                    users.add(new User(rs));
                }
            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return users;
    }
    
    /**
     * Find all users of the system.
     * @return a list of all system users encapsulated in active record objects.
     */
    public static ArrayList<User> findAll() {
        return User.executeSelection(User.SELECT_ALL);
    }
    
    /**
     * Find all users of the system and order the results.
     * @param orderBy an array of column names that will be used for sorting.
     * @return a list of all system users encapsulated in active record objects.
     */
    public static ArrayList<User> findAll(final String[] orderBy) {
        return User.executeSelection(User.SELECT_ALL + ActiveRecord.buildOrderBy(orderBy));
    }
    
    /**
     * Find all users of the system and filter the results.
     * @param filter a <code>DBFilter</code> object that contains the constraints for the query's where clause.
     * @return a list of all system users encapsulated in active record objects.
     */
    public static ArrayList<User> findAll(DBFilter filter) {
        return User.executeSelection(User.SELECT_ALL + filter.buildWhereClause());
    }
    
    /**
     * Find all users of the system and order and filter the results.
     * @param orderBy an array of colunn names that will be used for sorting.
     * @param filter a <code>DBFilter</code> object that contains the constraints for the query's where clause.
     * @return a list of all system users encapsulated in active record objects.
     */
    public static ArrayList<User> findAll(final String[] orderBy, DBFilter filter) {
        return User.executeSelection(User.SELECT_ALL + filter.buildWhereClause() + ActiveRecord.buildOrderBy(orderBy));
    }
    
    //Modification 
    /**
     * Insert the user specified in this active record into the database.
     * @return Returns true if the insertion was successfull and false otherwise.
     */
    @Override
    public boolean insert() {
        return false;
    }
    
    /**
     * Update the user specified in this active record in the database.
     * @return Returns true if the update was successfull and false otherwise.
     */
    @Override
    public boolean update() {
        return false;
    }
    
    /**
     * Removes the user from the database.
     * @return Returns true if the deletion was successfull and false otherwise.
     */
    @Override
    public boolean delete() {
        return false;
    }
    
    //Getter & Setter
    /**
     * Get the user ID.
     * @return The user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Get the user's e-mail address.
     * @return The user's e-mail address
     */
    public String getMail() {
        return mail;
    }

    /**
     * Set the user's e-mail address.
     * @param mail An e-mail address
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Get the hash of the user's password.
     * @return The hash of the user's password
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * Set the user's password.
     * @param password A hash of the user's password.
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }

    /**
     * Get the user's surname.
     * @return The user's surname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set the user's surname
     * @param lastname The user's surname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Get the user's forename.
     * @return The user's forname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set the user's forname.
     * @param firstname The user's forname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get the user's sign up date.
     * @return The user's sign up date
     */
    public Date getSignUpDate() {
        return signUpDate;
    }

    /**
     * Get the timestamp of the user's last login.
     * @return The timestamp of the user's last login
     */
    public Timestamp getLastSignInDate() {
        return lastSignInDate;
    }

    /**
     * Set the timestamp of the user's last login.
     * @param lastSignInDate The timestamp of the user's last login
     */
    public void setLastSignInDate(Timestamp lastSignInDate) {
        this.lastSignInDate = lastSignInDate;
    }

    /**
     * Get the user's type.
     * @return The user's type
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Set the user's type.
     * @param userType The user's type
     */
    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
}
