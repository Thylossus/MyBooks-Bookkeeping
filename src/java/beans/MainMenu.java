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
 * Java Bean for main menus.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class MainMenu extends Menu{
    /**
     * The authentication menu of the main menu.
     */
    private final AuthenticationMenu authenticationMenu;
    
    /**
     * The menu's title (read-only). This is the project's name.
     */
    private final String title;
    
    /**
     * The project's base URL (read-only).
     */
    private final String baseURL;

    /**
     * Construct an empty main menu.
     * @param title The main menu's title.
     * @param baseURL The project's base URL.
     */
    public MainMenu(String title, String baseURL) {
        super();
        
        this.title = title;
        this.baseURL = baseURL;
        this.authenticationMenu = new AuthenticationMenu(this.baseURL);
    }

    /**
     * Get the main menu's title.
     * @return the main menu's title.
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Get the project's base URL.
     * @return the project's base URL.
     */
    public String getBaseURL() {
        return this.baseURL;
    }
    
    /**
     * Set the user of the authentication menu. If the user is not set the sign in form will be displayed.
     * @param user the active user.
     */
    public void setAuthenticationMenu(User user) {
        this.authenticationMenu.setActiveUser(user);
    }
    
    /**
     * Get the main menu's authentication menu.
     * @return the main menu's authentication menu.
     */
    public AuthenticationMenu getAuthenticationMenu() {
        return this.authenticationMenu;
    }
}
