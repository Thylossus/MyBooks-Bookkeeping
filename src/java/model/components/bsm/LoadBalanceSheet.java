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
package model.components.bsm;

import commands.bsm.Balancesheets;
import controller.ScopeHandler;
import database.BalanceSheet;
import database.DBFilter;
import database.SQLConstraintOperator;
import database.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.ModelComponentFactory;
import tags.BalanceSheetOverview;

/**
 * Loads a balance sheet with all its records and stores it in the session for
 * faster access. There may only be one balance sheet opened at a time. If a
 * balance sheet is not required any longer, it should be closed by /MC80/ Close
 * Balance Sheet after it has been used .
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class LoadBalanceSheet extends ModelComponent {

    /**
     * The balance sheet's title.
     */
    private String title;
    /**
     * The balance sheet's owner.
     */
    private User owner;

    /**
     * Construct a LoadBalanceSheet object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public LoadBalanceSheet(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for LoadBalanceSheet
     */
    @Override
    public void process() {
        if (this.owner != null && this.title != null) {
            DBFilter filter = new DBFilter();
            filter.addConstraint(BalanceSheet.CLMN_OWNER, SQLConstraintOperator.EQUAL, this.owner.getId());
            filter.addConstraint(BalanceSheet.CLMN_TITLE, SQLConstraintOperator.LIKE, this.title);

            HashMap<String, Object> dataListParameters = new HashMap<>();
            dataListParameters.put("tableName", BalanceSheet.SELECT_TABLE);
            dataListParameters.put("dbFilter", filter);

            try {
                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                        .provideParameters(dataListParameters)
                        .process();

                ArrayList<BalanceSheet> balanceSheetList = (ArrayList<BalanceSheet>) ScopeHandler.getInstance().load(this.request, "dataList");
                
                if (balanceSheetList.size() == 1) {
                    //Store loaded balance sheet in session
                    ScopeHandler.getInstance().store(this.request, "balanceSheet", balanceSheetList.get(0), "session");
                } else {
                    Logger.getLogger(LoadBalanceSheet.class.getName()).log(Level.SEVERE, "Could not load balance sheet.");
                }

            } catch (Exception ex) {
                Logger.getLogger(LoadBalanceSheet.class.getName()).log(Level.SEVERE, "Could not create data list.", ex);
            }
        }
    }

    /**
     * Provide parameters to the model component.
     *
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for
     * concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if (params.get("title") != null) {
            this.title = params.get("title").toString();
        }

        if (params.get("owner") != null) {
            this.owner = (User) params.get("owner");
        }

        return this;
    }

}
