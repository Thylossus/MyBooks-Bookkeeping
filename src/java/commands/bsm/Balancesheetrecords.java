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
import database.DBFilter;
import database.Record;
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
 * Balancesheetrecords command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Balancesheetrecords extends Command{

    /**
     * Constructs a Balancesheetrecords command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Balancesheetrecords (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);        
        this.viewFile = "/records.jsp";
        this.htmlOutput = false;
        this.xmlOutput = true;
        this.requiredUserType = UserType.STANDARD_USER;
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {   
        Integer balanceSheetId = Integer.parseInt((String)ScopeHandler.getInstance().load(this.request, "id", "parameter"));
        User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");
        
        if (balanceSheetId != 0 && user != null) {
            DBFilter filter = new DBFilter();
            filter.addConstraint(Record.CLMN_BALANCE_SHEET, SQLConstraintOperator.EQUAL, balanceSheetId);
            
            HashMap<String,Object> params = new HashMap<>();
            params.put("dbFilter", filter);
            params.put("tableName", Record.SELECT_TABLE);
            params.put("orderByColumns", new String[]{Record.CLMN_RECORD_DATE});
            try {
                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                        .provideParameters(params)
                        .process();
            } catch (Exception ex) {
                Logger.getLogger(Balancesheetrecords.class.getName()).log(Level.SEVERE, null, ex);
                this.viewFile = "/error.jsp";
            }
            
        }
        
        return this.viewPath + this.viewFile;
    }
    
}


