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

package commands.bsm;


import commands.Command;
import controller.ScopeHandler;
import database.BalanceSheet;
import database.DBFilter;
import database.SQLConstraintOperator;
import database.User;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.types.UserType;

/**
 * Balancesheets command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Balancesheets extends Command{

    /**
     * Constructs a Balancesheets command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Balancesheets (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);     
        this.requiredUserType = UserType.STANDARD_USER;
        this.viewFile = "/balancesheets.jsp";
        this.xmlOutput = true;
        ScopeHandler.getInstance().store(request, "title", "Balance Sheet Management");
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {   
        User user = (User)ScopeHandler.getInstance().load(this.request, "user", "session");
        boolean dispatch = true;
        if (ScopeHandler.getInstance().load(this.request, "dispatch") != null) {
            dispatch = (boolean)ScopeHandler.getInstance().load(this.request, "dispatch");
        }
        
        String orderBy = (String)ScopeHandler.getInstance().load(this.request, "orderby");
        if (orderBy == null || orderBy.isEmpty()) {
            orderBy = BalanceSheet.CLMN_DATE_OF_LAST_CHANGE + " DESC";
        } else {
            //Validate the entered orderBy statements to prevent SQL injections
            //and ensure that the upper case version of the column name is used.
            if (orderBy.equalsIgnoreCase(BalanceSheet.CLMN_DATE_OF_CREATION + " DESC") || orderBy.equalsIgnoreCase(BalanceSheet.CLMN_DATE_OF_CREATION)) {
                orderBy = BalanceSheet.CLMN_DATE_OF_CREATION + " DESC";
            } else if (orderBy.equalsIgnoreCase(BalanceSheet.CLMN_DATE_OF_LAST_CHANGE + " DESC") || orderBy.equalsIgnoreCase(BalanceSheet.CLMN_DATE_OF_LAST_CHANGE)) {
                orderBy = BalanceSheet.CLMN_DATE_OF_LAST_CHANGE + " DESC";
            } else if (orderBy.equalsIgnoreCase(BalanceSheet.CLMN_TITLE)) {
                orderBy = BalanceSheet.CLMN_TITLE;
            } else {
                orderBy = BalanceSheet.CLMN_DATE_OF_LAST_CHANGE + " DESC";
            }
        }
        //Update value in request scope after validation
        ScopeHandler.getInstance().store(this.request, "orderby", orderBy);
        
        //User should be available because the command factory already checked the login and this command cannot be called if no one is logged in.
        if (user != null) {
            
            //Only load balance sheets if this command is called from the custom tag (dispatch = false in this case).
            if (!dispatch) {
                DBFilter filter = new DBFilter();
                filter.addConstraint(BalanceSheet.CLMN_OWNER, SQLConstraintOperator.EQUAL, user.getId());

                HashMap<String,Object> dataListParameters = new HashMap<>();
                dataListParameters.put("tableName", BalanceSheet.SELECT_TABLE);
                dataListParameters.put("dbFilter", filter);
                dataListParameters.put("orderByColumns", new String[]{orderBy});

                try {
                    ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                            .provideParameters(dataListParameters)
                            .process();

                } catch (Exception ex) {
                    Logger.getLogger(Balancesheets.class.getName()).log(Level.SEVERE, "Could not create data list.", ex);
                }
            }
            
        } else {
            if (this.viewPath.equals("/xml")) {
                this.viewFile = "/error.jsp";
            } else {
                this.viewPath = "/MyBooks-Bookkeeping";
                this.viewFile = "/home";
                ScopeHandler.getInstance().store(request, "title", "Home");
            }
        }
        
        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
        } catch (Exception ex) {
            Logger.getLogger(Balancesheets.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return this.viewPath + this.viewFile;
    }
    
}


