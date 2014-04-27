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
package model.components;

import beans.MainMenu;
import beans.Menu;
import controller.ScopeHandler;
import database.User;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.components.auth.CheckUserCredentials;
import model.types.UserType;

/**
 * Model component for creating main menus depending on the active user type.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class CreateMainMenu extends ModelComponent {

    /**
     * The active user. If no user is logged in, this field is null.
     */
    private User activeUser;

    /**
     * Construct a CreateMainMenu object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public CreateMainMenu(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        CheckUserCredentials cuc = new CheckUserCredentials(this.request, this.response);
        if (cuc.validateLogIn()) {
            this.activeUser = (User)ScopeHandler.getInstance().load(this.request, "user", "session");
        } else {
            this.activeUser = null;
        }
    }

    /**
     * Process the action for CreateMainMenu
     */
    @Override
    public void process() {
        String baseURL = "http://" + this.request.getServerName() + ":" + this.request.getServerPort() + this.request.getContextPath();
        MainMenu mainMenu = new MainMenu("MyBooks", baseURL);
        mainMenu.setAuthenticationMenu(this.activeUser);
        mainMenu.addItem("Home").setActive();
        mainMenu.addItem("Download");
        mainMenu.addItem("About");
        mainMenu.addItem("Help");

        if (this.activeUser != null) {
            Menu bsmSubmenu = new Menu();
            
            bsmSubmenu.addItem("Management", mainMenu.getBaseURL() + "/bsm/balancesheets");
            bsmSubmenu.addItem("Create Balance Sheet", mainMenu.getBaseURL() + "/bsm/createbalancesheet");
            bsmSubmenu.addItem("Open Editor", mainMenu.getBaseURL() + "/bsm/openbalancesheeteditor");
            
            mainMenu.addItem("Balance Sheets", bsmSubmenu.getMenuItems());
            if (this.activeUser.getUserType().getId() >= UserType.NEWS_WRITER.getId()) {
                Menu articleSubmenu = new Menu();
                
                mainMenu.addItem("Articles", articleSubmenu.getMenuItems());
                if (this.activeUser.getUserType() == UserType.ADMINISTRATOR) {
                    Menu sysmgmtSubmenu = new Menu();
                    
                    mainMenu.addItem("System Management", sysmgmtSubmenu.getMenuItems());
                }
            }
        }

        ScopeHandler.getInstance().store(request, "mainMenu", mainMenu);
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
        return this;
    }
}
