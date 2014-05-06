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

package model.types;

/**
 * Enumerator for credit card types.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public enum CreditCardType {
    VISA (1, "Visa"),
    MASTERCARD (2, "Mastercard"),
    AMEX (3, "American Express");
    
    private int id;
    private String name;
    
    /**
     * Construct a CreditCardType
     * @param id The constant's id
     * @param name The constant's identifier string
     */
    CreditCardType(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Get the credit card type's id.
     * @return The credit card type's id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Get the credit card type's name.
     * @return The credit card type's name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Search for a specific credit card type by its id.
     * @param id The id of the searched credit card type.
     * @return If the given id matches the id of an credit card type, the credit card
     * type is returned. Otherwise the <code>null</code> is returned.
     */
    public static CreditCardType getCreditCardTypeById(int id) {
        for (CreditCardType cct : CreditCardType.values()) {
            if (cct.getId() == id) {
                return cct;
            }
        }
        
        return null;
    }
    
    /**
     * Search for a specific credit card type by its name.
     * @param name The name of the searched credit card type.
     * @return If the given name matches the name of an credit card type, 
     * the credit card type is returned. Otherwise the <code>null</code> is returned.
     */
    public static CreditCardType getCreditCardTypeByIdentifier(String name) {
        for (CreditCardType cct : CreditCardType.values()) {
            if (cct.getName().equals(name)) {
                return cct;
            }
        }
        
        return null;
    }
    
}
