package commands.auth;

import commands.Command;
import controller.ScopeHandler;
import database.User;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.components.auth.CheckUserCredentials;

/**
 * Signout command.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Signout extends Command{
    /**
     * Constructs a sign in command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Signout (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);        
        ScopeHandler.getInstance().store(request, "title", "Home");
        this.viewFile = "/home.jsp";
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {    
        CheckUserCredentials cuc = new CheckUserCredentials(this.request, this.response);
        User user = (User)ScopeHandler.getInstance().load(this.request, "user", "session");
        
        //Check whether a user is stored in the session
        if (user != null) {
            //Check if the user in the session has a valid login
            if (cuc.validateLogIn()) {
                //Update last sign in date
                user.setLastSignInDate(new Timestamp(new GregorianCalendar().getTimeInMillis()));
                user.update();
            }
            
            //Remove user from session
            ScopeHandler.getInstance().remove(this.request, "user", "session");
        }
        
        this.viewPath = "/MyBooks-Bookkeeping";
        this.viewFile = "/home";
        
        return this.viewPath + this.viewFile;
        
    }
}
