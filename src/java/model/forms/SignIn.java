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
import model.components.Input;

/**
 * Context for the sign in form.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class SignIn extends Context{

    /**
     * User's mail.
     */
    private String mail;
    /**
     * User's password.
     */
    private byte[] password;
    
    /**
     * Construct the sign in form context.
     * @param inputList 
     */
    public SignIn(ArrayList<Input> inputList) {
        this.inputList = inputList;
        
        this.mail = null;
        this.password = null;
    }
    
    /**
     * Validate the given inputs for the sign in form.
     * @return 
     */
    @Override
    public boolean validate() {
        //Check required parameters
        //Check mail
        if (inputList.get(0).getKey().equals("mail")) {
            this.mail = (String)inputList.get(0).getValue();
        } else {
            this.mail = (String)this.searchInputValue("mail");
            if (this.mail == null) {
                return false;
            }
        }
        //Check password
        if (inputList.get(1).getKey().equals("password")) {
            this.password = (byte[])inputList.get(1).getValue();
        } else {
            this.password = (byte[])this.searchInputValue("password");
            if (this.password == null) {
                return false;
            }
        }
        
        //Check length of mail and password hash
        if (this.mail.toString().length() > 60) {
            return false;
        }
        if (this.password.length != 32) {
            return false;
        }
        
        return true;
    }
    
}
