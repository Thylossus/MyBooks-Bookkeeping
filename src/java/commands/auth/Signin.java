package commands.auth;

import commands.Command;
import controller.ScopeHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.components.Input;
import model.types.ContextType;
import model.types.InputType;

/**
 *
 * @author Tobias
 */
public class Signin extends Command{

    /**
     * Constructs a sign in commmand.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Signin(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.viewFile = "/home.jsp";
    }

    @Override
    public String execute() throws ServletException, IOException {
        Input firstname = new Input("firstname", InputType.NAME, ScopeHandler.getInstance().load(this.request, "firstname", "parameter"));
        Input lastname = new Input("lastname", InputType.NAME, ScopeHandler.getInstance().load(this.request, "lastname", "parameter"));
        Input mail = new Input("mail", InputType.MAIL, ScopeHandler.getInstance().load(this.request, "mail", "parameter"));
        Input password = new Input("password", InputType.PASSWORD, ScopeHandler.getInstance().load(this.request, "password", "parameter"));
        Input rePassword = new Input("re-password", InputType.PASSWORD, ScopeHandler.getInstance().load(this.request, "re-password", "parameter"));
        
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
            
            if((boolean)ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                //Semantic validation successful
                //Start semantic input validation
                ModelComponentFactory.createModuleComponent(this.request, this.response, "SemanticInputValidation")
                        .provideParameters(validationParameters)
                        .process();
                
                if((boolean)ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                    //Semantic validation successful
                    HashMap<String, Object> registrationParameters = new HashMap<>();
                    registrationParameters.put("firstname", firstname.getValue());
                    registrationParameters.put("lastname", lastname.getValue());
                    registrationParameters.put("mail", mail.getValue());
                    registrationParameters.put("password", password.getValue());
                    
                     ModelComponentFactory.createModuleComponent(this.request, this.response, "RegisterUser")
                            .provideParameters(registrationParameters)
                            .process();
                } else {
                    //Semantic validation failed
                }
                
            } else {
                //Semantic validation failed
            }
            
        } catch (Exception ex) {
            Logger.getLogger(Signin.class.getName()).log(Level.SEVERE, null, ex);
            //If an error occured, go back to the sign in page.
            this.viewFile = "/signin.jsp";
        }
        
        return this.viewPath + this.viewFile;
    }
    
}
