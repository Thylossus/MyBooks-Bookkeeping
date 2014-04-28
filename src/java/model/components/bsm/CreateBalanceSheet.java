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


import database.BalanceSheet;
import database.User;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;

/**
 * Create balance sheet model component. Takes a title and inserts a new balance sheet with the given title in the database.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class CreateBalanceSheet extends ModelComponent{

    /**
     * The balance sheet's title.
     */
    private String title;
    /**
     * The balance sheet's owner.
     */
    private User owner;
    
    /**
     * Construct a CreateBalanceSheet object
     * @param request The request's request object
     * @param response The request's response object
     */
    public CreateBalanceSheet(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for CreateBalanceSheet
     */
    @Override
    public void process() {
        if (this.title != null && this.owner != null) {
            BalanceSheet newBalanceSheet = new BalanceSheet();
            newBalanceSheet.setTitle(this.title);
            newBalanceSheet.setOwner(this.owner.getId());
            
            if (newBalanceSheet.insert()) {
                //Insertion successful
                Logger.getLogger(CreateBalanceSheet.class.getName()).log(Level.FINE, "Inserting new balance sheet was successful!");
            } else {
                //Insertion failed
                Logger.getLogger(CreateBalanceSheet.class.getName()).log(Level.WARNING, "Inserting new balance sheet failed!");
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
        if (params.get("title") != null) {
            this.title = params.get("title").toString();
        }
        
        if (params.get("owner") != null) {
            this.owner = (User)params.get("owner");
        }
        
        return this;
    }

}
