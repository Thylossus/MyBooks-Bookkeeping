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
package model.components.user;

import beans.Date;
import controller.ScopeHandler;
import database.DBFilter;
import database.PremiumMembership;
import database.SQLConstraintOperator;
import database.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.ModelComponentFactory;
import model.types.UserType;

/**
 * Checks whether a premium user has exceeded his premium membership. This is a
 * part of the login process (/FR40/ Sign in). Returns the latest expiration
 * date of all premium memberships of a user.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class CheckPremiumMembership extends ModelComponent {

    /**
     * Construct a CheckPremiumMembership object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public CheckPremiumMembership(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for CheckPremiumMembership
     */
    @Override
    public void process() {
        User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");
        Date lastExpirationDate = new Date();
        lastExpirationDate.setFormat("yyyy-MM-dd");
        boolean expired = true;

        if (user != null) {
            if (user.getUserType().equals(UserType.PREMIUM_USER)) {
                DBFilter filter = new DBFilter();
                filter.addConstraint(PremiumMembership.CLMN_USER_ID, SQLConstraintOperator.EQUAL, user.getId());
                filter.addConstraint(PremiumMembership.CLMN_ACTIVE, SQLConstraintOperator.EQUAL, true);

                HashMap<String, Object> params = new HashMap<>();
                params.put("tableName", PremiumMembership.SELECT_TABLE);
                params.put("dbFilter", filter);
                params.put("orderByColumns", new String[]{PremiumMembership.CLMN_END_DATE});

                try {
                    ModelComponentFactory.createModuleComponent(this.request, this.response, "CreateDataList")
                            .provideParameters(params)
                            .process();

                    ArrayList<PremiumMembership> premiumMemberships = (ArrayList<PremiumMembership>) ScopeHandler.getInstance().load(this.request, "dataList");
                    if (premiumMemberships != null && !premiumMemberships.isEmpty()) {
                        Date endDate = new Date();
                        Date today = new Date();
                        for (PremiumMembership pm : premiumMemberships) {
                            endDate.setCalendar(pm.getEndDate());
                            if (endDate.getCalendar().before(today.getCalendar())) {
                                pm.setActive(false);
                                pm.update();
                            }
                        }

                        if (premiumMemberships.get(premiumMemberships.size() - 1).isActive()) {
                            lastExpirationDate.setCalendar(premiumMemberships.get(premiumMemberships.size() - 1).getEndDate());
                            expired = false;
                        } else {
                            user.setUserType(UserType.STANDARD_USER);
                            user.update();
                        }

                    } else {
                        user.setUserType(UserType.STANDARD_USER);
                        user.update();
                    }

                    ScopeHandler.getInstance().store(this.request, "expired", expired);
                    ScopeHandler.getInstance().store(this.request, "lastExpirationDate", lastExpirationDate);
                } catch (Exception ex) {
                    Logger.getLogger(CheckPremiumMembership.class.getName()).log(Level.SEVERE, null, ex);
                }
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
        return this;
    }

}
