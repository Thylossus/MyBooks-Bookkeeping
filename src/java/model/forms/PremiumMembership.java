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
package model.forms;

import java.util.ArrayList;
import model.components.Input;
import model.types.CreditCardType;

/**
 * Context for the purchase and renew premium membership forms.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class PremiumMembership extends Context {

    /**
     * Premium memberships duration as a period of month.
     */
    private int period;

    /**
     * Cardholder firstname.
     */
    private String cFirstname;

    /**
     * Cardholder lastname.
     */
    private String cLastname;

    /**
     * Credit card type id.
     */
    private int creditCardType;

    /**
     * Encrypted credit card number.
     */
    private byte[] creditCardNumber;

    /**
     * Construct the premium membership context.
     *
     * @param inputList
     */
    public PremiumMembership(ArrayList<Input> inputList) {
        this.inputList = inputList;

        this.period = 0;
        this.cFirstname = null;
        this.cLastname = null;
        this.creditCardType = 0;
        this.creditCardNumber = null;
    }

    /**
     * Validate the given inputs for the premium membership context.
     *
     * @return
     */
    @Override
    public boolean validate() {
        //Check required parameters
        //Check period
        if (inputList.get(0).getKey().equals("period")) {
            this.period = (int)inputList.get(0).getValue();
        } else {
            this.period = (int)this.searchInputValue("period");
            if (this.period == 0) {
                return false;
            }
        }
        //Check cFirstname
        if (inputList.get(1).getKey().equals("cFirstname")) {
            this.cFirstname = (String)inputList.get(1).getValue();
        } else {
            this.cFirstname = (String)this.searchInputValue("cFirstname");
            if (this.cFirstname == null) {
                return false;
            }
        }       
        //Check cLastname
        if (inputList.get(2).getKey().equals("cLastname")) {
            this.cLastname = (String)inputList.get(2).getValue();
        } else {
            this.cLastname = (String)this.searchInputValue("cLastname");
            if (this.cLastname == null) {
                return false;
            }
        }
        //Check creditCardType
        if (inputList.get(3).getKey().equals("creditCardType")) {
            this.creditCardType = (int)inputList.get(3).getValue();
        } else {
            this.creditCardType = (int)this.searchInputValue("creditCardType");
            if (this.creditCardType == 0) {
                return false;
            }
        }
        //Check creditCardNumber
        if (inputList.get(4).getKey().equals("creditCardNumber")) {
            this.creditCardNumber = (byte[])inputList.get(4).getValue();
        } else {
            this.creditCardNumber = (byte[])this.searchInputValue("creditCardNumber");
            if (this.creditCardNumber == null) {
                return false;
            }
        }
        
        //Check length of strings and lenght of encrypted credit card number.
        if (this.cFirstname.length() > 30) {
            return false;
        }
        if (this.cLastname.length() > 30) {
            return false;
        }
        if (this.creditCardNumber.length != 32) {
            return false;
        }
        
        //Check if period is valid
        if (this.period != 1 &&
                this.period != 3 &&
                this.period != 6 &&
                this.period != 12) {
            return false;
        }
        
        return CreditCardType.getCreditCardTypeById(this.creditCardType) != null;
    }

}
