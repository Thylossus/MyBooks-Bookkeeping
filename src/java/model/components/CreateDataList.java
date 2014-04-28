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

import controller.ScopeHandler;
import database.ActiveRecord;
import database.ActiveRecordFactory;
import database.DBFilter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;

/**
 * Takes the name of a table or view, the desired order, and ltering arguments, 
 * builds a database query, and creates a list of the results.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class CreateDataList extends ModelComponent{

    private String tableName;
    private String[] orderByColumns;
    private DBFilter dbFilter;
    
    /**
     * Construct a CreateDataList object
     * @param request The request's request object
     * @param response The request's response object
     */
    public CreateDataList(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.tableName = "";
        this.orderByColumns = null;
        this.dbFilter = null;
    }

    /**
     * Process the action for CreateDataList
     */
    @Override
    public void process() {
        //Check whether the table name is empty.
        if (!this.tableName.isEmpty()) {
            try {
                //Parameter classes and values for "findAll"
                Class[] paramClasses;
                Object[] paramValues;
                
                //Check which parameters are required.
                if (this.orderByColumns != null && this.dbFilter != null) {
                    //ordering and filtering is required
                    paramClasses = new Class[]{String[].class, DBFilter.class};
                    paramValues = new Object[]{this.orderByColumns, this.dbFilter};
                } else if (this.orderByColumns != null) {
                    //only ordering is required
                    paramClasses = new Class[]{String[].class};
                    paramValues = new Object[]{this.orderByColumns};
                } else if (this.dbFilter != null) {
                    //only filtering is required
                    paramClasses = new Class[]{DBFilter.class};
                    paramValues = new Object[]{this.dbFilter};
                } else {
                    //nothing required
                    paramClasses = new Class[]{};
                    paramValues = new Object[]{};
                }
                
                //Get the active record class
                Class ar = ActiveRecordFactory.createActiveRecord(this.tableName);
                //Access the "findAll" static method
                Method findAll = ar.getMethod("findAll", paramClasses);
                //Invoke "findAll"
                ArrayList<ActiveRecord> dataList = (ArrayList<ActiveRecord>)findAll.invoke(null, paramValues);
                //Store dataList in request scope
                ScopeHandler.getInstance().store(this.request, "dataList", dataList);
            } catch (Exception ex) {
                //Could not create the active record 
                //  (did not find any active record for the specified table OR
                //   the found active record class does not implement the static method "findAll")
                Logger.getLogger(CreateDataList.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //No table name specified => cannot process the module component
        }
    }

    /**
     * Provide parameters to the model component.
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if (params.get("tableName") != null) {
            this.tableName = params.get("tableName").toString();
        }
        
        if (params.get("dbFilter") != null) {
            this.dbFilter = (DBFilter) params.get("dbFilter");
        }
        
        if (params.get("orderByColumns") != null) {
            this.orderByColumns = (String[]) params.get("orderByColumns");
        }
        
        return this;
    }

}
