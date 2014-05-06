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
package commands;

import controller.ScopeHandler;
import controller.URLProber;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The command factory provides commands based on the URI that is generated by
 * <code>URLProber</code> using Java's class loading features.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public abstract class CommandFactory {

    /**
     * Create a command based on an URI and pass the request and response object
     * to the created command. If the command creation fails, an error command
     * is created.
     *
     * @param request The request's request object.
     * @param response The request's response object.
     * @return A command that can be executed.
     */
    public static Command createCommand(HttpServletRequest request, HttpServletResponse response) {
        //Initialise class loader
        ClassLoader classLoader = CommandFactory.class.getClassLoader();

        //Initialise command
        Command command;

        //Get URLProber object from request scope
        URLProber up = (URLProber) request.getAttribute("url");
        
        //Set up parameters from url
        HashMap<String, String> params = up.getParams();
        //A list of parameters that cannot be set by the url because they are reserved by the system
        String[] prohibitedParameterKeys = new String[]{"menu", "title", "inputValidation", "dataList", "dispatch", "url"};
        Arrays.sort(prohibitedParameterKeys);
        if (!params.isEmpty()) {
            for(String key : params.keySet()) {
                //Check if key is prohibited
                if (Arrays.binarySearch(prohibitedParameterKeys, key) < 0) {
                    ScopeHandler.getInstance().store(request, key, params.get(key));
                }
            }
        }

        //Initialise view path
        String viewPath = "/html";

        try {
            Class aCommand = classLoader.loadClass(up.getResourceLocation());
            Constructor commandConstructor = aCommand.getDeclaredConstructor(new Class[]{HttpServletRequest.class, HttpServletResponse.class});
            command = (Command) commandConstructor.newInstance(request, response);

            //Authorise
            if (command.checkAccessRights()) {

                //Detect requested output (either HTML, XML or JSON)
                //Check whether the request came from the Java client (its user-agent is "MyBooks-Client") or the web application.
                if (request.getHeader("User-Agent").equals("MyBooks-Client")) {
                    //Java client
                    if (!command.hasXmlOutput()) {
                        command = new Error(request, response);
                        ((Error) command).setErrno(2);
                    }

                    viewPath = "/xml";

                } else {
                    //Web application
                    //Check whether the request is a AJAX call that requires JSON output
                    if (request.getHeader("X-Requested-With") != null
                            && request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
                        //Asynchronous (AJAX) request
                        if (!command.hasJsonOutput()) {
                            command = new Error(request, response);
                            ((Error) command).setErrno(2);
                        }

                        viewPath = "/json";
                    } else {
                        //Synchronous request
                        if (!command.hasHtmlOutput()) {
                            command = new Error(request, response);
                            ((Error) command).setErrno(2);
                        }
                    }
                }
            } else {
                //Access denied
                command = new Error(request, response);
                ((Error) command).setErrno(4);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.SEVERE, null, ex);
            command = new Error(request, response);
            ((Error) command).setErrno(3);
        }

        //Set correct view path
        command.setViewPath(viewPath);

        return command;
    }
}
