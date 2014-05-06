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
package commands.user;

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
 * Changename command
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Changename extends Command {

    /**
     * Constructs a Changename command using a request and a response object.
     *
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Changename(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.requiredUserType = UserType.STANDARD_USER;
        this.viewFile = "/user/viewprofile";
    }

    /**
     * Executes the command and returns the location of the view.
     *
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {
        //Check whether the form was submitted or not.
        if (ScopeHandler.getInstance().load(this.request, "submit", "parameter") != null) {
            User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");

            if (user != null) {
                Input firstname = new Input("firstname", InputType.NAME, ScopeHandler.getInstance().load(this.request, "firstname", "parameter"));
                Input lastname = new Input("lastname", InputType.NAME, ScopeHandler.getInstance().load(this.request, "lastname", "parameter"));
                //Not actually required by the semantic input validation needs those
                Input mail = new Input("mail", InputType.MAIL, user.getMail());
                Input password = new Input("password", InputType.PASSWORD, "password");
                Input rePassword = new Input("re-password", InputType.PASSWORD, "password");

                try {
                    //Load input values from parameter scope and store them in an list of inputs.
                    ArrayList<Input> inputList = new ArrayList<>();
                    inputList.add(firstname);
                    inputList.add(lastname);
                    inputList.add(mail);
                    inputList.add(password);
                    inputList.add(rePassword);

                    HashMap<String, Object> validationParameters = new HashMap<>();
                    validationParameters.put("inputList", inputList);
                    validationParameters.put("context", ContextType.USER_DETAILS);

                    ModelComponentFactory.createModuleComponent(this.request, this.response, "SyntactialInputValidation")
                            .provideParameters(validationParameters)
                            .process();

                    if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                    //Semantic validation successful
                        //Start semantic input validation
                        ModelComponentFactory.createModuleComponent(this.request, this.response, "SemanticInputValidation")
                                .provideParameters(validationParameters)
                                .process();

                        if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                            //Semantic validation successful
                            HashMap<String, Object> updateParameters = new HashMap<>();
                            updateParameters.put("firstname", firstname.getValue());
                            updateParameters.put("lastname", lastname.getValue());

                            ModelComponentFactory.createModuleComponent(this.request, this.response, "UpdateUser")
                                    .provideParameters(updateParameters)
                                    .process();
                        } else {
                            //Semantic validation failed
                            Logger.getLogger(Changename.class.getName()).log(Level.WARNING, "Semantic validation failed!");
                        }

                    } else {
                        //Syntactical validation failed
                        Logger.getLogger(Changename.class.getName()).log(Level.WARNING, "Syntactical validation failed!");
                    }

                } catch (Exception ex) {
                    Logger.getLogger(Changename.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                this.viewFile = "/home";
            }
        }

        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
        } catch (Exception ex) {
            Logger.getLogger(Changename.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.viewPath = "/MyBooks-Bookkeeping";

        return this.viewPath + this.viewFile;
    }

}
