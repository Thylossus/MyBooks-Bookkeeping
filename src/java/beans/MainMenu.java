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
 * Java Bean for main menus.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class MainMenu {

    /**
     * Items of the main menu.
     */
    private ArrayList<MainMenuItem> menuItems;
    /**
     * The active item of the main menu.
     */
    private MainMenuItem activeItem;
    
    /**
     * The menu's title (read-only).
     */
    private String title;

    /**
     * Construct an empty main menu.
     */
    public MainMenu() {
        this.menuItems = new ArrayList<>();
        this.activeItem = null;
        
        this.title = "MyBooks";
    }

    /**
     * Get the menu's items.
     * @return the menu's items.
     */
    public ArrayList<MainMenuItem> getMenuItems() {
        return menuItems;
    }

    /**
     * Get the menu's active item.
     * @return the menu's active item.
     */
    public MainMenuItem getActiveItem() {
        return activeItem;
    }

    /**
     * Set the menu's active item.
     * @param activeItem the menu's active item.
     */
    public void setActiveItem(MainMenuItem activeItem) {
        this.activeItem = activeItem;
    }  

    /**
     * Get the main menu's title.
     * @return the main menu's title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Add item with submenu to the menu.
     * @param label the item's label.
     * @param link the item's link.
     * @param submenu the item's submenu.
     * @return the added item.
     */
    public MainMenuItem addItem(String label, String link, ArrayList<MainMenuItem> submenu) {
        MainMenuItem item = this.addItem(label, link);
        
        item.setSubmenu(submenu);
        
        return item;
    }
    
     /**
     * Add item with submenu to the menu. The item's link is constructed from its label.
     * @param label the item's label.
     * @param submenu the item's submenu.
     * @return the added item.
     */
    public MainMenuItem addItem(String label, ArrayList<MainMenuItem> submenu) {
        MainMenuItem item = this.addItem(label);
        
        item.setSubmenu(submenu);
        
        return item;
    }
    
    /**
     * Add an item to the menu.
     * @param label the item's label.
     * @param link the item's link.
     * @return the added item.
     */
    public MainMenuItem addItem(String label, String link) {
        MainMenuItem item = new MainMenuItem();
        
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
    public MainMenuItem addItem(String label) {
        MainMenuItem item = new MainMenuItem();
        
        item.setLabel(label);
        item.setLink("/MyBooks-Bookkeeping/" + label.toLowerCase());      //A change might be required here!
        this.menuItems.add(item);
        
        return item;
    }
    
}
