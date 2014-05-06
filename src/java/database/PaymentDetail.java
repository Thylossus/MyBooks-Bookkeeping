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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.types.CreditCardType;

/**
 * Active record for payment details
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class PaymentDetail extends ActiveRecord implements DBInsertable{

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "PAYMENT_DETAILS";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "PAYMENT_DETAILS";
    
    //Columns
    public static final String CLMN_ID = "ID";
    public static final String CLMN_CARDHOLDER_LASTNAME = "CARDHOLDER_LASTNAME";
    public static final String CLMN_CARDHOLDER_FIRSTNAME = "CARDHOLDER_FIRSTNAME";
    public static final String CLMN_CREDIT_CARD_NUMBER = "CREDIT_CARD_NUMBER";
    public static final String CLMN_DATE_OF_PURCHASE = "DATE_OF_PURCHASE";
    public static final String CLMN_CREDIT_CARD_TYPE = "CREDIT_CARD_TYPE";

    //SQL code
    /**
     * SQL query for selecting all PaymentDetails from the PaymentDetails table.
     */
    private static final String SELECT_ALL = 
            "SELECT * " + 
            "FROM " + PaymentDetail.SELECT_TABLE;
    
    //Attributes
    /**
     * The payment detail's id.
     */
    private int id;
    /**
     * Cardholder lastname.
     */
    private String cardholderLastname;
    /**
     * Cardholder firstname.
     */
    private String cardholderFirstname;
    /**
     * Encrypted credit card number.
     */
    private byte[] creditCardNumber;
    /**
     * Date of purchase.
     */
    private Timestamp dateOfPurchase;
    /**
     * Credit card type.
     */
    private CreditCardType creditCardType;
    
    //Construction
    /**
     * Empty constructor for creating non-initialised PaymentDetails.
     */
    public PaymentDetail(){}
    /**
     * Construct a PaymentDetail from a result set.
     * @param rs a result set containing data about a PaymentDetail.
     * @throws DBException if reading from the result set failed.
     */
    public PaymentDetail(ResultSet rs) throws DBException{
        try {
            this.id = rs.getInt(PaymentDetail.CLMN_ID);
            this.cardholderLastname = rs.getString(PaymentDetail.CLMN_CARDHOLDER_LASTNAME);
            this.cardholderFirstname = rs.getString(PaymentDetail.CLMN_CARDHOLDER_FIRSTNAME);
            this.creditCardNumber = rs.getBytes(PaymentDetail.CLMN_CREDIT_CARD_NUMBER);
            this.dateOfPurchase = rs.getTimestamp(PaymentDetail.CLMN_DATE_OF_PURCHASE);
            this.creditCardType = CreditCardType.getCreditCardTypeById(rs.getInt(PaymentDetail.CLMN_CREDIT_CARD_TYPE));
        } catch (SQLException sqle) {
            String msg = "An error occured while reading from a result set.";
            throw new DBException(msg, sqle, 2);
        }
    }
    
    //Selection
    /**
     * Execute an SQL query that is provided as a string.
     * @param SQL a string containing a valid SQL statement.
     * @return A list of PaymentDetails that may also be empty.
     */
    private static ArrayList<PaymentDetail> executeSelection(String SQL) {
        ArrayList<PaymentDetail> paymentDetails = new ArrayList<>();
        Connection con;
        
        try {
            con = DBConnection.getInstance().getConnection();
            try(PreparedStatement stmt = con.prepareStatement(SQL)) {
                ResultSet rs = stmt.executeQuery();
                
                while(rs.next()) {
                    paymentDetails.add(new PaymentDetail(rs));
                }
                
                rs.close();
            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return paymentDetails;
    }

    /**
     * Find all PaymentDetails.
     *
     * @return a list of all PaymentDetails encapsulated in active record objects.
     */
    public static ArrayList<PaymentDetail> findAll() {
        return PaymentDetail.executeSelection(PaymentDetail.SELECT_ALL);
    }

    /**
     * Find all PaymentDetails and order the results.
     *
     * @param orderBy an array of column names that will be used for sorting.
     * @return a list of all PaymentDetails encapsulated in active record objects.
     */
    public static ArrayList<PaymentDetail> findAll(final String[] orderBy) {
        return PaymentDetail.executeSelection(PaymentDetail.SELECT_ALL + ActiveRecord.buildOrderBy(orderBy));
    }

    /**
     * Find all PaymentDetails and filter the results.
     *
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all PaymentDetails encapsulated in active record objects.
     */
    public static ArrayList<PaymentDetail> findAll(DBFilter filter) {
        try {
            return PaymentDetail.executeSelection(PaymentDetail.SELECT_ALL + filter.buildWhereClause());
        } catch (DBException ex) {
            Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Find all PaymentDetails and order and filter the results.
     *
     * @param orderBy an array of colunn names that will be used for sorting.
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all PaymentDetails encapsulated in active record objects.
     */
    public static ArrayList<PaymentDetail> findAll(final String[] orderBy, DBFilter filter) {
        try {
            return PaymentDetail.executeSelection(PaymentDetail.SELECT_ALL + filter.buildWhereClause() + ActiveRecord.buildOrderBy(orderBy));
        } catch (DBException ex) {
            Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Modification 
    /**
     * Insert the <code>PaymentDetail</code> specified in this active record into the database.
     * @return Returns true if the insertion was successfull and false otherwise.
     */
    @Override
    public boolean insert() {
        Connection con;
        String sql = "INSERT INTO " + PaymentDetail.MODIFY_TABLE
                + "(" + PaymentDetail.CLMN_CARDHOLDER_LASTNAME + ", " + PaymentDetail.CLMN_CARDHOLDER_FIRSTNAME + ", " + PaymentDetail.CLMN_CREDIT_CARD_NUMBER + ", " + PaymentDetail.CLMN_CREDIT_CARD_TYPE + ")"
                + " VALUES "
                + "(?, ?, ?, ?)";

        try {
            con = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, this.cardholderLastname);
                stmt.setString(2, this.cardholderFirstname);
                stmt.setBytes(3, this.creditCardNumber);
                stmt.setInt(4, this.creditCardType.getId());

                if (stmt.executeUpdate() != 1) {
                    Logger.getLogger(PaymentDetail.class.getName()).log(Level.WARNING, "Failed to insert new PaymentDetail in DB!");
                } else {
                    //Get the id of the new user
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        this.id = rs.getInt(1);
                    }
                }

            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(PaymentDetail.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
    
    //Getter & Setter
    /**
     * Get the payment detail's id.
     * @return the payment detail's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the card holder's lastname.
     * @return the card holder's lastname.
     */
    public String getCardholderLastname() {
        return cardholderLastname;
    }

    /**
     * Set the cardholder's lastname.
     * @param cardholderLastname the cardholder's lastname.
     */
    public void setCardholderLastname(String cardholderLastname) {
        this.cardholderLastname = cardholderLastname;
    }

    /**
     * Get the card holder's firstname.
     * @return the card holder's firstname.
     */
    public String getCardholderFirstname() {
        return cardholderFirstname;
    }

    /**
     * Set the cardholder's firstname.
     * @param cardholderFirstname the cardholder's firstname.
     */
    public void setCardholderFirstname(String cardholderFirstname) {
        this.cardholderFirstname = cardholderFirstname;
    }

    /**
     * Get the encrypted credit card number.
     * @return the encrypted credit card number.
     */
    public byte[] getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Set the credit card number.
     * @param creditCardNumber the credit card number (encrypted).
     */
    public void setCreditCardNumber(byte[] creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * Get the date of purchase.
     * @return the date of purchase.
     */
    public Timestamp getDateOfPurchase() {
        return dateOfPurchase;
    }

    /**
     * Get the credit card type.
     * @return the credit card type.
     */
    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    /**
     * Set the credit card type.
     * @param creditCardType the credit card type.
     */
    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }
    
    
}

