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

package database;

import java.util.ArrayList;

/**
 * Enumeration of all supported SQL constraint operators
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public enum SQLConstraintOperator {

    /**
     * Check whether two values are equal.
     */
    EQUAL("", "", " = ", new Class[]{Number.class}),
    /**
     * Check whether the "left" value is greater than the "right" value.
     */
    GREATER_THAN("", "", " > ", new Class[]{Number.class}),
    /**
     * Check whether the "left" value is less than the "right" value.
     */
    LESS_THAN("", "", " < ", new Class[]{Number.class}),
    /**
     * Check whether the "left" value is greater than or equal to the "right" value.
     */
    GREATER_EQUAL("", "", " >= ", new Class[]{Number.class}),
    /**
     * Check whether the "left" value is less than or equal to the "right" value.
     */
    LESS_EQUAL("", "", " <= ", new Class[]{Number.class}),
    /**
     * Check whether the "left" value is not equal to the "right" value.
     */
    INEQUAL("", "", " <> ", new Class[]{Number.class}),
    /**
     * Check whether the "left" value is simliar to the "right" value. The right value can be modified by wildcards (e.g. %).
     * Use LIKE for strings!
     */
    LIKE("", "'", " LIKE '", new Class[]{String.class}),
    /**
     * Check whether the "left" value is between the two provided values.
     */
    BETWEEN("", "", " BETWEEN ", new Class[]{ArrayList.class, String.class}),
    /**
     * Check whether the "left" value is in a set of values.
     */
    IN("", ")", " IN(", new Class[]{ArrayList.class, String.class}),
    /**
     * Check if both constraints evaluate true.
     */
    AND("(", ")", " AND ", new Class[]{ArrayList.class, Constraint.class}),
    /**
     * Check if on of them or both constraints evaluate true.
     */
    OR("(", ")", " OR ", new Class[]{ArrayList.class, Constraint.class});
    
    /**
     * The operator's start tag.
     */
    private final String startTag;

    /**
     * The operator's end tag.
     */
    private final String endTag;
    
    /**
     * The actual operator symbol/string.
     */
    private final String operator;
    
    /**
     * The expected data type of the value that will be used with the operator.
     * If the size of the array is greater than one, it indicates that the value 
     * will be provided as a <code>ArrayList</code>. The second value in the array
     * will then provide the expected data type of the elements in the list.
     */
    private final Class[] expectedValueType;

    /**
     * Construct an operator constant.
     * @param startTag The operator's start tag.
     * @param endTag The operator's end tag.
     * @param operator The operator symbol/string.
     * @param expectedValueType The expected data type of the value provided as an array. If multiple values will be used provide at first <code>ArrayList.class</code> and then the data type of the list's elements.
     */
    SQLConstraintOperator(String startTag, String endTag, String operator, Class[] expectedValueType) {
        this.startTag = startTag;
        this.endTag = endTag;
        this.operator = operator;
        this.expectedValueType = expectedValueType;
    }

    /**
     * Get the operator's start tag.
     * @return the operator's start tag.
     */
    public String getStartTag() {
        return this.startTag;
    }

    /**
     * Get the operator's end tag.
     * @return the operator's end tag.
     */
    public String getEndTag() {
        return this.endTag;
    }

    /**
     * Get the operator symbol/string.
     * @return the operator symbol/string.
     */
    public String getOperator() {
        return this.operator;
    }

    /**
     * Get the expected data type of the value.
     * @return the expected data type of the value.
     */
    public Class[] getExpectedValueType() {
        return this.expectedValueType;
    }
    
    
    
}
