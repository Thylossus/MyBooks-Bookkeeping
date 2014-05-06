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
package commands.sysmgmt;

import commands.Command;
import controller.ScopeHandler;
import database.DBFilter;
import database.SQLConstraintOperator;
import database.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.components.Input;
import model.types.InputType;
import model.types.UserType;

/**
 * Deleteuser command
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Deleteuser extends Command {

    /**
     * Constructs a Deleteuser command using a request and a response object.
     *
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Deleteuser(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.viewFile = "/error.jsp";
        this.htmlOutput = false;
        this.jsonOutput = true;
        this.requiredUserType = UserType.ADMINISTRATOR;
    }

    /**
     * Executes the command and returns the location of the view.
     *
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {

        if (ScopeHandler.getInstance().load(this.request, "id", "parameter") != null) {
            Input id = new Input("id", InputType.INTEGER, ScopeHandler.getInstance().load(this.request, "id", "parameter"));
            try {
                //Load input values from parameter scope and store them in an list of inputs.
                ArrayList<Input> inputList = new ArrayList<>();
                inputList.add(id);

                HashMap<String, Object> params = new HashMap<>();
                params.put("inputList", inputList);

                ModelComponentFactory.createModuleComponent(this.request, this.response, "SyntactialInputValidation")
                        .provideParameters(params)
                        .process();

                if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {

                    if ((int) id.getValue() != 0) {

                        //Call model component DeleteUser
                        params.put("userId", id.getValue());
                        ModelComponentFactory.createModuleComponent(this.request, this.response, "DeleteUser")
                                .provideParameters(params)
                                .process();
                                         
                        if (ScopeHandler.getInstance().load(this.request, "userDeleted") != null 
                                &&(boolean)ScopeHandler.getInstance().load(this.request, "userDeleted")) {
                            this.viewFile = "/success.jsp";
                        }
                        
                    }

                }

            } catch (Exception ex) {
                Logger.getLogger(Deleteuser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.viewPath + this.viewFile;
    }

}
