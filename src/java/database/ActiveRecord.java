package database;

/**
 * Abstract class to group all active records.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public abstract class ActiveRecord {
    /**
     * Constructs an SQL ORDER BY clause from an array of column names.
     * @param columns the column names used for the ORDER BY clause.
     * @return Either an empty string (no columns were provided) or an ORDER BY clause.
     */
    protected static String buildOrderBy (final String[] columns) {
        String orderBy = "";
        
        if (columns.length != 0) {
            orderBy = " ORDER BY ";
            //Loop through all column names
            for (String column : columns) {
                orderBy += column + ", ";
            }
            //Get rid of the last colon.
            orderBy = orderBy.substring(0, orderBy.lastIndexOf(',')) + " ";
        }
        
        return orderBy;
    }
}
