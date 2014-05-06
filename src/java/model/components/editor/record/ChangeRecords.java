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

package model.components.editor.record;


import database.Record;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;

/**
 * Takes a list of records and updates their corresponding database records. Updating a single record is
 * also possible.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class ChangeRecords extends ModelComponent{

    /**
     * A list of records, which need to be updated.
     */
    private ArrayList<Record> records;
    
    /**
     * Construct a ChangeRecords object
     * @param request The request's request object
     * @param response The request's response object
     */
    public ChangeRecords(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for ChangeRecords
     */
    @Override
    public void process() {
        if (this.records != null && !this.records.isEmpty()) {
            for (Record record : this.records) {
                record.update();
            }
        }
    }

    /**
     * Provide parameters to the model component.
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if (params.get("records") != null) {
            this.records = (ArrayList<Record>)params.get("records");
        }
        
        return this;
    }

}
