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

package controller;

import java.util.regex.Pattern;

/**
 * This class is a preprocessor for incomming requests. It scans the requested
 * URL/URI and provides the locations of requested resources in the appropriated
 * format for <code>Controller</code>, <code>CommandFactory</code>,and others.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class URLProber {

    private boolean isCommand;
    private String resourceLocation;
    
    /**
     * Create a URLProber object to preprocess the URL of the incomming request.
     * @param url The incomming request's URL
     */
    public URLProber(String url) {
        if (Pattern.matches(".*[.].*", url)) {
            this.isCommand = false;
            this.resourceLocation = url;
        } else {
            this.isCommand = true;
            this.resourceLocation = this.getCommandResourceLocation(url);
        }
    }
    
    /**
     * Build the resource identifier of a class by using the provided URL, i.e.
     * transform the URL to an URI.
     * @param url The incomming request's URL
     * @return A class' URI
     */
    private String getCommandResourceLocation (String url) {
        //Remove unnecessary command location ("MyBooks-Bookkeeping/website")
        //and transform to lower case
        url = url.replaceFirst("/MyBooks-Bookkeeping/", "")
                 .toLowerCase();

        //Check whether URL is empty
        if (!url.isEmpty()) {
            
            //Separate URL components to modify class part
            String urlComponents[] = url.split("/");
            //Change first letter of class to upper case to match command naming
            //convention
            urlComponents[urlComponents.length-1] = 
                    urlComponents[urlComponents.length-1]
                            .substring(0, 1)
                            .toUpperCase()
                    + urlComponents[urlComponents.length-1].substring(1);

            //Build translated URI
            url = "commands";
            for(String urlComponent : urlComponents) {
                url += "." + urlComponent;
            }
            
        } else {
            url = "commands.Home";
        }
        
        return url;
    }
    
    /**
     * Check whether the probed URL was a command or not.
     * @return Returns true if the probed URL was a command or false otherwise
     */
    public boolean isCommand() {
        return this.isCommand;
    }
    
    /**
     * Provide the processed resource location.
     * @return The resources location as an URL or an URI (for commands).
     */
    public String getResourceLocation() {
        return this.resourceLocation;
    }
    
}
