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

package commands.editor;

import commands.Command;
import controller.ScopeHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.components.Input;
import model.types.InputType;

/**
 * Deleterecord command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Deleterecord extends Command{

    /**
     * Constructs a Deleterecord command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Deleterecord (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);  
        this.htmlOutput = false;     
        this.jsonOutput = true;
        this.viewFile = "/success.jsp";
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {   
        try {
            Input balanceSheet = new Input("balanceSheet", InputType.INTEGER, ScopeHandler.getInstance().load(this.request, "bsid", "parameter"));
            Input id = new Input("id", InputType.INTEGER, ScopeHandler.getInstance().load(this.request, "recordid", "parameter"));
            
            //Load input values from parameter scope and store them in an list of inputs.
            ArrayList<Input> inputList = new ArrayList<>();
            inputList.add(balanceSheet);
            inputList.add(id);
            
            HashMap<String, Object> validationParameters = new HashMap<>();
            validationParameters.put("inputList", inputList);
            
            ModelComponentFactory.createModuleComponent(this.request, this.response, "SyntactialInputValidation")
                    .provideParameters(validationParameters)
                    .process();
            
            if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                HashMap<String, Object> deletionParameters = new HashMap<>();
                deletionParameters.put("bsId", balanceSheet.getValue());
                deletionParameters.put("recordId", id.getValue());
                
                ModelComponentFactory.createModuleComponent(this.request, this.response, "DeleteRecords")
                    .provideParameters(deletionParameters)
                    .process();
            } else {
                this.viewFile = "/error.jsp";
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(Deleterecord.class.getName()).log(Level.SEVERE, "Failed to delete record. (" + ex.getMessage() + ")", ex);
            this.viewFile = "/error.jsp";
        }
        
        return this.viewPath + this.viewFile;
    }
    
}


