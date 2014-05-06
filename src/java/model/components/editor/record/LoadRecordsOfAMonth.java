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

import beans.Date;
import controller.ScopeHandler;
import database.BalanceSheet;
import database.DBFilter;
import database.Record;
import database.SQLConstraintOperator;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.ModelComponentFactory;

/**
 * Load the records of a month from the balance sheet in the session.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class LoadRecordsOfAMonth extends ModelComponent {

    /**
     * A date representing the month which defines what records to load.
     */
    private Date month;

    /**
     * Construct a LoadRecordsOfAMonth object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public LoadRecordsOfAMonth(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.month = new Date();
    }

    /**
     * Process the action for LoadRecordsOfAMonth
     */
    @Override
    public void process() {
        BalanceSheet bs = (BalanceSheet) ScopeHandler.getInstance().load(this.request, "balanceSheet", "session");

        if (bs != null && this.month != null) {
            DBFilter filter = new DBFilter();
            Date firstDayOfMonth = new Date();
            Date lastDayOfMonth = new Date();
            firstDayOfMonth.setFormat("yyyy-MM-dd");
            lastDayOfMonth.setFormat("yyyy-MM-dd");

            GregorianCalendar gcFDOM = new GregorianCalendar(this.month.getYear(), this.month.getMonth(), month.getCalendar().getActualMinimum(GregorianCalendar.DAY_OF_MONTH));
            GregorianCalendar gcLDOM = new GregorianCalendar(this.month.getYear(), this.month.getMonth(), month.getCalendar().getActualMaximum(GregorianCalendar.DAY_OF_MONTH));

            firstDayOfMonth.setCalendar(gcFDOM);
            lastDayOfMonth.setCalendar(gcLDOM);

            ArrayList<String> months = new ArrayList<>();
            months.add(firstDayOfMonth.toString());
            months.add(lastDayOfMonth.toString());

            filter.addConstraint(Record.CLMN_RECORD_DATE, SQLConstraintOperator.BETWEEN, months);
            filter.addConstraint(Record.CLMN_BALANCE_SHEET, SQLConstraintOperator.EQUAL, bs.getId());

            HashMap<String, Object> dataParams = new HashMap<>();
            dataParams.put("tableName", Record.SELECT_TABLE);
            dataParams.put("dbFilter", filter);
            dataParams.put("orderByColumns", new String[]{Record.CLMN_RECORD_DATE + " DESC", Record.CLMN_ID});
            try {
                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                        .provideParameters(dataParams)
                        .process();

                ArrayList<Record> records = (ArrayList<Record>) ScopeHandler.getInstance().load(this.request, "dataList");

                if (records != null) {
                    ScopeHandler.getInstance().store(this.request, "records", records);
                    ScopeHandler.getInstance().store(this.request, "numberOfRecords", records.size());
                } else {
                    //Ensure that request attribute "records" is set to prevent null pointer exception
                    ScopeHandler.getInstance().store(this.request, "records", new ArrayList<Record>());
                    ScopeHandler.getInstance().store(this.request, "numberOfRecords", 0);
                }
            } catch (Exception ex) {
                Logger.getLogger(LoadRecordsOfAMonth.class.getName()).log(Level.SEVERE, "Could not load records of month " + this.month, ex);
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
        if (params.get("month") != null) {
            this.month = (Date) params.get("month");
        }

        return this;
    }

}
