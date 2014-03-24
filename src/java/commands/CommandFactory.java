package commands;
import controller.URLProber;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class CommandFactory
{
    public static Command createCommand(HttpServletRequest request, 
                                        HttpServletResponse response)
    {
        //Initialise class loader
        ClassLoader classLoader = CommandFactory.class.getClassLoader();
        
        //Initialise command
        Command command = null;
        
        //Get URLProber object from request scope
        URLProber up = (URLProber)request.getAttribute("url");
        
        try {
            Class aCommand = classLoader.loadClass(up.getResourceLocation());
            Constructor commandConstructor = aCommand.getDeclaredConstructor(new Class[]{HttpServletRequest.class, HttpServletResponse.class});
            command = (Command)commandConstructor.newInstance(request, response);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
             Logger.getLogger(CommandFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return command;
    }
}