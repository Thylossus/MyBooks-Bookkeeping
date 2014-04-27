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
package beans;

import database.User;

/**
 * Java Bean for authentication menus, which provide either a sign in form or
 * details of the current user and a sign out button.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class AuthenticationMenu {

    /**
     *
     */
    private User activeUser;

    private String baseURL;

    public AuthenticationMenu(String baseURL) {
        this.activeUser = null;
        this.baseURL = baseURL;
    }

    public void setActiveUser(User user) {
        this.activeUser = user;
    }

    /**
     * Transform the authentication menu into a html representation.
     *
     * @return html representation of the authentication menu.
     */
    @Override
    public String toString() {
        MenuItem[] menuItems = new MenuItem[2];
        if (this.activeUser != null) {
            MenuItem user = new MenuItem();
            user.setLabel(this.activeUser.getFirstname() + " " + this.activeUser.getLastname() + " (" + this.activeUser.getLastSignInDate().toString() + ")");
            user.setLink("#");
            user.setGlyphicon("glyphicon-user");
            MenuItem signout = new MenuItem();
            signout.setLabel("Sign out");
            signout.setLink(baseURL + "/auth/signout");
            signout.setGlyphicon("glyphicon-off");
            
            menuItems[0] = user;
            menuItems[1] = signout;
        } else {
            MenuItem signup = new MenuItem();
            signup.setLabel("Sign up");
            signup.setLink(this.baseURL + "/auth/signup");
            MenuItem signin = new MenuItem();
            signin.setLabel("Sign in");
            signin.setLink(this.baseURL + "/auth/signin");

            menuItems[0] = signup;
            menuItems[1] = signin;
        }
        
        return "<ul class=\"nav navbar-nav pull-right\">\n"
                    + menuItems[0].toString()
                    + menuItems[1].toString()
                    + "</ul>";
    }

}
