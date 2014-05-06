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

/**
 * Active record for articles
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 * @version 0.1
 */
public class Article extends ActiveRecord implements DBDeletable, DBInsertable, DBUpdatable{

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "NEWS";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "NEWS";
    
    //Columns
    public static final String CLMN_ID = "ID";
    public static final String CLMN_TITLE = "TITLE";
    public static final String CLMN_CONTENT = "CONTENT";
    public static final String CLMN_ARTICLE_DATE = "ARTICLE_DATE";
    public static final String CLMN_EDIT_DATE = "EDIT_DATE";
    public static final String CLMN_AUTHOR = "AUTHOR";
    public static final String CLMN_EDITOR = "EDITOR";
    //Select only columns
    public static final String CLMN_AUTHOR_MAIL = "A_MAIL";
    public static final String CLMN_AUTHOR_NAME = "A_NAME";
    public static final String CLMN_EDITOR_MAIL = "E_MAIL";
    public static final String CLMN_EDITOR_NAME = "E_NAME";

    //SQL code
    /**
     * SQL query for selecting all Articles from the Articles table.
     */
    private static final String SELECT_ALL = 
            "SELECT "
            + " U1." + User.CLMN_ID + " AS " + Article.CLMN_AUTHOR + ","
            + " U1." + User.CLMN_MAIL + " AS " + Article.CLMN_AUTHOR_MAIL + ","
            + " U1." + User.CLMN_FIRSTNAME + " || ' ' || U1." + User.CLMN_LASTNAME + " AS " + Article.CLMN_AUTHOR_NAME + ","
            + " U2." + User.CLMN_ID + " AS " + Article.CLMN_EDITOR + ","
            + " U2." + User.CLMN_MAIL + " AS " + Article.CLMN_EDITOR_MAIL + ","
            + " U2." + User.CLMN_FIRSTNAME + " || ' ' || U2." + User.CLMN_LASTNAME + " AS " + Article.CLMN_EDITOR_NAME + ","
            + " " + Article.SELECT_TABLE + "." + Article.CLMN_ID + " AS " + Article.CLMN_ID + ","
            + " " + Article.SELECT_TABLE + "." + Article.CLMN_TITLE + " AS " + Article.CLMN_TITLE + ","
            + " " + Article.SELECT_TABLE + "." + Article.CLMN_CONTENT + " AS " + Article.CLMN_CONTENT + ","
            + " " + Article.SELECT_TABLE + "." + Article.CLMN_ARTICLE_DATE + " AS " + Article.CLMN_ARTICLE_DATE + ","
            + " NEWS_EDITS.DATE AS " + Article.CLMN_EDIT_DATE
            + " FROM " + User.SELECT_TABLE + " U1 "
            + " INNER JOIN " + Article.SELECT_TABLE
            + "	ON U1." + User.CLMN_ID + " = " + Article.SELECT_TABLE + "." + Article.CLMN_AUTHOR
            + "	LEFT JOIN (" 
                + " SELECT NE1.ARTICLE, NE1.DATE, NE2.EDITOR FROM ("
                    + "	SELECT ARTICLE, MAX(DATE) AS DATE FROM NEWS_EDITS NE1 GROUP BY ARTICLE"
                + " ) AS NE1 "
                + " INNER JOIN NEWS_EDITS NE2 "
                + " ON NE1.ARTICLE = NE2.ARTICLE "
                + " AND NE1.DATE = NE2.DATE"
            + " ) AS NEWS_EDITS "
            + " ON " + Article.SELECT_TABLE + "." + Article.CLMN_ID + " = NEWS_EDITS.ARTICLE"
            + " LEFT JOIN " + User.SELECT_TABLE + " U2 ON NEWS_EDITS." + Article.CLMN_EDITOR + " = U2.ID";
    
    //Attributes
    private int id;
    private String title;
    private String content;
    private Timestamp date;
    private int authorId;
    private String authorName;
    private String authorMail;
    private int editorId;
    private String editorName;
    private String editorMail;
    private Timestamp editDate;
    
