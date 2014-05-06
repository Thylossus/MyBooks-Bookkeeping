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
package model.components.editor.record;

import database.Category;
import database.DBFilter;
import database.Record;
import database.SQLConstraintOperator;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;

/**
 * Takes a list of records and deletes their corresponding database records.
 * Deleting a single record is also possible.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class DeleteRecords extends ModelComponent {

    /**
     * Balance sheet id.
     */
    private int bsId;
    /**
     * Record id.
     */
    private int recordId;
    /**
     * Category
     */
    private Category category;

    /**
     * Construct a DeleteRecords object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public DeleteRecords(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.bsId = 0;
        this.recordId = 0;
        this.category = null;
    }

    /**
     * Process the action for DeleteRecords
     */
    @Override
    public void process() {
        DBFilter filter = null;

        if (this.bsId != 0) {
            filter = new DBFilter();
            filter.addConstraint(Record.CLMN_BALANCE_SHEET, SQLConstraintOperator.EQUAL, this.bsId);
            if (this.recordId != 0) {
                filter.addConstraint(Record.CLMN_ID, SQLConstraintOperator.EQUAL, this.recordId);
            }
        } else if (this.category != null && !this.category.isSystemCategory()) {
            filter = new DBFilter();
            filter.addConstraint(Record.CLMN_CAT_ID, SQLConstraintOperator.EQUAL, this.category.getId());
        }

        if (filter != null) {
            ArrayList<Record> records = Record.findAll(filter);
            for (Record record : records) {
                record.delete();
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
        if (params.get("bsId") != null) {
            this.bsId = (int) params.get("bsId");
        }
        if (params.get("recordId") != null) {
            this.recordId = (int) params.get("recordId");
        }
        if (params.get("category") != null) {
            this.category = (Category) params.get("category");
        }
        return this;
    }

}
