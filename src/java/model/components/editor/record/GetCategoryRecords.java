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
import database.BalanceSheet;
import database.Category;
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
 * Get all records that are associated with a category within either the current session balance sheet or all
 * balance sheets of a user.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class GetCategoryRecords extends ModelComponent{

    /**
     * The category that is used for retrieving the records.
     */
    private Category category;
    
    /**
     * Construct a GetCategoryRecords object
     * @param request The request's request object
     * @param response The request's response object
     */
    public GetCategoryRecords(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.category = null;
    }

    /**
     * Process the action for GetCategoryRecords
     */
    @Override
    public void process() {
        BalanceSheet bs = (BalanceSheet) ScopeHandler.getInstance().load(this.request, "balanceSheet", "session");

        if (bs != null && this.category != null) {
            DBFilter filter = new DBFilter();
            filter.addConstraint(Record.CLMN_CATEGORY, SQLConstraintOperator.EQUAL, this.category.getId());
            filter.addConstraint(Record.CLMN_BALANCE_SHEET, SQLConstraintOperator.EQUAL, bs.getId());

            HashMap<String, Object> dataParams = new HashMap<>();
            dataParams.put("tableName", Record.SELECT_TABLE);
            dataParams.put("dbFilter", filter);
            dataParams.put("orderByColumns", new String[]{Record.CLMN_RECORD_DATE + " DESC", Record.CLMN_ID});
            try {
                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                        .provideParameters(dataParams)
                        .process();

                ArrayList<Record> records = (ArrayList<Record>) ScopeHandler.getInstance().load(this.request, "dataList");

                if (records != null) {
                    ScopeHandler.getInstance().store(this.request, "records", records);
                    ScopeHandler.getInstance().store(this.request, "numberOfRecords", records.size());
                } else {
                    //Ensure that request attribute "records" is set to prevent null pointer exception
                    ScopeHandler.getInstance().store(this.request, "records", new ArrayList<Record>());
                    ScopeHandler.getInstance().store(this.request, "numberOfRecords", 0);
                }
            } catch (Exception ex) {
                Logger.getLogger(GetCategoryRecords.class.getName()).log(Level.SEVERE, "Could not load records of category " + this.category.getName(), ex);
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
        if (params.get("category") != null) {
            this.category = (Category)params.get("category");
        }
        
        return this;
    }

}
