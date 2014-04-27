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

import java.util.ArrayList;

/**
 * Java Bean for a main menu item.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class MainMenuItem {

    /**
     * The menu item's label.
     */
    private String label;
    
    /**
     * The menu item's link.
     */
    private String link;
    
    /**
     * The menu item's submenu (optional).
     */
    private ArrayList<MainMenuItem> submenu;

    /**
     * Construct a menu item.
     */
    public MainMenuItem() {
        this.label = "";
        this.link = "";
        this.submenu = new ArrayList<>();
    }

    /**
     * Get the menu item's label.
     * @return the menu item's label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the menu item's label.
     * @param label the menu item's label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the menu item's link.
     * @return the menu item's link.
     */
    public String getLink() {
        return link;
    }

    /**
     * Set the menu item's link.
     * @param link the menu item's link.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Get the menu item's submenu.
     * @return the menu item's submenu.
     */
    public ArrayList<MainMenuItem> getSubmenu() {
        return submenu;
    }

    /**
     * Set the menu item's submenu.
     * @param submenu a list of MenuItems.
     */
    public void setSubmenu(ArrayList<MainMenuItem> submenu) {
        this.submenu = submenu;
    }    
    
}
