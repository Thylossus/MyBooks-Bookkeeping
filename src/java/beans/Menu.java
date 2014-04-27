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
 * Java Bean for a general menu.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Menu {
    /**
     * Items of the menu.
     */
    protected ArrayList<MenuItem> menuItems;

    /**
     * Construct a menu.
     */
    public Menu() {
        this.menuItems = new ArrayList<>();
    }
    
    /**
     * Get the menu's items.
     * @return the menu's items.
     */
    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }
    
    /**
     * Add item with submenu to the menu. The dropdown toggle menu item will have the link "#".
     * @param label the item's label.
     * @param submenu the item's submenu.
     * @return the added item.
     */
    public MenuItem addItem(String label, ArrayList<MenuItem> submenu) {
        MenuItem item = this.addItem(label, "#");
        
        item.setSubmenu(submenu);
        
        return item;
    }
    
    /**
     * Add an item to the menu.
     * @param label the item's label.
     * @param link the item's link.
     * @return the added item.
     */
    public MenuItem addItem(String label, String link) {
        MenuItem item = new MenuItem();
        
        item.setLabel(label);
        item.setLink(link);
        this.menuItems.add(item);
        
        return item;
    }
    
    /**
     * Add an item to the menu. The item's link is constructed from its label.
     * @param label the item's label.
     * @return the added item.
     */
    public MenuItem addItem(String label) {
        MenuItem item = new MenuItem();
        
        item.setLabel(label);
        item.setLink("/MyBooks-Bookkeeping/" + label.toLowerCase());      //A change might be required here!
        this.menuItems.add(item);
        
        return item;
    }
    
    
}
