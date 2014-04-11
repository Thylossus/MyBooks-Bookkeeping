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

package model.components;


import controller.ScopeHandler;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.forms.*;
import model.types.ContextType;

/**
 * Validate the user input to make sure it adheres to special rules for different contexts/forms.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class SemanticInputValidation extends ModelComponent{

    /**
     * Type of the context in which the validation takes place.
     */
    ContextType context;
    /**
     * A list of inputs to validate. These inputs have already been validated 
     * in terms of their syntax.
     */
    ArrayList<Input> inputList;
    
    /**
     * Construct a SemanticInputValidation object
     * @param request The request's request object
     * @param response The request's response object
     */
    public SemanticInputValidation(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.inputList = null;
        this.context = null;
    }
    
    /**
     * Process the action for SemanticInputValidation
     */
    @Override
    public void process() {
        Context validationContext;
        if(this.inputList != null && this.context != null) {
            switch (this.context) {
                case ARTICLE:
                    validationContext = new Article(this.inputList);
                    break;
                case BALANCE_SHEET:
                    validationContext = new BalanceSheet(this.inputList);
                    break;
                case CATEGORY:
                    validationContext = new Category(this.inputList);
                    break;
                case EDIT_UPLOADED_FILE:
                    validationContext = new EditUploadedFile(this.inputList);
                    break;
                case PASSWORD_RESET:
                    validationContext = new PasswordReset(this.inputList);
                    break;
                case PREMIUM_MEMBERSHIP:
                    validationContext = new PremiumMembership(this.inputList);
                    break;
                case RECORD:
                    validationContext = new Record(this.inputList);
                    break;
                case SIGN_IN:
                    validationContext = new SignIn(this.inputList);
                    break;
                case UPLOAD_FILE:
                    validationContext = new UploadFile(this.inputList);
                    break;
                case USER_DETAILS:
                    validationContext = new UserDetails(this.inputList);
                    break;
                default:
                    //Context undefined
                    validationContext = null;
            }
            
            if (validationContext != null) {
                if (validationContext.validate()) {
                    //Validation successful
                    ScopeHandler.getInstance().store(this.request, "inputValidation", true);
                } else {
                    //Validation failed
                    ScopeHandler.getInstance().store(this.request, "inputValidation", false);
                }
            } else {
                //Context undefined
                ScopeHandler.getInstance().store(this.request, "inputValidation", false);
            }
                    
        } else {
            //Either the list of inputs, the context type or both are not set.
            ScopeHandler.getInstance().store(this.request, "inputValidation", false);
        }
    }
    
    /**
     * Provide parameters to the model component.
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if(params.get("context") != null) {
            this.context = (ContextType)params.get("context");
        }
        
        if(params.get("inputList") != null) {
            this.inputList = (ArrayList<Input>)params.get("inputList");
        }
        
        return this;
    }

}
