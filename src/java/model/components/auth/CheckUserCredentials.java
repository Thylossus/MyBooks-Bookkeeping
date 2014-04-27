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
import database.DBFilter;
import database.SQLConstraintOperator;
import database.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;

/**
 * Check credentials that were entered by the user.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class CheckUserCredentials extends ModelComponent{
    
    /**
     * The entered e-mail address.
     */
    private String mail;
    /**
     * The SHA-256 hash value of the inserted password.
     */
    private byte[] password;
    
    
    /**
     * Construct a CheckUserCredentials object
     * @param request The request's request object
     * @param response The request's response object
     */
    public CheckUserCredentials(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.mail = "";
        this.password = null;
    }

    /**
     * Process the action for CheckUserCredentials
     */
    @Override
    public void process() {
        if (this.mail.isEmpty() || this.password == null) {
            ScopeHandler.getInstance().store(this.request, "inputValidation", false);
        } else {
            //Get all users with the given e-mail address (size of list is 1 if successful)
            DBFilter mailFilter = new DBFilter();
            mailFilter.addConstraint(User.CLMN_MAIL, SQLConstraintOperator.LIKE, this.mail);
            ArrayList<User> users = User.findAll(mailFilter);
            
            //We expect to find exactly one user. If we find more or less than one user, the verification failed.
            if (users != null && users.size() == 1) {
                User user = users.get(0);
                //Check password:
                if (Arrays.equals(user.getPassword(), this.password)) {
                    ScopeHandler.getInstance().store(this.request, "inputValidation", true);
                    ScopeHandler.getInstance().store(this.request, "user", user);
                } else {
                    ScopeHandler.getInstance().store(this.request, "inputValidation", false);
                }
            } else {
                ScopeHandler.getInstance().store(this.request, "inputValidation", false);
            }
        }
    }

    /**
     * Provide parameters to the model component.
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if (params.get("mail") != null) {
            this.mail = (String)params.get("mail");
        }
        
        if (params.get("password") != null) {
            this.password = (byte[])params.get("password");
        }
        
        return this;
    }
    
    /**
     * Validate a signed in user.
     * @return true if the user is logged in and false if not.
     */
    public boolean validateLogIn() {
        //Load user from session. If the user is not in the session, no user is logged in.
        User user = (User)ScopeHandler.getInstance().load(this.request, "user", "session");
        
        if (user == null) {
            return false;
        }        
        
        this.mail = user.getMail();
        this.password = user.getPassword();
        
        //Check user credentials
        this.process();
        
        return (boolean) ScopeHandler.getInstance().load(this.request, "inputValidation");
    }

}
