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
import database.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.components.auth.CheckUserCredentials;
import model.types.UserType;

/**
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public abstract class Command {

    /**
     * The request object of the request.
     */
    protected HttpServletRequest request;
    /**
     * The response object of the request.
     */
    protected HttpServletResponse response;

    /**
     * Flag to specify the command is available for html requests.
     */
    protected boolean htmlOutput;
    /**
     * Flag to specify whether the command is available for the client or not,
     * i.e. whether it has an XML view or not
     */
    protected boolean xmlOutput;

    /**
     * Flag to specify whether the command is available for ajax requests
     */
    protected boolean jsonOutput;

    /**
     * View path for the view file. It is either "/html" (default) or "/xml".
     */
    protected String viewPath;

    /**
     * Name of the view file
     */
    protected String viewFile;

    /**
     * The user type required to use the command.
     */
    protected UserType requiredUserType;
    
    /**
     * Constructs the command and sets the <code>xmlOutput</code> flag to false.
     * Therefore the xmlOutput is by default not available and has to be
     * activated explicitly by the specific commands. The view path is set to
     * "html" by default.
     *
     * @param request The incomming HTTP request.
     * @param response The outgoing HTTP response.
     */
    public Command(HttpServletRequest request, HttpServletResponse response) {
        //By default commands have a HTML output
        this.htmlOutput = true;
        //By default commands are not available for XML output
        this.xmlOutput = false;
        //By detault commands are not available for JSON output
        this.jsonOutput = false;
        //By default the html output is used
        this.viewPath = "/html";
        //By default the error message is loaded
        this.viewFile = "/error.jsp";
        //By default the required user type is <code>UserType.GUEST</code>.
        this.requiredUserType = UserType.GUEST;

        this.request = request;
        this.response = response;
    }

    /**
     * Execute the command.
     *
     * @return The name of the view that should be displayed.
     * @throws ServletException
     * @throws IOException
     */
    public abstract String execute() throws ServletException, IOException;

    /**
     * Checks whether the command has an HTML output.
     *
     * @return Returns true, if the command has an HTML output/view. Otherwise
     * false is returned.
     */
    public boolean hasHtmlOutput() {
        return this.htmlOutput;
    }
    
    /**
     * Checks whether the command has an XML Output respectively whether it is
     * available for the Java client.
     *
     * @return Returns true, if the command has an XML output/view. Otherwise
     * false is returned.
     */
    public boolean hasXmlOutput() {
        return this.xmlOutput;
    }

    /**
     * Checks whether the command has a JSON output for AJAX requests.
     *
     * @return Returns true, if the command has a JSON output. Otherwise false
     * is returned.
     */
    public boolean hasJsonOutput() {
        return this.jsonOutput;
    }

    /**
     * Sets the view path for the command.
     *
     * @param viewPath The view path with a leading slash ("/").
     */
    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }
    
    /**
     * Check whether the current user is allowed to access the command.
     * @return True if the user is allowed to access the command and false if not.
     */
    public boolean checkAccessRights() {
        if (this.requiredUserType == UserType.GUEST) {
            return true;
        } else {
            //Login required
            CheckUserCredentials cuc = new CheckUserCredentials(this.request, this.response);
            //Check login
            if (cuc.validateLogIn()) {
                User user = (User)ScopeHandler.getInstance().load(this.request, "user", "session");
                return user.getUserType().getId() >= this.requiredUserType.getId();
            } else {
                return false;
            }
        }
    }
}