    //Construction
    /**
     * Empty constructor for creating non-initialised Articles.
     */
    public Article(){
        this.id = 0;
        this.title = null;
        this.content = null;
        this.date = null;
        this.authorId = 0;
        this.authorName = null;
        this.authorMail = null;
        this.editorId = 0;
        this.editorName = null;
        this.editorMail = null;
        this.editDate = null;
    }
    /**
     * Construct a Article from a result set.
     * @param rs a result set containing data about a Article.
     * @throws DBException if reading from the result set failed.
     */
    public Article(ResultSet rs) throws DBException{
        try {
            this.id = rs.getInt(Article.CLMN_ID);
            this.title = rs.getString(Article.CLMN_TITLE);
            this.content = rs.getString(Article.CLMN_CONTENT);
            this.date = rs.getTimestamp(Article.CLMN_ARTICLE_DATE);
            this.authorId = rs.getInt(Article.CLMN_AUTHOR);
            this.authorName = rs.getString(Article.CLMN_AUTHOR_NAME);
            this.authorMail = rs.getString(Article.CLMN_AUTHOR_MAIL);
            this.editorId = rs.getInt(Article.CLMN_EDITOR);
            this.editorName = rs.getString(Article.CLMN_EDITOR_NAME);
            this.editorMail = rs.getString(Article.CLMN_EDITOR_MAIL);
            this.editDate = rs.getTimestamp(Article.CLMN_EDIT_DATE);
        } catch (SQLException sqle) {
            String msg = "An error occured while reading from a result set.";
            throw new DBException(msg, sqle, 2);
        }
    }
    
