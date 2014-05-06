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
package model.components.sysmgmt;

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

/**
 * Delete an user and all his balance sheets.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class DeleteUser extends ModelComponent {

    private int userId;

    /**
     * Construct a DeleteUser object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public DeleteUser(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for DeleteUser
     */
    @Override
    public void process() {
        ScopeHandler.getInstance().store(this.request, "userDeleted", false);

        if (this.userId != 0) {
            try {
                //Get user
                DBFilter filter = new DBFilter();
                filter.addConstraint(User.CLMN_ID, SQLConstraintOperator.EQUAL, userId);
                HashMap<String, Object> params = new HashMap<>();
                params.put("tableName", User.SELECT_TABLE);
                params.put("dbFilter", filter);
                params.put("orderByColumns", new String[]{User.CLMN_ID});

                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                        .provideParameters(params)
                        .process();

                ArrayList<User> users = (ArrayList<User>) ScopeHandler.getInstance().load(this.request, "dataList");
                User user = null;
                if (users != null && users.size() == 1) {
                    user = users.get(0);
                }

                if (user != null) {

                    //Get the users balance sheets
                    filter = new DBFilter();
                    filter.addConstraint(BalanceSheet.CLMN_OWNER, SQLConstraintOperator.EQUAL, user.getId());
                    params.put("tableName", BalanceSheet.SELECT_TABLE);
                    params.put("orderByColumns", new String[]{BalanceSheet.CLMN_ID});
                    params.put("dbFilter", filter);

                    ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                            .provideParameters(params)
                            .process();

                    ArrayList<BalanceSheet> balanceSheets = (ArrayList<BalanceSheet>) ScopeHandler.getInstance().load(this.request, "dataList");

                    //Delete balance sheets and records
                    if (balanceSheets != null) {
                        for (BalanceSheet balanceSheet : balanceSheets) {
                            params.put("balanceSheet", balanceSheet);
                            ModelComponentFactory.createModuleComponent(this.request, this.response, "DeleteBalanceSheet")
                                    .provideParameters(params)
                                    .process();
                        }
                        
                        if (user.delete()) {
                            ScopeHandler.getInstance().store(this.request, "userDeleted", true);
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(DeleteUser.class.getName()).log(Level.SEVERE, null, ex);
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
        if (params.get("userId") != null) {
            this.userId = (int) params.get("userId");
        }

        return this;
    }

}
