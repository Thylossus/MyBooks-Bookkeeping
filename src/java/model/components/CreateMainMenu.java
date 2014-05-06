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
import controller.URLProber;
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
    private final User activeUser;

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
            this.activeUser = (User) ScopeHandler.getInstance().load(this.request, "user", "session");
        } else {
            this.activeUser = null;
        }
    }

    /**
     * Process the action for CreateMainMenu
     */
    @Override
    public void process() {
        URLProber up = (URLProber) ScopeHandler.getInstance().load(this.request, "url");
        String baseURL = "http://" + this.request.getServerName() + ":" + this.request.getServerPort() + this.request.getContextPath();
        MainMenu mainMenu = new MainMenu("MyBooks", baseURL);
        mainMenu.setAuthenticationMenu(this.activeUser);
        mainMenu.addItem("Home");
        mainMenu.addItem("Download");
        mainMenu.addItem("About");
        mainMenu.addItem("Help");

        if (this.activeUser != null) {
            Menu bsmSubmenu = new Menu();

            bsmSubmenu.addItem("Management", mainMenu.getBaseURL() + "/bsm/balancesheets");
            bsmSubmenu.addItem("Create balance sheet", mainMenu.getBaseURL() + "/bsm/createbalancesheet#titleInput");
            bsmSubmenu.addItem("Open in editor", mainMenu.getBaseURL() + "/bsm/openbalancesheeteditor");
            bsmSubmenu.addItem("View", mainMenu.getBaseURL() + "/bsm/viewbalancesheet");
            bsmSubmenu.addItem("Basic reports", mainMenu.getBaseURL() + "/bsm/basicreports");

            if (this.activeUser.getUserType().getId() >= UserType.PREMIUM_USER.getId()) {
                bsmSubmenu.addItem("Extended reports", mainMenu.getBaseURL() + "/bsm/extendedreports");
            }
            
            bsmSubmenu.addItem("Delete", mainMenu.getBaseURL() + "/bsm/deletebalancesheet");

            //Disable menu items if no balance sheet is in the session
            if (ScopeHandler.getInstance().load(this.request, "balanceSheet", "session") == null) {
                bsmSubmenu.getMenuItems().get(2).setDisabled();
                bsmSubmenu.getMenuItems().get(3).setDisabled();
                bsmSubmenu.getMenuItems().get(4).setDisabled();
                bsmSubmenu.getMenuItems().get(5).setDisabled();
                if (this.activeUser.getUserType().getId() >= UserType.PREMIUM_USER.getId()) {
                    bsmSubmenu.getMenuItems().get(6).setDisabled();
                }
            }

            

            mainMenu.addItem("Balance Sheets", bsmSubmenu.getMenuItems());
            if (this.activeUser.getUserType().getId() >= UserType.NEWS_WRITER.getId()) {
                Menu articleSubmenu = new Menu();
                articleSubmenu.addItem("Article Management", mainMenu.getBaseURL() + "/blog/articles");
                articleSubmenu.addItem("Write Article", mainMenu.getBaseURL() + "/blog/writearticle");

                mainMenu.addItem("Articles", articleSubmenu.getMenuItems());
                if (this.activeUser.getUserType() == UserType.ADMINISTRATOR) {
                    Menu sysmgmtSubmenu = new Menu();
                    sysmgmtSubmenu.addItem("Delete Users", mainMenu.getBaseURL() + "/sysmgmt/deleteusers");

                    mainMenu.addItem("System Management", sysmgmtSubmenu.getMenuItems());
                }
            }
        }

        if (up != null) {
            //Not sufficient!
            mainMenu.setActiveItem(up.getCommand());
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
