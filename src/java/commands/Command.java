package commands;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public abstract class Command
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    //Flag to specify whether the command is available for the client or not,
    //i.e. whether it has an XML view or not
    protected boolean xmlOutput;
    //View path for the view file. It is either "/html" (default) or "/xml".
    protected String viewPath;
    //Name of the view file
    protected String viewFile;
    
    /**
     * Constructs the command and sets the <code>xmlOutput</code> flag to false.
     * Therefore the xmlOutput is by default not available and has to be
     * activated explicitly by the specific commands. The view path is set
     * to "html" by default.
     * @param request The incomming HTTP request.
     * @param response The outgoing HTTP response.
     */
    public Command(HttpServletRequest request, HttpServletResponse response) {
        //By default commands are not available
        this.xmlOutput = false;
        //By default the html output is used
        this.viewPath = "/html";
        //By default the error message is loaded
        this.viewFile = "/error.jsp";
        
        this.request = request;
        this.response = response;
    }
    
    /**
     * Execute the command.
     * @return The name of the view that should be displayed.
     * @throws ServletException
     * @throws IOException 
     */
    public abstract String execute() throws ServletException, IOException;
    
    /**
     * Checks whether the command has an XML Output respectively whether it is
     * available for the Java client.
     * @return Returns true, if the command has an XML output/view. Otherwise false is returned.
     */
    public boolean hasXmlOutput() {
        return this.xmlOutput;
    }
}
