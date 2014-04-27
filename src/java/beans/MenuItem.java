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
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class MenuItem {

    /**
     * The menu item's label.
     */
    private String label;

    /**
     * The menu item's link.
     */
    private String link;

    /**
     * A string which identifies a bootstrap glyphicon. See http://getbootstrap.com/components/#glyphicons for the valid glyphicon identifiers.
     * E.g. "glyphicon-asterisk".
     */
    private String glyphicon;
    
    /**
     * The menu item's submenu (optional).
     */
    private ArrayList<MenuItem> submenu;

    /**
     * True if the menu item is active and false otherwise.
     */
    private boolean active;

    /**
     * Construct a menu item.
     */
    public MenuItem() {
        this.label = "";
        this.link = "";
        this.glyphicon = "";
        this.submenu = new ArrayList<>();
        this.active = false;
    }

    /**
     * Get the menu item's label.
     *
     * @return the menu item's label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the menu item's label.
     *
     * @param label the menu item's label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the menu item's link.
     *
     * @return the menu item's link.
     */
    public String getLink() {
        return link;
    }

    /**
     * Set the menu item's link.
     *
     * @param link the menu item's link.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Set the glyphicon identifier for the menu item.
     * @param glyphicon a string that represents a glyphicon (e.g. "glyphicon-asterisk").
     */
    public void setGlyphicon(String glyphicon) {
        this.glyphicon = glyphicon;
    }

    /**
     * Get the menu item's submenu.
     *
     * @return the menu item's submenu.
     */
    public ArrayList<MenuItem> getSubmenu() {
        return submenu;
    }

    /**
     * Set the menu item's submenu.
     *
     * @param submenu a list of MenuItems.
     */
    public void setSubmenu(ArrayList<MenuItem> submenu) {
        this.submenu = submenu;
    }

    /**
     * Transform the menu item into an active menu item.
     */
    public void setActive() {
        this.active = true;
    }

    /**
     * Check whether the menu item is active or not.
     *
     * @return True if the menu item is active and false if not.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Transform the menu item into a string with its html representation.
     *
     * @return the menu item as a string.
     */
    @Override
    public String toString() {
        String html = "<li";

        //Check whether a submenu is available.
        if (this.submenu != null && this.submenu.size() > 0) {

            html += " class=\"";
            
            if (this.active) {
                html += "active ";
            }
            
            html += "dropdown\">";
            
            html += "<a "
                    + "href=\"" + this.link + "\" "
                    + "class=\"dropdown-toggle\" "
                    + "data-toggle=\"dropdown\">";
            
            if (!this.glyphicon.isEmpty()) {
                html += "<span class=\"glyphicon " + this.glyphicon + "\"></span> ";
            }
            
            html += this.label
                    + " <b class=\"caret\"></b>"
                    + "</a>";
            
            html += "<ul class=\"dropdown-menu\">\n";
            
            for (MenuItem item : this.submenu) {
                html += item.toString();
            }
            
            html += "</ul>\n";

        } else {
            if (this.active) {
                html += " class=\"active\"";
            }

            html += "><a href=\"";
            html += this.link;
            html += "\">";
            html += this.label;

            html += "</a>";
        }

        html += "</li>\n";

        return html;
    }

}
