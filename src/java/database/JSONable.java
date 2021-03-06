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

package database;

import javax.json.JsonObject;


/**
 * Classes that implement this interface can be parsed to a JSON object representation.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public interface JSONable {
    /**
     * Parse the object to a JSON object.
     * @return JSON object of the object.
     */
    public JsonObject toJSON();
}
