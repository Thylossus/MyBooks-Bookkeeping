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
import database.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.components.Input;
import model.types.ContextType;
import model.types.InputType;
import model.types.UserType;

/**
 * Loadbalancesheet command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Loadbalancesheet extends Command{

    /**
     * Constructs a LoadBalanceSheet command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Loadbalancesheet (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);        
        this.viewFile = "/balancesheet.jsp";
        this.jsonOutput = true;
        this.requiredUserType = UserType.STANDARD_USER;
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {        
        //Check whether the form was submitted or not.
        if (ScopeHandler.getInstance().load(this.request, "submit", "parameter") != null) {
            Input title = new Input("title", InputType.NAME, ScopeHandler.getInstance().load(this.request, "title", "parameter"));
            User owner = (User) ScopeHandler.getInstance().load(this.request, "user", "session");
            if (owner != null) {
                try {
                    //Load input values from parameter scope and store them in an list of inputs.
                    ArrayList<Input> inputList = new ArrayList<>();
                    inputList.add(title);

                    HashMap<String, Object> validationParameters = new HashMap<>();
                    validationParameters.put("inputList", inputList);
                    validationParameters.put("context", ContextType.BALANCE_SHEET);

                    ModelComponentFactory.createModuleComponent(this.request, this.response, "SyntactialInputValidation")
                            .provideParameters(validationParameters)
                            .process();

                    if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                        //Syntactical validation successful
                        //Start semantic input validation
                        ModelComponentFactory.createModuleComponent(this.request, this.response, "SemanticInputValidation")
                                .provideParameters(validationParameters)
                                .process();

                        if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                            //Semantic validation successful
                            HashMap<String, Object> loadParameters = new HashMap<>();
                            loadParameters.put("title", title.getValue());
                            loadParameters.put("owner", owner);

                            //Load balance sheet
                            ModelComponentFactory.createModuleComponent(this.request, this.response, "LoadBalanceSheet")
                                    .provideParameters(loadParameters)
                                    .process();

                            
                        } else {
                            //Semantic validation failed
                            Logger.getLogger(Loadbalancesheet.class.getName()).log(Level.WARNING, "Semantic validation failed!");
                        }

                    } else {
                        //Syntactical validation failed
                        Logger.getLogger(Loadbalancesheet.class.getName()).log(Level.WARNING, "Syntactical validation failed!");
                    }

                } catch (Exception ex) {
                    Logger.getLogger(Loadbalancesheet.class.getName()).log(Level.SEVERE, null, ex);

                }
            }
        }

        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
        } catch (Exception ex) {
            Logger.getLogger(Loadbalancesheet.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.viewPath + this.viewFile;
    }
    
}


