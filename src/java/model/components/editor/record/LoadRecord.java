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


import controller.ScopeHandler;
import database.DBFilter;
import database.Record;
import database.SQLConstraintOperator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.ModelComponentFactory;

/**
 * Load a single record from the current session balance sheet.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class LoadRecord extends ModelComponent{

    /**
     * Balance sheet id.
     */
    private int bsId;
    /**
     * Record id.
     */
    private int recordId;
    
    /**
     * Construct a LoadRecord object
     * @param request The request's request object
     * @param response The request's response object
     */
    public LoadRecord(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for LoadRecord
     */
    @Override
    public void process() {
        if (this.bsId != 0 && this.recordId != 0) {
            DBFilter filter = new DBFilter();
            filter.addConstraint(Record.CLMN_BALANCE_SHEET, SQLConstraintOperator.EQUAL, this.bsId);
            filter.addConstraint(Record.CLMN_ID, SQLConstraintOperator.EQUAL, this.recordId);
            
            HashMap<String,Object> params = new HashMap<>();
            params.put("tableName", Record.SELECT_TABLE);
            params.put("dbFilter", filter);
            
            try {
                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                        .provideParameters(params)
                        .process();
            } catch (Exception ex) {
                Logger.getLogger(LoadRecord.class.getName()).log(Level.SEVERE, "Could not load record. (" + ex.getMessage() + ")", ex);
            }
            
            ArrayList<Record> records = (ArrayList<Record>)ScopeHandler.getInstance().load(this.request, "dataList");
            
            if (records != null && records.size() == 1) {
                ScopeHandler.getInstance().store(this.request, "record", records.get(0));
            } else {
                Logger.getLogger(LoadRecord.class.getName()).log(Level.WARNING, "Could not load record.");
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
        if (params.get("bsId") != null) {
            this.bsId = (int) params.get("bsId");
        }
        if (params.get("recordId") != null) {
            this.recordId = (int) params.get("recordId");
        }
        
        return this;
    }

}
