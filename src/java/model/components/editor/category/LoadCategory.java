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
package model.components.editor.category;

import controller.ScopeHandler;
import database.Category;
import database.DBFilter;
import database.SQLConstraintOperator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.ModelComponentFactory;

/**
 * Load a category's details.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class LoadCategory extends ModelComponent {

    /**
     * The category's id.
     */
    private int id;

    /**
     * Construct a LoadCategory object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public LoadCategory(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.id = 0;
    }

    /**
     * Process the action for LoadCategory
     */
    @Override
    public void process() {
        if (this.id != 0) {
            DBFilter filter = new DBFilter();
            filter.addConstraint(Category.CLMN_ID, SQLConstraintOperator.EQUAL, this.id);
            HashMap<String,Object> params = new HashMap<>();
            params.put("tableName", Category.SELECT_TABLE);
            params.put("dbFilter", filter);
            
            try {
                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                        .provideParameters(params)
                        .process();
                
                ArrayList<Category> categories = (ArrayList<Category>)ScopeHandler.getInstance().load(this.request, "dataList");
                
                if (categories != null && categories.size() == 1) {
                    //Found a single category
                    //Store category in request scope
                    ScopeHandler.getInstance().store(this.request, "category", categories.get(0));
                }
            } catch (Exception ex) {
                Logger.getLogger(LoadCategory.class.getName()).log(Level.SEVERE, "Could not load category.", ex);
            }
        }
    }

    /**
     * Provide parameters to the model component.
     *
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for
     * concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if (params.get("id") != null) {
            this.id = (int) params.get("id");
        }

        return this;
    }

}
