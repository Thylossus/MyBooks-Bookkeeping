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
 * Context for the reset user password form.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class PasswordReset extends Context{

    
    /**
     * User's password.
     */
    private byte[] password;
    /**
     * User's re-entered password.
     */
    private byte[] rePassword;
    
    /**
     * Construct the password reset context.
     * @param inputList 
     */
    public PasswordReset(ArrayList<Input> inputList) {
        this.inputList = inputList;
        
        this.password = null;
        this.rePassword = null;
    }
    
    /**
     * Validate the given inputs for the password reset context.
     * @return 
     */
    @Override
    public boolean validate() {
        //Check required parameters
        
        //Check password
        if (inputList.get(0).getKey().equals("password")) {
            this.password = (byte[])inputList.get(0).getValue();
        } else {
            this.password = (byte[])this.searchInputValue("password");
            if (this.password == null) {
                return false;
            }
        }
        //Check re-password
        if (inputList.get(1).getKey().equals("re-password")) {
            this.rePassword = (byte[])inputList.get(1).getValue();
        } else {
            this.rePassword = (byte[])this.searchInputValue("re-password");
            if (this.rePassword == null) {
                return false;
            }
        }
        
        //Check length of password hashes
        if (this.password.length != 32 || this.rePassword.length != 32) {
            return false;
        }
        
        //Check if both passwords match
        if (!Arrays.equals(this.password, this.rePassword)) {
            return false;
        }
        
        return true;
    }
    
}
