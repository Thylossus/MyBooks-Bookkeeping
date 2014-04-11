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
 * Context for the upload file form.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class UploadFile extends EditUploadedFile{

    /**
     * Construct the upload file context.
     * @param inputList 
     */
    public UploadFile(ArrayList<Input> inputList) {
        super(inputList);
    }
    
    /**
     * Validate the given inputs for the upload file context.
     * @return 
     */
    @Override
    public boolean validate() {
        //Perform validation of the file input and return false if it is not valid.
        //Otherwise continuej, i.e. use super.validate() to validate title and description).
        return super.validate();
    }
    
}
