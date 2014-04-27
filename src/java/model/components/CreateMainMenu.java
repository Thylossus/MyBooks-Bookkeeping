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

import beans.MainMenuItem;
import controller.ScopeHandler;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;

/**
 * _DESCRIPTION_
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class CreateMainMenu extends ModelComponent {

    //Constants for specifying how the main menu will be constructed.
    /**
     * Build the default main menu using only standard menu items.
     */
    public static final String CREATION_MODE_DEFAULT = "default";
    /**
     * Build a custom main menu using only user specified MenuItems.
     */
    public static final String CREATION_MODE_CUSTOM = "custom";
    /**
     * Build a main menu using the standard menu items and additional user
     * defined items.
     */
    public static final String CREATION_MODE_EXTENSION = "extension";

    /**
     * Mode of construction of the main menu. Use the class' constants for this
     * field.
     */
    private String constructionMode;

    /**
     * A list of main menu items specified by the calling application and
     * provided by the <code>provideParameters</code> method.
     */
    private ArrayList<MainMenuItem> customMenuItems;

    /**
     * Construct a CreateMainMenu object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public CreateMainMenu(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.constructionMode = CreateMainMenu.CREATION_MODE_DEFAULT;
        this.customMenuItems = null;
    }

    /**
     * Process the action for CreateMainMenu
     */
    @Override
    public void process() {
        beans.MainMenu mainMenu = null;

        switch (this.constructionMode) {
            case CreateMainMenu.CREATION_MODE_DEFAULT:
                mainMenu = this.constructDefaultMenu();
                break;
            case CreateMainMenu.CREATION_MODE_CUSTOM:
                break;
            case CreateMainMenu.CREATION_MODE_EXTENSION:
                //Not sure if required...
                break;
            default:
                mainMenu = this.constructDefaultMenu();
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
        if (params.get("constructionMode") != null) {
            this.constructionMode = (String) params.get("constructionMode");
        }

        if (params.get("customMenuItems") != null) {
            this.customMenuItems = (ArrayList<MainMenuItem>) params.get("customMenuItems");
        }

        return this;
    }

    private beans.MainMenu constructDefaultMenu() {
        beans.MainMenu mainMenu = new beans.MainMenu();
        mainMenu.setActiveItem(mainMenu.addItem("Home"));
        mainMenu.addItem("Download");
        mainMenu.addItem("About");
        mainMenu.addItem("Help");
        return mainMenu;
    }

}
