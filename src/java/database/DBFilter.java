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
 * This class provides an simple way of creating filters for SQL queries.
 * Constraints can be added and afterwards an SQL WHERE clause is generated.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class DBFilter {

    /**
     * A list of constraints for the filter.
     */
    private final ArrayList<Constraint> constraints;
    
    /**
     * Construct a filter object.
     */
    public DBFilter() {
        this.constraints = new ArrayList<>();
    }
    
    /**
     * Add a constraint to the filter. For constraints with strings use the LIKE operator.
     * @param attribute Column that will be used for the constraint. Use the constant column names provided by an active record.
     * @param operator One of the supported SQL constraint operators.
     * @param value Depending on the operator the value can be a string, a list of strings or a list of constraints (for cascaded constraints with AND and OR).
     */
    public void addConstraint(final String attribute, SQLConstraintOperator operator, Object value) {
        this.constraints.add(new Constraint(attribute, operator, value));
    }
    
    /**
     * Add a constraint to the filter.
     * @param constraint the constraint.
     */
    public void addConstraint(Constraint constraint) {
        this.constraints.add(constraint);
    }
    
    /**
     * Build a constraint from the parameters and return it for later use. It will NOT be added to the list of constraints. For constraints with strings use the LIKE operator.
     * @param attribute Column that will be used for the constraint. Use the constant column names provided by an active record.
     * @param operator One of the supported SQL constraint operators.
     * @param value Depending on the operator the value can be a string, a list of strings or a list of constraints (for cascaded constraints with AND and OR).
     * @return a constraint.
     */
    public Constraint buildConstraint(final String attribute, SQLConstraintOperator operator, Object value) {
        return new Constraint(attribute, operator, value);
    }
    
    /**
     * Builds an SQL WHERE clause based on the contraints that have been added to the object.
     * @return an SQL WHERE clause or an empty string (no contraints were defined).
     * @throws database.DBException if the where clause cannot be built.
     */
    public String buildWhereClause() throws DBException{
        String whereClause = " WHERE ";
        
        for (Constraint c : this.constraints) {
            if(c.toString().isEmpty()) {
                throw new DBException("Failed to generate string represenation of a constraint!", 4);
            }
            whereClause += c.toString();
            whereClause += " AND ";
        }
        
        //Remove the last AND
        if (!whereClause.isEmpty()) {
            whereClause = whereClause.substring(0, whereClause.length()-5);
        }
        
        return whereClause;
    }
    
}

