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
 * Parent class for form contexts.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public abstract class Context {
    /**
     * List of inputs for of the form. The input list is already validated syntactically.
     */
    protected ArrayList<Input> inputList;
    
    /**
     * Validate the form by means of its semantics.
     * @return true if the validation is successful, false otherwise.
     */
    public abstract boolean validate();
    
    /**
     * Search for an input by its key.
     * @param key a string representing the searched input's key.
     * @return The value of the input that matches the given key. If no input has the given key, null will be returned.
     */
    protected Object searchInputValue(String key) {
        for (Input i : this.inputList) {
            if (i.getKey().equals(key)) {
                return i.getValue();
            }
        }
        
        return null;
    }
    
}
