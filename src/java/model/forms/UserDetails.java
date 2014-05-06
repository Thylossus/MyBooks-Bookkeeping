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

package model.forms;

import java.util.ArrayList;
import java.util.Arrays;
import model.components.Input;

/**
 * Context for the sign up and change user details forms.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class UserDetails extends Context{

    /**
     * User's firstname.
     */
    private String firstname;
    /**
     * User's lastname.
     */
    private String lastname;
    /**
     * User's mail.
     */
    private String mail;
    /**
     * User's password.
     */
    private byte[] password;
    /**
     * User's re-entered password.
     */
    private byte[] rePassword;
    
    /**
     * Construct the user details context.
     * @param inputList 
     */
    public UserDetails(ArrayList<Input> inputList) {
        this.inputList = inputList;
        
        this.firstname = null;
        this.lastname = null;
        this.mail = null;
        this.password = null;
        this.rePassword = null;
    }
    
    /**
     * Validate the given inputs for the user details context.
     * @return True if the input is valid and false if it is invalid.
     */
    @Override
    public boolean validate() {
        //Check required parameters
        //Check firstname
        if (inputList.get(0).getKey().equals("firstname")) {
            this.firstname = (String)inputList.get(0).getValue();
        } else {
            this.firstname = (String)this.searchInputValue("firstname");
            if (this.firstname == null) {
                return false;
            }
        }
        //Check lastname
        if (inputList.get(1).getKey().equals("lastname")) {
            this.lastname = (String)inputList.get(1).getValue();
        } else {
            this.lastname = (String)this.searchInputValue("lastname");
            if (this.lastname == null) {
                return false;
            }
        }       
        //Check mail
        if (inputList.get(2).getKey().equals("mail")) {
            this.mail = (String)inputList.get(2).getValue();
        } else {
            this.mail = (String)this.searchInputValue("mail");
            if (this.mail == null) {
                return false;
            }
        }
        //Check password
        if (inputList.get(3).getKey().equals("password")) {
            this.password = (byte[])inputList.get(3).getValue();
        } else {
            this.password = (byte[])this.searchInputValue("password");
            if (this.password == null) {
                return false;
            }
        }
        //Check re-password
        if (inputList.get(4).getKey().equals("re-password")) {
            this.rePassword = (byte[])inputList.get(4).getValue();
        } else {
            this.rePassword = (byte[])this.searchInputValue("re-password");
            if (this.rePassword == null) {
                return false;
            }
        }
        
        //Check length of strings and password hash
        if (this.firstname.length() > 30) {
            return false;
        }
        if (this.lastname.length() > 30) {
            return false;
        }
        if (this.mail.toString().length() > 60) {
            return false;
        }
        if (this.password.length != 32 || this.rePassword.length != 32) {
            return false;
        }
        
        return Arrays.equals(this.password, this.rePassword);
    }
    
}
