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

import model.types.InputType;

/**
 * Objects of this class represent an input and its type.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Input {
    /**
     * A key to identify the inpput.
     */
    private String key;
    
    /**
     * The input's type.
     */
    private InputType type;
    /**
     * The input's value.
     */
    private Object value;
    
    /**
     * Constructs an input object.
     * @param key the input's key.
     * @param type the input's type.
     * @param value the input's value.
     */
    public Input (String key, InputType type, Object value) {
        this.key = key;
        this.type = type;
        this.value = value;
    }
    
    /**
     * Get the input's key.
     * @return the input's key.
     */
    public String getKey() {
        return this.key;
    }
    
    /**
     * Get the input's type.
     * @return the input's type.
     */
    public InputType getType() {
        return this.type;
    }
    
    /**
     * Get the input's value.
     * @return the input's value.
     */
    public Object getValue() {
        return this.value;
    }
    
    /**
     * Set the input's value.
     * @param value a new or converted input value.
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
