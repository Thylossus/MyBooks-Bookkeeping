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
package model.components.user;

import controller.ScopeHandler;
import database.User;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.components.auth.RegisterUser;

/**
 * Update any attribute of the user. This a core component because it will be used for updating the user
 * after the purchase of a premium membership.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class UpdateUser extends ModelComponent {

    /**
     * User's forename.
     */
    private String firstname;
    /**
     * User's surname.
     */
    private String lastname;
    /**
     * Hash value of user's password.
     */
    private byte[] password;

    /**
     * Construct a UpdateUser object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public UpdateUser(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);

        this.firstname = null;
        this.lastname = null;
        this.password = null;
    }

    /**
     * Process the action for UpdateUser
     */
    @Override
    public void process() {
        User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");

        if (user != null) {

            if ((this.firstname != null && this.lastname != null) || this.password != null) {
                if (this.firstname != null && this.lastname != null) {
                    user.setFirstname(this.firstname);
                    user.setLastname(this.lastname);
                }
                
                if (this.password != null) {
                    user.setPassword(this.password);
                }

                if (user.update()) {
                    //Insertion successful
                    Logger.getLogger(RegisterUser.class.getName()).log(Level.FINE, "Updating user was successful!");
                } else {
                    //Insertion unsuccessful
                    Logger.getLogger(UpdateUser.class.getName()).log(Level.WARNING, "Updating user failed!");
                }
            } else {
                Logger.getLogger(UpdateUser.class.getName()).log(Level.WARNING, "Missing parameters for updating user!");
            }
        }
    }

    /**
     * Provide parameters to the model component.
     *
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for
     * concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if (params.get("firstname") != null) {
            this.firstname = (String) params.get("firstname");
        }

        if (params.get("lastname") != null) {
            this.lastname = (String) params.get("lastname");
        }

        if (params.get("password") != null) {
            this.password = (byte[]) params.get("password");
        }

        return this;
    }

}
