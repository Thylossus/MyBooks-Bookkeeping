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
import database.BalanceSheet;
import database.Category;
import database.Record;
import database.User;
import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.components.Input;
import model.types.ContextType;
import model.types.InputType;

/**
 * Changerecord command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Changerecord extends Command{

    /**
     * Constructs a Changerecord command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Changerecord (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);   
        this.htmlOutput = false;
        this.jsonOutput = true;
        this.viewFile = "/record.jsp";
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {        
        //Check whether the form was submitted or not.
        if (ScopeHandler.getInstance().load(this.request, "submit", "parameter") != null) {
            User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");
            BalanceSheet bs = (BalanceSheet) ScopeHandler.getInstance().load(this.request, "balanceSheet", "session");

            if (user != null && bs != null) {

                Input id = new Input("id", InputType.INTEGER, ScopeHandler.getInstance().load(this.request, "recordid", "parameter"));
                Input title = new Input("title", InputType.NAME, ScopeHandler.getInstance().load(this.request, "title", "parameter"));
                Input description = new Input("description", InputType.DESCRIPTION, ScopeHandler.getInstance().load(this.request, "description", "parameter"));
                Input amount = new Input("amount", InputType.FLOAT, ScopeHandler.getInstance().load(this.request, "amount", "parameter"));
                Input date = new Input("date", InputType.DATE, ScopeHandler.getInstance().load(this.request, "date", "parameter"));
                Input category = new Input("category", InputType.INTEGER, ScopeHandler.getInstance().load(this.request, "category", "parameter"));

                try {
                    //Load input values from parameter scope and store them in an list of inputs.
                    ArrayList<Input> inputList = new ArrayList<>();
                    inputList.add(id);
                    inputList.add(title);
                    inputList.add(description);
                    inputList.add(amount);
                    inputList.add(date);
                    inputList.add(category);

                    HashMap<String, Object> validationParameters = new HashMap<>();
                    validationParameters.put("inputList", inputList);
                    validationParameters.put("context", ContextType.RECORD);

                    ModelComponentFactory.createModuleComponent(this.request, this.response, "SyntactialInputValidation")
                            .provideParameters(validationParameters)
                            .process();

                    if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                        //Syntactical validation successful
                        //Start semantic input validation
                        ModelComponentFactory.createModuleComponent(this.request, this.response, "SemanticInputValidation")
                                .provideParameters(validationParameters)
                                .process();

                        if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation") && ((int)id.getValue()) != 0) {
                            //Semantic validation successful
                            //Load category
                            HashMap<String, Object> params = new HashMap<>();
                            params.put("id", (int) category.getValue());
                            ModelComponentFactory.createModuleComponent(this.request, this.response, "LoadCategory")
                                    .provideParameters(params)
                                    .process();

                            Category categoryObject = (Category) ScopeHandler.getInstance().load(this.request, "category");

                            if (categoryObject != null) {

                                params.remove("id");
                                params.put("bsId", bs.getId());
                                params.put("recordId", id.getValue());
                                ModelComponentFactory.createModuleComponent(this.request, this.response, "LoadRecord")
                                    .provideParameters(params)
                                    .process();
                                
                                Record changedRecord = (Record)ScopeHandler.getInstance().load(this.request, "record");
                                changedRecord.setTitle(title.getValue().toString());
                                changedRecord.setDescription(description.getValue().toString());
                                changedRecord.setAmount((double) amount.getValue());
                                changedRecord.setRecordDate(new Date(((GregorianCalendar) date.getValue()).getTimeInMillis()));
                                changedRecord.setCategory(categoryObject);
                                
                                //Update in database
                                params.remove("bsId");
                                params.remove("recordId");
                                ArrayList<Record> changeRecords = new ArrayList<>();
                                changeRecords.add(changedRecord);
                                params.put("records", changeRecords);
                                
                                 ModelComponentFactory.createModuleComponent(this.request, this.response, "ChangeRecords")
                                    .provideParameters(params)
                                    .process();
                                 
                                 ScopeHandler.getInstance().store(this.request, "record", changedRecord);
                                 
                            } else {
                                //Could not load category
                                Logger.getLogger(Createrecord.class.getName()).log(Level.WARNING, "Could not load category!");
                            }
                        } else {
                            //Semantic validation failed
                            Logger.getLogger(Createrecord.class.getName()).log(Level.WARNING, "Semantic validation failed!");
                        }

                    } else {
                        //Syntactical validation failed
                        Logger.getLogger(Createrecord.class.getName()).log(Level.WARNING, "Syntactical validation failed!");
                    }

                } catch (Exception ex) {
                    //An error occured during the creation process.
                    Logger.getLogger(Createrecord.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //User and/or balance sheet not defined
                Logger.getLogger(Createrecord.class.getName()).log(Level.WARNING, "User and/or balance sheet not defined!");
            }
        }

        return this.viewPath + this.viewFile;
    }
    
}


