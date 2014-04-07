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

import java.sql.Date;
import java.sql.Timestamp;
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
    
    //SQL code
    /**
     * SQL query for selecting all users from the USERS table.
     */
    private static final String SELECT_ALL = 
            "SELECT * " + 
            "FROM " + User.SELECT_TABLE;
    
    /**
     * SQL where clause to find a user by his e-mail address.
     */
    private static final String BY_MAIL = 
            " WHERE mail=?";
    
    /**
     * SQL where clause to find a user by his id.
     */
    private static final String BY_ID = 
            " WHERE id=?";
    
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
    private String password;
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
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's password.
     * @param password A hash of the user's password.
     */
    public void setPassword(String password) {
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
