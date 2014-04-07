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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This command is exlusively used for handling errors occured in the 
 * command factory. It supports all output types (HTML, XML, JSON).
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version
 */
public class Error extends Command{

    private int errno;
    
    /**
     * Constructs an error command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Error (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);        
        this.viewFile = "/error.jsp";
        this.xmlOutput = true;
        this.jsonOutput = true;
        this.errno = 1;
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {   
        this.request.setAttribute("errno", this.errno);
        return this.viewPath + this.viewFile;
    }
    
    /**
     * Set the error number for the command. The error number specifies, which
     * error will be displayed in the error view.
     * @param errno An error number.
     */
    public void setErrno(int errno) {
        this.errno = errno;
    }
    
}
