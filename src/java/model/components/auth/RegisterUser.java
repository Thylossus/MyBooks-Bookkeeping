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

package model.components.auth;


import controller.ScopeHandler;
import database.User;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.types.UserType;

/**
 * Register a user in the system.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class RegisterUser extends ModelComponent{

    /**
     * User's forename.
     */
    private String firstname;
    /**
     * User's surname.
     */
    private String lastname;
    /**
     * User's e-mail address.
     */
    private String mail;
    /**
     * Hash value of user's password.
     */
    private byte[] password;
    
    /**
     * Construct a RegisterUser object
     * @param request The request's request object
     * @param response The request's response object
     */
    public RegisterUser(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for RegisterUser
     */
    @Override
    public void process() {
        if(this.firstname != null &&
                this.lastname != null &&
                this.mail != null &&
                this.password != null) {
            User newUser = new User();
            newUser.setFirstname(this.firstname);
            newUser.setLastname(this.lastname);
            newUser.setMail(this.mail);
            newUser.setPassword(this.password);
            newUser.setUserType(UserType.STANDARD_USER);
            
            if (newUser.insert()) {
                //Insertion successful
                Logger.getLogger(RegisterUser.class.getName()).log(Level.FINE, "Registering user was successful!");
                //Store last sign in date of the user (i.e. current timestamp)
                newUser.setLastSignInDate(new Timestamp(new GregorianCalendar().getTimeInMillis()));
                //Store user in session scope
                ScopeHandler.getInstance().store(this.request, "user", newUser, "session");
            } else {
                //Insertion unsuccessful
                Logger.getLogger(RegisterUser.class.getName()).log(Level.WARNING, "Registering user failed!");
            }
        } else {
            //Some input is not set and therefore the process cannot be started
        }
    }
    
    /**
     * Provide parameters to the model component.
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if(params.get("firstname") != null) {
            this.firstname = (String)params.get("firstname");
        }
        
        if(params.get("lastname") != null) {
            this.lastname = (String)params.get("lastname");
        }
        
        if(params.get("mail") != null) {
            this.mail = (String)params.get("mail");
        }
        
        if(params.get("password") != null) {
            this.password = (byte[])params.get("password");
        }
        
        return this;
    }

}
