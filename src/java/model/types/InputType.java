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

package model.types;

/**
 * Enumerator for input types.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public enum InputType {

    /**
     * Input is a forename, surename, title, name.
     */
    NAME,
    
    /**
     * Input is a description.
     */
    DESCRIPTION,

    /**
     * Input is an e-mail address.
     */
    MAIL,

    /**
     * Input is a credit card number.
     */
    CREDIT_CARD_NUMBER,
    
    /**
     * Input is an integer number.
     */
    INTEGER,

    /**
     * Input is a floating point number.
     */
    FLOAT,

    /**
     * Input is a date.
     */
    DATE,
    
    /**
     * Input is a password. 
     */
    PASSWORD,
    
    /**
     * Input is a colour.
     */
    COLOUR;
}