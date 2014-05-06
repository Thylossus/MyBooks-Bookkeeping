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
import database.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.types.UserType;

/**
 * Viewbalancesheet command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Viewbalancesheet extends Command{

    /**
     * Constructs a Viewbalancesheet command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Viewbalancesheet (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);        
        this.viewFile = "/viewbalancesheet.jsp";
        ScopeHandler.getInstance().store(this.request, "title", "View Balance Sheet");
        this.requiredUserType = UserType.STANDARD_USER;
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {    
        BalanceSheet activeBalanceSheet = (BalanceSheet) ScopeHandler.getInstance().load(this.request, "balanceSheet", "session");
        User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");

        if (activeBalanceSheet != null && user != null) {
            try {
                //Load records
                ModelComponentFactory.createModuleComponent(this.request, this.response, "LoadRecordsOfAMonth")
                        .process();
            } catch (Exception ex) {
                Logger.getLogger(Openbalancesheeteditor.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            //If no balance sheet is in the session, forward the user to the balance sheet overview.
            this.viewPath = "/MyBooks-Bookkeeping";
            this.viewFile = "/balancesheets";
            ScopeHandler.getInstance().store(this.request, "orderby", BalanceSheet.CLMN_DATE_OF_LAST_CHANGE);
            ScopeHandler.getInstance().store(this.request, "title", "Balance Sheet Management");
        }

        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
        } catch (Exception ex) {
            Logger.getLogger(Openbalancesheeteditor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.viewPath + this.viewFile;
    }
    
}


