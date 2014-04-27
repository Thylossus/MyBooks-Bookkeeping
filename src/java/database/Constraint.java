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
import java.util.Iterator;

/**
 * Class for constraints that can be added to the DBFilter.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Constraint {

    /**
     * Column that will be used for the constraint. Use the constant column
     * names provided by an active record.
     */
    private final String attribute;
    /**
     * One of the supported SQL constraint operators.
     */
    private final SQLConstraintOperator operator;
    /**
     * Depending on the operator the value can be a string, a list of strings or
     * a list of constraints (for cascaded constraints with AND and OR).
     */
    private final Object value;

    /**
     * Construct a new constraint.
     *
     * @param attribute Column that will be used for the constraint. Use the
     * constant column names provided by an active record.
     * @param operator One of the supported SQL constraint operators.
     * @param value Depending on the operator the value can be a string, a list
     * of strings or a list of constraints (for cascaded constraints with AND
     * and OR).
     */
    public Constraint(String attribute, SQLConstraintOperator operator, Object value) {
        this.attribute = attribute;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Parse the constraint and transform it into a string.
     *
     * @return the string representation of the constraint or an empty string if
     * the constraint was not properly defined.
     */
    @Override
    public String toString() {

        if (this.evaluateAttribute() && this.evaluateValue()) {
            String constraint = this.operator.getStartTag();

            switch (this.operator) {
                case AND:
                    //We have already checked the type of the value but we also
                    //have to check whether the amount of elements in the list
                    //is correct.
                    //We expect exactly TWO constraints for the AND operator.
                    if (((ArrayList)this.value).size() != 2) {
                        return "";
                    }
                    constraint += ((ArrayList)this.value).get(0);
                    constraint += this.operator.getOperator();
                    constraint += ((ArrayList)this.value).get(1);
                    break;
                case BETWEEN:
                    //We have already checked the type of the value but we also
                    //have to check whether the amount of elements in the list
                    //is correct.
                    //We expect exactly TWO strings for the BETWEEN operator.
                    if (((ArrayList)this.value).size() != 2) {
                        return "";
                    }
                    constraint += this.attribute;
                    constraint += this.operator.getOperator();
                    constraint += ((ArrayList)this.value).get(0);
                    constraint += " AND ";
                    constraint += ((ArrayList)this.value).get(1);
                    break;
                case IN:
                    //We have already checked the type of the value but we also
                    //have to check whether the amount of elements in the list
                    //is correct.
                    //We expect the list not to be empty.
                    if (((ArrayList)this.value).isEmpty()) {
                        return "";
                    }
                    constraint += this.attribute;
                    constraint += this.operator.getOperator();
                    Iterator iter = ((ArrayList)this.value).iterator();
                    while(iter.hasNext()) {
                        constraint += iter.next().toString();
                        if (iter.hasNext()) {
                            constraint += ", ";
                        }
                    }
                    //At this point the constraint should look like:
                    //ATTRIBUTE IN(value1, value2, value3,...
                    //The closing parenthesis will be added as the end tag later.
                    break;
                case OR:
                    //We have already checked the type of the value but we also
                    //have to check whether the amount of elements in the list
                    //is correct.
                    //We expect exactly TWO constraints for the OR operator.
                    if (((ArrayList)this.value).size() != 2) {
                        return "";
                    }
                    constraint += ((ArrayList)this.value).get(0);
                    constraint += this.operator.getOperator();
                    constraint += ((ArrayList)this.value).get(1);
                    break;
                default:
                    constraint = this.attribute
                            + this.operator.getOperator()
                            + this.value.toString();
            }
            
            constraint += this.operator.getEndTag();

            return constraint;
        } else {
            return "";
        }

    }

    /**
     * Evaluate the given value/s with respect to their data type.
     * @return True if the value is valid and false if it is not.
     */
    private boolean evaluateValue() {
        Class[] expectedValue = this.operator.getExpectedValueType();
        if (expectedValue[0].isInstance(this.value)) {
            if (expectedValue.length == 2) {
                //There is a second data type in the expected value array.
                //This indicates that we have to handle an array list.
                //Check whether all elements of the list have the expeced type.
                for (Object listElement : (ArrayList)this.value) {
                    if (!expectedValue[1].isInstance(listElement)) {
                        return false;
                    }
                }
                //If this statement is reached all elements of the list have the expected data type.
                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Evaluate the given attribute/column name.
     *
     * @return True if the attribute/column name is valid and false if it is
     * not.
     */
    private boolean evaluateAttribute() {
        //Check whether the attribute/column name is empty.
        if (this.attribute.isEmpty()) {
            //If it is empty, it is only valid with the operator AND and OR.
            if (this.operator.equals(SQLConstraintOperator.AND) || this.operator.equals(SQLConstraintOperator.OR)) {
                //The used operator is either AND or OR.
                return true;
            } else {
                //The used operator is neither AND nor OR and thus, the attribute/column name is not valid.
                return false;
            }
        } else {
            //If it is not empty, it is valid.
            return true;
        }
    }
}
