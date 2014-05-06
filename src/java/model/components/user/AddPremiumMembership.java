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
import database.PaymentDetail;
import database.PremiumMembership;
import database.User;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.ModelComponentFactory;
import model.types.UserType;

/**
 * Insert new premium membership into database.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class AddPremiumMembership extends ModelComponent{

    /**
     * Period of the premium membership.
     */
    private int period;
    /**
     * Payment details for the premium membership.
     */
    private PaymentDetail pd;
    
    /**
     * Construct a AddPremiumMembership object
     * @param request The request's request object
     * @param response The request's response object
     */
    public AddPremiumMembership(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        
        this.period = 0;
        this.pd = null;
    }

    /**
     * Process the action for AddPremiumMembership
     */
    @Override
    public void process() {
        User user = (User) ScopeHandler.getInstance().load(this.request, "user", "session");

        if (user != null && this.period != 0 && this.pd != null) {
            Date startDate = new Date();
            Date endDate = new Date();
            
            try {
                //Get existing premium memberships of the user (only for extend premium membership)
                ModelComponentFactory.createModuleComponent(this.request, this.response, "CheckPremiumMembership").process();
                
                if (ScopeHandler.getInstance().load(this.request, "expired") != null && !(boolean)ScopeHandler.getInstance().load(this.request, "expired")) {
                    startDate = (Date)ScopeHandler.getInstance().load(this.request, "lastExpirationDate");
                    //Get next day (new premium membership starts on the day after the old one expired)
                    GregorianCalendar nextDay = startDate.getCalendar();
                    nextDay.add(GregorianCalendar.DAY_OF_MONTH, 1);
                    startDate.setCalendar(nextDay);
                    //Update initial end date
                    endDate.setCalendar(nextDay);
                }
            } catch (Exception ex) {
                Logger.getLogger(AddPremiumMembership.class.getName()).log(Level.INFO, "Could not check premium memberships (" + ex.getMessage() + ")", ex);
            }
            
            //Calculate end day
            GregorianCalendar dateAfterPeriodOfMonths = endDate.getCalendar();
            dateAfterPeriodOfMonths.add(GregorianCalendar.MONTH, this.period);
            endDate.setCalendar(dateAfterPeriodOfMonths);
            
            PremiumMembership pm = new PremiumMembership();
            pm.setUserId(user.getId());
            pm.setPaymentDetails(this.pd.getId());
            pm.setStartDate(new java.sql.Date(startDate.getCalendar().getTimeInMillis()));
            pm.setEndDate(new java.sql.Date(endDate.getCalendar().getTimeInMillis()));
            
            if (pm.insert()) {
                if (user.getUserType().equals(UserType.STANDARD_USER)) {
                    user.setUserType(UserType.PREMIUM_USER);
                    user.update();
                }
                Logger.getLogger(AddPremiumMembership.class.getName()).log(Level.FINE, "Premium membership added!");
            } else {
                Logger.getLogger(AddPremiumMembership.class.getName()).log(Level.FINE, "Could not add premium membership because insertion into database failed.");
            }
            
        } else {
            Logger.getLogger(AddPremiumMembership.class.getName()).log(Level.FINE, "Could not add premium membership because parameters were missing.");
        }
    }

    /**
     * Provide parameters to the model component.
     * @param params an hash map of parameters.
     * @return the module component. The return value can be used for concatenation.
     */
    @Override
    public ModelComponent provideParameters(HashMap<String, Object> params) {
        if (params.get("period") != null) {
            this.period = (int)params.get("period");
        }
        
        if (params.get("paymentDetail") != null) {
            this.pd = (PaymentDetail)params.get("paymentDetail");
        }
        
        return this;
    }

}
