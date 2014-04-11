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

package model.forms;

import java.util.ArrayList;
import model.components.Input;

/**
 * Context for the create and change category forms.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Category extends Context{

    /**
     * Construct the category context.
     * @param inputList 
     */
    public Category(ArrayList<Input> inputList) {
        this.inputList = inputList;
    }
    
    /**
     * Validate the given inputs for the category context.
     * @return 
     */
    @Override
    public boolean validate() {
        return false;
    }
    
}
