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

import controller.ScopeHandler;
import database.PaymentDetail;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ModelComponent;
import model.types.CreditCardType;

/**
 * This component is directly related to purchasing or renewing a premium membership and stores the
 * payment details for a premium membership.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class StorePaymentDetails extends ModelComponent {

    /**
     * Cardholder firstname.
     */
    private String cFirstname;
    /**
     * Cardholder lastname.
     */
    private String cLastname;
    /**
     * Encrypted credit card number.
     */
    private byte[] creditCardNumber;
    /**
     * Credit card type.
     */
    private CreditCardType creditCardType;

    /**
     * Construct a StorePaymentDetails object
     *
     * @param request The request's request object
     * @param response The request's response object
     */
    public StorePaymentDetails(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * Process the action for StorePaymentDetails
     */
    @Override
    public void process() {
        if (this.cFirstname != null &&
                this.cLastname != null &&
                this.creditCardNumber != null &&
                this.creditCardType != null) {
            
            PaymentDetail pd = new PaymentDetail();
            pd.setCardholderFirstname(this.cFirstname);
            pd.setCardholderLastname(this.cLastname);
            pd.setCreditCardNumber(this.creditCardNumber);
            pd.setCreditCardType(this.creditCardType);
            
            if (pd.insert()) {
                ScopeHandler.getInstance().store(this.request, "paymentDetail", pd);
            } else {
                Logger.getLogger(StorePaymentDetails.class.getName()).log(Level.WARNING, "Could not store payment details because insertion into the database failed.");
            }
            
        } else {
            Logger.getLogger(StorePaymentDetails.class.getName()).log(Level.WARNING, "Could not store payment details because some parameter was not set.");
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
        if (params.get("cFirstname") != null) {
            this.cFirstname = (String) params.get("cFirstname");
        }
        if (params.get("cLastname") != null) {
            this.cLastname = (String) params.get("cLastname");
        }
        if (params.get("creditCardType") != null) {
            this.creditCardType = (CreditCardType) params.get("creditCardType");
        }
        if (params.get("creditCardNumber") != null) {
            this.creditCardNumber = (byte[]) params.get("creditCardNumber");
        }

        return this;
    }

}
