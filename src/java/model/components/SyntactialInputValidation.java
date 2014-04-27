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
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;

/**
 * Validates user input to make sure it adheres to basic syntax rules.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class SyntactialInputValidation extends ModelComponent{

    /**
     * A list of inputs.
     */
    private ArrayList<Input> inputList;
    
    /**
     * Construct a SyntactialInputValidation object
     * @param request The request's request object
     * @param response The request's response object
     */
    public SyntactialInputValidation(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.inputList = null;
    }

    /**
     * Process the action for SyntactialInputValidation
     */
    @Override
    public void process() {
        if (this.inputList != null) {
            String tmpInput;
            
            //Start with assumption that all inputs are valid.
            ScopeHandler.getInstance().store(this.request, "inputValidation", true);
            
            for (Input input : inputList) {
                try {
                    tmpInput = this.generalValidation(input.getValue());
                    switch (input.getType()) {
                        case NAME: 
                            input.setValue(this.validateName(tmpInput));
                            break;
                        case DESCRIPTION:
                            input.setValue(this.validateDescription(tmpInput));
                            break;
                        case MAIL: 
                            input.setValue(this.validateMail(tmpInput));
                            break;
                        case INTEGER:
                            input.setValue(this.validateInteger(tmpInput));
                            break;
                        case FLOAT:
                            input.setValue(this.validateFloat(tmpInput));
                            break;
                        case DATE:
                            input.setValue(this.validateDate(tmpInput));
                            break;
                        case PASSWORD:
                            input.setValue(this.validatePassword(tmpInput));
                            break;
                        case COLOUR:
                            input.setValue(this.validateColour(tmpInput));
                            break;
                        default:
                            //Unsupported/unknown type
                            input.setValue(false);
                            ScopeHandler.getInstance().store(this.request, "inputValidation", false);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(SyntactialInputValidation.class.getName()).log(Level.SEVERE, null, ex);
                    //If an input has the value false, its input validation failed.
                    input.setValue(false);
                    //The validation failed.
                    ScopeHandler.getInstance().store(this.request, "inputValidation", false);
                }
            }
        } else {
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
        if(params.get("inputList") != null) {
            this.inputList = (ArrayList<Input>)params.get("inputList");
        }
        
        return this;
    }
    
    /**
     * Performs general validation (e.g. checking for an empty input) on input
     * and transforms input into a string for further validation.
     * @param input input object.
     * @return generally validated input as a string.
     * @throws Exception if input is syntactically invalid.
     */
    private String generalValidation(Object input) throws Exception{
        String validatedInput = input.toString();
        
        if (validatedInput.isEmpty()) {
            throw new Exception("Input invalid! (Empty input)");
        }
        
        return validatedInput;
    }
    
    /**
     * Validate an input as a name.
     * @param input The input as a string.
     * @return validated name as a string.
     * @throws Exception if input is syntactically invalid.
     */
    private String validateName(String input) throws Exception{
        return input;
    }
    
    /**
     * Validate an input as a description.
     * @param input the input as a string.
     * @return validated description as string.
     * @throws Exception if input is syntactically invalid.
     */
    private String validateDescription(String input) throws Exception{
        return input;
    }
    
    /**
     * Validate an input as an e-mail address.
     * @param input The input as a string.
     * @return validated e-mail address as a string.
     * @throws Exception if input is syntactically invalid.
     */
    private String validateMail(String input) throws Exception{
        return input;
    }
    
    /**
     * Validate an input as an integer number.
     * @param input The input as a string.
     * @return validated integer number.
     * @throws Exception if input is syntactically invalid.
     */
    private int validateInteger(String input) throws Exception{
        return Integer.parseInt(input);
    }
    
    /**
     * Validate an input as a floating point number.
     * @param input The input as a string.
     * @return validated floating point number as double.
     * @throws Exception if input is syntactically invalid.
     */
    private double validateFloat(String input) throws Exception{
        return Double.parseDouble(input);
    }
    
    /**
     * Validate an input as a date.
     * @param input The input as a string.
     * @return validated date as a <code>GregorianCalendar</code> object.
     * @throws Exception if input is syntactically invalid.
     */
    private GregorianCalendar validateDate(String input) throws Exception{
        String[] splittedInput = input.split("-");
        
        if (splittedInput.length == 3) {
            if (splittedInput[0].length() == 4) {
                if (splittedInput[1].length() == 2) {
                    if (splittedInput[2].length() == 2) {
                        int year = Integer.parseInt(splittedInput[0]);
                        int month = Integer.parseInt(splittedInput[1]);
                        int day = Integer.parseInt(splittedInput[2]);
                        
                        return new GregorianCalendar(year, month, day);
                    } else {
                        throw new Exception("The entered date does not adhere to the format yyyy-MM-dd. Day is not provided as dd.");
                    }
                } else {
                    throw new Exception("The entered date does not adhere to the format yyyy-MM-dd. Month is not provided as MM.");
                }
            } else {
                throw new Exception("The entered date does not adhere to the format yyyy-MM-dd. Year is not provided as yyyy.");
            }
        } else {
            throw new Exception("The entered date does not adhere to the format yyyy-MM-dd. Not enough or too many hyphens.");
        }
        
    }
    
    /**
     * Validate an input as a password.
     * @param input The input as a string.
     * @return validated password as a byte array containing the password's hash value.
     * @throws Exception if input is syntactically invalid.
     */
    private byte[] validatePassword(String input) throws Exception{
        //Do some validation
        
        //Hash
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());
        return md.digest();
    }

    /**
     * Validate an input as a colour.
     * @param input the input as a string.
     * @return validated colour as a string.
     * @throws Exception if input is syntactically invalid.
     */
    private String validateColour(String input) throws Exception{
        return "";
    }
    
}
