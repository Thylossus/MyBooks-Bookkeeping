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
import commands.Home;
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
 * Extendedreports command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Extendedreports extends Command{

    /**
     * Constructs a Extendedreports command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Extendedreports (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);   
        this.requiredUserType = UserType.PREMIUM_USER;
        this.viewFile = "/extendedreports.jsp";
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {        
        BalanceSheet balanceSheet = (BalanceSheet) ScopeHandler.getInstance().load(this.request, "balanceSheet", "session");
        User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");

        if (balanceSheet != null && user != null) {

            try {
                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
            } catch (Exception ex) {
                Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            this.viewPath = "/MyBooks-Bookkeeping";
            if (user != null) {
                this.viewFile = "/bsm/balancesheets";
            } else {
                this.viewFile = "/home";
            }
        }

        return this.viewPath + this.viewFile;
    }
    
}


