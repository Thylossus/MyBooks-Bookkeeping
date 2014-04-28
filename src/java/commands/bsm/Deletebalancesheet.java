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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.types.UserType;

/**
 * Deletebalancesheet command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Deletebalancesheet extends Command{

    /**
     * Constructs a Deletebalancesheet command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Deletebalancesheet (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);   
        this.requiredUserType = UserType.STANDARD_USER;
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {     
        
        //Delete the balance sheet in the session.
        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "DeleteBalanceSheet").process();
        } catch (Exception ex) {
            Logger.getLogger(Deletebalancesheet.class.getName()).log(Level.SEVERE, "Could not delete the balance sheet.", ex);
        }
        
        this.viewPath = "/MyBooks-Bookkeeping";
        this.viewFile = "/bsm/balancesheets";
        ScopeHandler.getInstance().store(this.request, "title", "Balance Sheet Management");
        ScopeHandler.getInstance().store(this.request, "orderby", BalanceSheet.CLMN_DATE_OF_LAST_CHANGE);
        
        return this.viewPath + this.viewFile;
    }
    
}


