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

package model.components.editor.category;


import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;

/**
 * _DESCRIPTION_
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class UpdateCategory extends ModelComponent{

    /**
     * Construct a UpdateCategory object
     * @param request The request's request object
     * @param response The request's response object
     */
    public UpdateCategory(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for UpdateCategory
     */
    @Override
    public void process() {
        
    }

    /**
     * Provide parameters to the model component.
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        return this;
    }

}
