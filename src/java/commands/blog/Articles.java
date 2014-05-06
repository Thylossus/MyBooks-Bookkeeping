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

package commands.blog;

import commands.Command;
import controller.ScopeHandler;
import database.Article;
import database.DBFilter;
import database.SQLConstraintOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.types.UserType;

/**
 * Articles command
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Articles extends Command{

    /**
     * Constructs a Articles command using a request and a response object.
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Articles (HttpServletRequest request, HttpServletResponse response) {
        super(request, response);        
        this.viewFile = "/articles.jsp";
        this.requiredUserType = UserType.NEWS_WRITER;
    }
    
    /**
     * Executes the command and returns the location of the view.
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {        
    
        ScopeHandler.getInstance().store(this.request, "articles", Article.findAll());
        
        ScopeHandler.getInstance().store(this.request, "title", "Article Management");
        
        try {
            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
        } catch (Exception ex) {
            Logger.getLogger(Articles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.viewPath + this.viewFile;
    }
    
}


