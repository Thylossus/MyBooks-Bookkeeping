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
 * Closebalancesheet command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Closebalancesheet extends Command{

    /**
     * Constructs a Closebalancesheet command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Closebalancesheet (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);   
        this.requiredUserType = UserType.STANDARD_USER;
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {  
        this.viewPath = "/MyBooks-Bookkeeping";
        this.viewFile = "/bsm/balancesheets";
        ScopeHandler.getInstance().store(this.request, "title", "Balance Sheet Management");
        ScopeHandler.getInstance().store(this.request, "orderby", BalanceSheet.CLMN_DATE_OF_LAST_CHANGE);
        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "CloseBalanceSheet").process();
        } catch (Exception ex) {
            Logger.getLogger(Closebalancesheet.class.getName()).log(Level.SEVERE, "Could not close the balance sheet.", ex);
        }
        
        return this.viewPath + this.viewFile;
    }
    
}


