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

import beans.Date;
import commands.Command;
import controller.ScopeHandler;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.components.Input;
import model.types.InputType;

/**
 * Loadrecordsofmonth command
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Loadrecordsofmonth extends Command {

    /**
     * Constructs a Loadrecordsofmonth command using a request and a response
     * object.
     *
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Loadrecordsofmonth(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.htmlOutput = false;
        this.jsonOutput = true;
        this.viewFile = "/recordList.jsp";
    }

    /**
     * Executes the command and returns the location of the view.
     *
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {
        Input month = new Input("month", InputType.INTEGER, ScopeHandler.getInstance().load(this.request, "month", "parameter"));
        Input year = new Input("year", InputType.INTEGER, ScopeHandler.getInstance().load(this.request, "year", "parameter"));

        if (month.getValue() != null && year.getValue() != null) {
            try {
                //Load input values from parameter scope and store them in an list of inputs.
                ArrayList<Input> inputList = new ArrayList<>();
                inputList.add(month);
                inputList.add(year);
                
                HashMap<String, Object> validationParameters = new HashMap<>();
                validationParameters.put("inputList", inputList);
                
                ModelComponentFactory.createModuleComponent(this.request, this.response, "SyntactialInputValidation")
                        .provideParameters(validationParameters)
                        .process();
                
                if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                    
                    HashMap<String, Object> params = new HashMap<>();
                    Date monthParam = new Date();
                    monthParam.setCalendar(new GregorianCalendar((int)year.getValue(), (int)month.getValue(), 1));
                    params.put("month", monthParam);
                    
                    try {
                        //Load records
                        ModelComponentFactory.createModuleComponent(this.request, this.response, "LoadRecordsOfAMonth")
                                .provideParameters(params)
                                .process();
                    } catch (Exception ex) {
                        Logger.getLogger(Loadrecordsofmonth.class.getName()).log(Level.SEVERE, "Could not load records of the specified month. (" + ex.getMessage() + ")", ex);
                        this.viewFile = "/error.jsp";
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(Loadrecordsofmonth.class.getName()).log(Level.SEVERE, "Syntactical validation failed (" + ex.getMessage() + ")", ex);
                this.viewFile = "/error.jsp";
            }
        }

        return this.viewPath + this.viewFile;
    }

}