    //Selection
    /**
     * Execute an SQL query that is provided as a string.
     * @param SQL a string containing a valid SQL statement.
     * @return A list of Articles that may also be empty.
     */
    private static ArrayList<Article> executeSelection(String SQL) {
        ArrayList<Article> articles = new ArrayList<>();
        Connection con;
        
        try {
            con = DBConnection.getInstance().getConnection();
            try(PreparedStatement stmt = con.prepareStatement(SQL); ResultSet rs = stmt.executeQuery()) {
                
                while(rs.next()) {
                    articles.add(new Article(rs));
                }
            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return articles;
    }

    /**
     * Find all Articles.
     *
     * @return a list of all Articles encapsulated in active record objects.
     */
    public static ArrayList<Article> findAll() {
        return Article.executeSelection(Article.SELECT_ALL);
    }

    /**
     * Find all Articles and order the results.
     *
     * @param orderBy an array of column names that will be used for sorting.
     * @return a list of all Articles encapsulated in active record objects.
     */
    public static ArrayList<Article> findAll(final String[] orderBy) {
        return Article.executeSelection(Article.SELECT_ALL + ActiveRecord.buildOrderBy(orderBy));
    }

    /**
     * Find all Articles and filter the results.
     *
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all Articles encapsulated in active record objects.
     */
    public static ArrayList<Article> findAll(DBFilter filter) {
        try {
            return Article.executeSelection(Article.SELECT_ALL + filter.buildWhereClause());
        } catch (DBException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Find all Articles and order and filter the results.
     *
     * @param orderBy an array of colunn names that will be used for sorting.
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all Articles encapsulated in active record objects.
     */
    public static ArrayList<Article> findAll(final String[] orderBy, DBFilter filter) {
        try {
            return Article.executeSelection(Article.SELECT_ALL + filter.buildWhereClause() + ActiveRecord.buildOrderBy(orderBy));
        } catch (DBException ex) {
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Modification 
    /**
     * Insert the <code>Article</code> specified in this active record into the database.
     * @return Returns true if the insertion was successfull and false otherwise.
     */
    @Override
    public boolean insert() {
        Connection con;
        String sql = "INSERT INTO " + Article.MODIFY_TABLE
                + "(" + Article.CLMN_TITLE + ", " + Article.CLMN_CONTENT + ", " + Article.CLMN_AUTHOR + ")"
                + " VALUES "
                + "(?, ?, ?)";

        try {
            con = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, this.title);
                stmt.setString(2, this.content);
                stmt.setInt(2, this.authorId);

                if (stmt.executeUpdate() != 1) {
                    Logger.getLogger(Article.class.getName()).log(Level.WARNING, "Failed to insert new Article in DB!");
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
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
    
    /**
     * Update the <code>Article</code> specified in this active record in the database.
     * @return Returns true if the update was successfull and false otherwise.
     */
    @Override
    public boolean update() {
        if (this.id != 0
                && this.title != null
                && this.content != null
                && this.editorId != 0) {
            Connection con;
            String sql = "UPDATE " + Article.MODIFY_TABLE
                    + " SET " + Article.CLMN_TITLE + " = ? ,"
                    + " " + Article.CLMN_CONTENT + " = ? "
                    + " WHERE " + Article.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    //Set parameters
                    stmt.setString(1, this.title);
                    stmt.setString(2, this.content);
                    stmt.setInt(3, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(BalanceSheet.class.getName()).log(Level.WARNING, "Failed to update Article in DB!");
                    } else {
                        //Update news_edit
                        sql = "INSERT INTO NEWS_EDITS (ARTICLE, EDITOR) VALUES (?,?)";
                        PreparedStatement neStmt = con.prepareStatement(sql);
                        neStmt.setInt(1, this.id);
                        neStmt.setInt(2, this.editorId);
                        
                        if (neStmt.executeUpdate() != 1) {
                            Logger.getLogger(BalanceSheet.class.getName()).log(Level.WARNING, "Failed to update Article in DB!");
                        }
                    }
                } catch (SQLException sqle) {
                    //Statement failed
                    String msg = "Failed to prepare the SQL statement.";
                    Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(BalanceSheet.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Removes the <code>Article</code> from the database.
     * @return Returns true if the deletion was successfull and false otherwise.
     */
    @Override
    public boolean delete() {
        if (this.id != 0) {
            Connection con;
            String sql = "DELETE FROM NEWS_EDITS WHERE ARTICLE = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setInt(1, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(Article.class.getName()).log(Level.WARNING, "Failed to delete Article from DB!");
                    } else {
                        sql = "DELETE FROM " + Article.MODIFY_TABLE
                            + " WHERE " + Article.CLMN_ID + " = ?";
                        
                        PreparedStatement nextStmt = con.prepareStatement(sql);
                        nextStmt.setInt(1, this.id);
                        
                        if (nextStmt.executeUpdate() != 1) {
                            Logger.getLogger(Article.class.getName()).log(Level.WARNING, "Failed to delete Article from DB!");
                        }
                    }
                } catch (SQLException sqle) {
                    //Statement failed
                    String msg = "Failed to prepare the SQL statement.";
                    Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(Article.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }
    
    //Getter & Setter
    /**
     * Get the article's id.
     * @return the article's id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Get the article's title.
     * @return the article's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the article's title.
     * @param title the article's title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the article's content.
     * @return the article's content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the article's content.
     * @param content the article's content.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the article's date.
     * @return the article's date.
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * Get the author's id.
     * @return the author's id.
     */
    public int getAuthorId() {
        return authorId;
    }
    
    /**
     * Set the author's id.
     * @param authorId the author's id.
     */
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    /**
     * Get the author's name.
     * @return the author's name.
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Get the author's mail.
     * @return the author's mail.
     */
    public String getAuthorMail() {
        return authorMail;
    }

    /**
     * Get the editor's id.
     * @return the editor's id.
     */
    public int getEditorId() {
        return editorId;
    }

    /**
     * Set the editor's id.
     * @param editorId the editor's id.
     */
    public void setEditorId(int editorId) {
        this.editorId = editorId;
    }

    /**
     * Get the editor's name.
     * @return the editor's name.
     */
    public String getEditorName() {
        return editorName;
    }

    /**
     * Get the editor's mail.
     * @return the editor's mail.
     */
    public String getEditorMail() {
        return editorMail;
    }

    /**
     * Get the edit date.
     * @return the edit date.
     */
    public Timestamp getEditDate() {
        return editDate;
    }

    
}

