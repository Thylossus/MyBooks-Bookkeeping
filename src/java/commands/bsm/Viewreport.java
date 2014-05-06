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
package commands.bsm;

import beans.Date;
import commands.Command;
import commands.Home;
import controller.ScopeHandler;
import database.BalanceSheet;
import database.DBFilter;
import database.Record;
import database.SQLConstraintOperator;
import database.User;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponentFactory;
import model.types.UserType;

/**
 * Viewreport command
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Viewreport extends Command {

    /**
     * A list of supported reports.
     */
    private static final String[] supportedReports = new String[]{
        "MonthOverview",
        "YearOverview",
        "Categories",
        "DevelopmentOfCosts",
        "TopTenExpenses",
        "TopTenIncomes"
    };

    /**
     * An index which indicates the position in the list of supported reports
     * where the extended reports start.
     */
    private static final int extendedReportsStart = 3;

    /**
     * Constructs a Viewreport command using a request and a response object.
     *
     * @param request The request object of the request.
     * @param response The response object of the request.
     */
    public Viewreport(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        this.requiredUserType = UserType.STANDARD_USER;
        this.viewFile = "/report.jsp";
    }

    /**
     * Executes the command and returns the location of the view.
     *
     * @return The relative location of the view's JSP file.
     */
    @Override
    public String execute() {
        BalanceSheet balanceSheet = (BalanceSheet) ScopeHandler.getInstance().load(this.request, "balanceSheet", "session");
        User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");

        if (balanceSheet != null && user != null) {

            String requestedReport = (String) ScopeHandler.getInstance().load(this.request, "report");

            if (requestedReport != null) {

                //Check if the requested report is supported
                boolean supported = false;
                boolean extendedReport = false;
                for (int i = 0; i < Viewreport.supportedReports.length && !supported; i++) {
                    if (Viewreport.supportedReports[i].equalsIgnoreCase(requestedReport)) {
                        supported = true;
                    }
                    if (i >= Viewreport.extendedReportsStart) {
                        extendedReport = true;
                    }
                }

                //Check if the requested report is supported and its either a basic report or the user is a premium user.
                if (supported && (!extendedReport || (user.getUserType().equals(UserType.PREMIUM_USER)))) {

                    DBFilter filter = null;
                    String[] orderByColumns = null;
                    HashMap<String, Object> params;
                    ArrayList<Record> records;
                    boolean dataLoaded = false;
                    int limitResults = -1;

                    try {
                        switch (requestedReport.toLowerCase()) {
                            case "monthoverview":
                                
                                String monthOverviewYearString = (String)ScopeHandler.getInstance().load(this.request, "year");
                                String monthOverviewMonthString = (String)ScopeHandler.getInstance().load(this.request, "month");
                                HashMap<String,Object> monthParam = new HashMap<>();
                                
                                if (monthOverviewYearString != null && monthOverviewMonthString != null) {
                                    try {
                                        int year = Integer.parseInt(monthOverviewYearString);
                                        int month  = Integer.parseInt(monthOverviewMonthString);
                                        if (month >= 1 && month <= 12) {
                                        Date monthDate = new Date();
                                        GregorianCalendar monthGC = new GregorianCalendar(year, month-1, 1);
                                        monthDate.setCalendar(monthGC);
                                        ScopeHandler.getInstance().store(this.request, "selectedMonth", monthGC);
                                        monthParam.put("month", monthDate);
                                        }
                                    } catch (NumberFormatException ex) {
                                        Logger.getLogger(Viewreport.class.getName()).log(Level.WARNING, "Could not parse given year and month!", ex);
                                    }
                                }

                                ModelComponentFactory.createModuleComponent(this.request, this.response, "LoadRecordsOfAMonth")
                                        .provideParameters(monthParam)
                                        .process();
                                if (ScopeHandler.getInstance().load(this.request, "records") != null) {
                                    dataLoaded = true;
                                    records = (ArrayList<Record>) ScopeHandler.getInstance().load(this.request, "records");

                                    HashMap<Integer, Double> dayOverallAverage = new HashMap<>();
                                    HashMap<Integer, Double> dayIncomeAverage = new HashMap<>();
                                    HashMap<Integer, Double> dayExpenseAverage = new HashMap<>();
                                    Double rAmount;
                                    Date rDate = new Date();

                                    for (Record record : records) {
                                        rAmount = record.getAmount();
                                        rDate.setCalendar(record.getRecordDate());
                                        if (dayOverallAverage.get(rDate.getDay()) == null) {
                                            dayOverallAverage.put(rDate.getDay(), 0.0);
                                            dayIncomeAverage.put(rDate.getDay(), 0.0);
                                            dayExpenseAverage.put(rDate.getDay(), 0.0);
                                        }
                                        dayOverallAverage.put(rDate.getDay(), dayOverallAverage.get(rDate.getDay()) + rAmount);

                                        if (rAmount >= 0.0) {
                                            dayIncomeAverage.put(rDate.getDay(), dayIncomeAverage.get(rDate.getDay()) + rAmount);
                                        } else {
                                            dayExpenseAverage.put(rDate.getDay(), dayExpenseAverage.get(rDate.getDay()) + rAmount);
                                        }
                                    }

                                    ScopeHandler.getInstance().store(this.request, "dayOverallAverage", dayOverallAverage);
                                    ScopeHandler.getInstance().store(this.request, "dayIncomeAverage", dayIncomeAverage);
                                    ScopeHandler.getInstance().store(this.request, "dayExpenseAverage", dayExpenseAverage);

                                } else {
                                    this.viewPath = "/MyBooks-Bookkeeping";
                                    this.viewFile = "/bsm/balancesheets";
                                }

                                ScopeHandler.getInstance().store(this.request, "lastDayOfMonth", GregorianCalendar.getInstance().getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
                                ScopeHandler.getInstance().store(this.request, "title", "Month Overview");
                                break;
                            case "yearoverview":
                                GregorianCalendar gcFirstDayOfTheYear = new GregorianCalendar();
                                GregorianCalendar gcLastDayOfTheYear = new GregorianCalendar();

                                String yearOverviewYearString = (String)ScopeHandler.getInstance().load(this.request, "year");
                                if (yearOverviewYearString != null && yearOverviewYearString.length() == 4) {
                                    try {
                                        int year = Integer.parseInt(yearOverviewYearString);
                                        gcFirstDayOfTheYear.set(GregorianCalendar.YEAR, year);
                                        gcLastDayOfTheYear.set(GregorianCalendar.YEAR, year);
                                        ScopeHandler.getInstance().store(this.request, "selectedYear", gcFirstDayOfTheYear);
                                    } catch (NumberFormatException ex) {
                                        Logger.getLogger(Viewreport.class.getName()).log(Level.WARNING, "Could not parse given year!", ex);
                                    }
                                }
                                
                                gcFirstDayOfTheYear.set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY);
                                gcFirstDayOfTheYear.set(GregorianCalendar.DAY_OF_MONTH, 1);

                                gcLastDayOfTheYear.set(GregorianCalendar.MONTH, GregorianCalendar.DECEMBER);
                                gcLastDayOfTheYear.set(GregorianCalendar.DAY_OF_MONTH, 31);

                                Date dFirstDayOfTheYear = new Date();
                                Date dLastDayOfTheYear = new Date();
                                dFirstDayOfTheYear.setCalendar(gcFirstDayOfTheYear);
                                dFirstDayOfTheYear.setFormat("yyyy-MM-dd");
                                dLastDayOfTheYear.setCalendar(gcLastDayOfTheYear);
                                dLastDayOfTheYear.setFormat("yyyy-MM-dd");

                                filter = new DBFilter();

                                ArrayList<String> months = new ArrayList<>();
                                months.add(dFirstDayOfTheYear.toString());
                                months.add(dLastDayOfTheYear.toString());

                                filter.addConstraint(Record.CLMN_RECORD_DATE, SQLConstraintOperator.BETWEEN, months);
                                filter.addConstraint(Record.CLMN_BALANCE_SHEET, SQLConstraintOperator.EQUAL, balanceSheet.getId());
                                orderByColumns = new String[]{Record.CLMN_RECORD_DATE};

                                params = new HashMap<>();

                                params.put("tableName", Record.SELECT_TABLE);
                                params.put("dbFilter", filter);
                                params.put("orderByColumns", orderByColumns);

                                ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                                        .provideParameters(params)
                                        .process();

                                records = (ArrayList<Record>) ScopeHandler.getInstance().load(this.request, "dataList");

                                if (records == null) {
                                    records = new ArrayList<>();
                                }
                                dataLoaded = true;

                                double[] monthOverallAverage = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                                double[] monthIncomeAverage = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
                                double[] monthExpenseAverage = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

                                double rAmount;
                                Date rDate = new Date();

                                for (Record record : records) {
                                    rAmount = record.getAmount();
                                    rDate.setCalendar(record.getRecordDate());
                                    
                                    monthOverallAverage[rDate.getMonth()] += rAmount;
                                    if (rAmount >= 0.0) {
                                        monthIncomeAverage[rDate.getMonth()] += rAmount;
                                    } else {
                                        monthExpenseAverage[rDate.getMonth()] += rAmount;
                                    }
                                }

                                ScopeHandler.getInstance().store(this.request, "monthOverallAverage", monthOverallAverage);
                                ScopeHandler.getInstance().store(this.request, "monthIncomeAverage", monthIncomeAverage);
                                ScopeHandler.getInstance().store(this.request, "monthExpenseAverage", monthExpenseAverage);
                                ScopeHandler.getInstance().store(this.request, "title", "Year Overview");
                                break;
                            case "categories":
                                orderByColumns = new String[]{Record.CLMN_CAT_ID};
                                ScopeHandler.getInstance().store(this.request, "title", "Categories");
                                break;
                            case "developmentofcosts":
                                ScopeHandler.getInstance().store(this.request, "title", "Development of Costs");
                                break;
                            case "toptenexpenses":
                                filter = new DBFilter();
                                filter.addConstraint(Record.CLMN_AMOUNT, SQLConstraintOperator.LESS_THAN, 0);
                                orderByColumns = new String[]{Record.CLMN_AMOUNT};
                                ScopeHandler.getInstance().store(this.request, "title", "Top 10 Expenses");
                                limitResults = 10;
                                break;
                            case "toptenincomes":
                                filter = new DBFilter();
                                filter.addConstraint(Record.CLMN_AMOUNT, SQLConstraintOperator.GREATER_EQUAL, 0);
                                orderByColumns = new String[]{Record.CLMN_AMOUNT + " DESC"};
                                ScopeHandler.getInstance().store(this.request, "title", "Top 10 Income");
                                limitResults = 10;
                                break;
                            default:
                                this.viewPath = "/MyBooks-Bookkeeping";
                                this.viewFile = "/bsm/balancesheets";
                        }

                        if (!dataLoaded) {

                            params = new HashMap<>();

                            params.put("tableName", Record.SELECT_TABLE);
                            if (filter != null) {
                                params.put("dbFilter", filter);
                            }
                            if (orderByColumns != null) {
                                params.put("orderByColumns", orderByColumns);
                            }

                            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                                    .provideParameters(params)
                                    .process();

                            records = (ArrayList<Record>) ScopeHandler.getInstance().load(this.request, "dataList");

                            if (records == null) {
                                records = new ArrayList<>();
                            }
                            
                            if (limitResults != -1) {
                                for (int i = limitResults; i < records.size(); i++) {
                                    records.remove(limitResults);
                                }
                            }

                            ScopeHandler.getInstance().store(this.request, "records", records);
                            ScopeHandler.getInstance().store(this.request, "numberOfRecords", records.size());
                        }

                        ScopeHandler.getInstance().store(this.request, "report", requestedReport.toLowerCase());

                        try {
                            ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateMainMenu").process();
                        } catch (Exception ex) {
                            Logger.getLogger(Home.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(Viewreport.class.getName()).log(Level.SEVERE, null, ex);
                        this.viewPath = "/MyBooks-Bookkeeping";
                        this.viewFile = "/bsm/balancesheets";
                    }
                } else {
                    this.viewPath = "/MyBooks-Bookkeeping";
                    this.viewFile = "/bsm/balancesheets";
                }
            } else {
                this.viewPath = "/MyBooks-Bookkeeping";
                this.viewFile = "/bsm/balancesheets";
            }
        } else {
            this.viewPath = "/MyBooks-Bookkeeping";
            if (user != null) {
                this.viewFile = "/bsm/balancesheets";
            } else {
                this.viewFile = "/home";
            }
        }

        return this.viewPath + this.viewFile;
    }

}
