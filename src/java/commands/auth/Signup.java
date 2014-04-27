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
 * Sign up a new customer.
 *
 * @author Tobias
 */
public class Signup extends Command {

    /**
     * Constructs a sign up commmand.
     *
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Signup(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.viewFile = "/home.jsp";
        ScopeHandler.getInstance().store(request, "title", "Home");
    }

    /**
     * Execute the sign up command.
     *
     * @return path to the view, which shall be displayed.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException if a I/O error occurs.
     */
    @Override
    public String execute() throws ServletException, IOException {        
        //Check whether the form was submitted or not.
        if (ScopeHandler.getInstance().load(this.request, "submit", "parameter") != null) {
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

                if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                //Semantic validation successful
                    //Start semantic input validation
                    ModelComponentFactory.createModuleComponent(this.request, this.response, "SemanticInputValidation")
                            .provideParameters(validationParameters)
                            .process();

                    if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                        //Semantic validation successful
                        HashMap<String, Object> registrationParameters = new HashMap<>();
                        registrationParameters.put("firstname", firstname.getValue());
                        registrationParameters.put("lastname", lastname.getValue());
                        registrationParameters.put("mail", mail.getValue());
                        registrationParameters.put("password", password.getValue());

                        ModelComponentFactory.createModuleComponent(this.request, this.response, "RegisterUser")
                                .provideParameters(registrationParameters)
                                .process();
                        
                        //Check whether user has been stored in the session scope
                        if (ScopeHandler.getInstance().load(this.request, "user", "session") != null) {
                            //Redirect to home
                            this.viewPath = "/MyBooks-Bookkeeping";
                            this.viewFile = "/home";
                        } else {
                            //User object is not stored in session.
                            Logger.getLogger(Signup.class.getName()).log(Level.WARNING, "User is not stored in session!");
                        }
                    } else {
                        //Semantic validation failed
                        Logger.getLogger(Signup.class.getName()).log(Level.WARNING, "Semantic validation failed!");
                    }

                } else {
                    //Syntactical validation failed
                    Logger.getLogger(Signup.class.getName()).log(Level.WARNING, "Syntactical validation failed!");
                }

            } catch (Exception ex) {
                Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
                //If an error occured, go back to the sign in page.
                this.viewFile = "/signup.jsp";
                ScopeHandler.getInstance().store(request, "title", "Sign up");
            }
        } else {
            ScopeHandler.getInstance().store(request, "title", "Sign up");
            this.viewFile = "/signup.jsp";
        }
        
        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
        } catch (Exception ex) {
            Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.viewPath + this.viewFile;
    }

}
