package commands.auth;

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

/**
 * Sign in command
 * @author Tobias
 */
public class Signin extends Command{
    /**
     * Constructs a sign in command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Signin (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);        
        ScopeHandler.getInstance().store(request, "title", "Sign in");
        this.viewFile = "/signin.jsp";
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {    
        
        //Check whether the form was submitted or not.
        if (ScopeHandler.getInstance().load(this.request, "submit", "parameter") != null) {
            Input mail = new Input("mail", InputType.MAIL, ScopeHandler.getInstance().load(this.request, "mail", "parameter"));
            Input password = new Input("password", InputType.PASSWORD, ScopeHandler.getInstance().load(this.request, "password", "parameter"));

            try {
                //Load input values from parameter scope and store them in an list of inputs.
                ArrayList<Input> inputList = new ArrayList<>();
                inputList.add(mail);
                inputList.add(password);

                HashMap<String, Object> validationParameters = new HashMap<>();
                validationParameters.put("inputList", inputList);
                validationParameters.put("context", ContextType.SIGN_IN);

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
                        HashMap<String, Object> credentialCheckParameters = new HashMap<>();
                        credentialCheckParameters.put("mail", mail.getValue());
                        credentialCheckParameters.put("password", password.getValue());

                        ModelComponentFactory.createModuleComponent(this.request, this.response, "CheckUserCredentials")
                                .provideParameters(credentialCheckParameters)
                                .process();
                        
                        if ((boolean) ScopeHandler.getInstance().load(this.request, "inputValidation")) {
                            //User credentials correct
                            User user = (User)ScopeHandler.getInstance().load(request, "user");
                            if (user != null) {
                                //Remove user from request scope...
                                ScopeHandler.getInstance().remove(request, "user");
                                //... and store the user in the session scope
                                ScopeHandler.getInstance().store(request, "user", user, "session");
                                
                                //Redirect to the corresponding view.
                                this.viewPath = "/MyBooks-Bookkeeping";
                                String title;        
                                
                                switch(user.getUserType()) {
                                    case ADMINISTRATOR:
                                        this.viewFile = "/sysmgmt/statistics";
                                        title = "System Statistics";
                                        break;
                                    case NEWS_WRITER:
                                        this.viewFile = "/blog/articles";
                                        title = "Articles";
                                        break;
                                    case PREMIUM_USER:
                                        this.viewFile = "/bsm/balancesheets";
                                        title = "Balance Sheets";
                                        break;
                                    case STANDARD_USER:
                                        this.viewFile = "/bsm/balancesheets";
                                        title = "Balance Sheets";
                                        break;
                                    default:
                                        this.viewFile = "/home";
                                        title = "Home";
                                }
                                
                                ScopeHandler.getInstance().store(request, "title", title);
                            } else {
                                //User could not be loaded
                                Logger.getLogger(Signup.class.getName()).log(Level.WARNING, "Could not load user from request scope!");
                            }
                        } else {
                            //User credentials incorrect
                            Logger.getLogger(Signup.class.getName()).log(Level.WARNING, "Checking user credentials failed!");
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
                //An error occured during the login process.
                Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
        } catch (Exception ex) {
            Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.viewPath + this.viewFile;
    }
    
}
