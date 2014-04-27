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
import model.types.Directory;

/**
 * Active record for DownloadFiles
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class DownloadFile extends ActiveRecord implements DBDeletable, DBInsertable, DBUpdatable {

    //Tables
    /**
     * The table that is used for selecting records.
     */
    public static final String SELECT_TABLE = "DOWNLOAD_FILES";
    /**
     * The table for modifying records.
     */
    private static final String MODIFY_TABLE = "DOWNLOAD_FILES";

    //Columns
    public static final String CLMN_ID = "ID";
    public static final String CLMN_TITLE = "TITLE";
    public static final String CLMN_DESCRIPTION = "DESCRIPTION";
    public static final String CLMN_UPLOAD_DATE = "UPLOAD_DATE";
    public static final String CLMN_FILENAME = "FILENAME";
    public static final String CLMN_FILESIZE = "FILESIZE";
    public static final String CLMN_NUMBER_OF_DOWNLOADS = "NUMBER_OF_DOWNLOADS";
    public static final String CLMN_DIRECTORY = "DIRECTORY";

    //SQL code
    /**
     * SQL query for selecting all users from the USERS table.
     */
    private static final String SELECT_ALL
            = "SELECT * "
            + "FROM " + DownloadFile.SELECT_TABLE;

    //Attributes
    /**
     * The download file's id.
     */
    private int id;
    /**
     * The download file's id.
     */
    private String title;
    /**
     * The download file's title.
     */
    private String description;
    /**
     * The download file's description.
     */
    private Timestamp uploadDate;
    /**
     * The download file's upload date.
     */
    private String filename;
    /**
     * The download file's name.
     */
    private int filesize;
    /**
     * The download file's size.
     */
    private int numberOfDownloads;
    /**
     * The download file's directory.
     */
    private Directory directory;

    //Construction
    /**
     * Empty constructor for creating non-initialised Users.
     */
    public DownloadFile() {
        this.id = 0;
        this.title = null;
        this.description = null;
        this.uploadDate = null;
        this.filename = null;
        this.filesize = 0;
        this.numberOfDownloads = 0;
        this.directory = null;
    }

    /**
     * Construct a user from a result set.
     *
     * @param rs a result set that contains a user.
     * @throws DBException if an error occurs while reading from the result set.
     */
    public DownloadFile(ResultSet rs) throws DBException {
        try {
            this.id = rs.getInt(DownloadFile.CLMN_ID);
            this.title = rs.getString(DownloadFile.CLMN_TITLE);
            this.description = rs.getString(DownloadFile.CLMN_DESCRIPTION);
            this.uploadDate = rs.getTimestamp(DownloadFile.CLMN_UPLOAD_DATE);
            this.filename = rs.getString(DownloadFile.CLMN_FILENAME);
            this.filesize = rs.getInt(DownloadFile.CLMN_FILESIZE);
            this.numberOfDownloads = rs.getInt(DownloadFile.CLMN_NUMBER_OF_DOWNLOADS);
            this.directory = Directory.getDirectoryById(rs.getInt(DownloadFile.CLMN_DIRECTORY));
        } catch (SQLException sqle) {
            String msg = "An error occured while reading from a result set.";
            throw new DBException(msg, sqle, 3);
        }
    }

    //Selection
    /**
     * Execute an SQL query that is provided as a string.
     *
     * @param SQL a string containing a valid SQL statement.
     * @return A list of download files that may also be empty.
     */
    private static ArrayList<DownloadFile> executeSelection(String SQL) {
        ArrayList<DownloadFile> downloadFiles = new ArrayList<>();
        Connection con;

        try {
            con = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = con.prepareStatement(SQL); ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    downloadFiles.add(new DownloadFile(rs));
                }
            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return downloadFiles;
    }

    /**
     * Find all download filess.
     *
     * @return a list of all download files encapsulated in active record
     * objects.
     */
    public static ArrayList<DownloadFile> findAll() {
        return DownloadFile.executeSelection(DownloadFile.SELECT_ALL);
    }

    /**
     * Find all download files and order the results.
     *
     * @param orderBy an array of column names that will be used for sorting.
     * @return a list of all download files encapsulated in active record
     * objects.
     */
    public static ArrayList<DownloadFile> findAll(final String[] orderBy) {
        return DownloadFile.executeSelection(DownloadFile.SELECT_ALL + ActiveRecord.buildOrderBy(orderBy));
    }

    /**
     * Find all download files and filter the results.
     *
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all download files encapsulated in active record
     * objects.
     */
    public static ArrayList<DownloadFile> findAll(DBFilter filter) {
        try {
            return DownloadFile.executeSelection(DownloadFile.SELECT_ALL + filter.buildWhereClause());
        } catch (DBException ex) {
            Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Find all download files and order and filter the results.
     *
     * @param orderBy an array of colunn names that will be used for sorting.
     * @param filter a <code>DBFilter</code> object that contains the
     * constraints for the query's where clause.
     * @return a list of all download files encapsulated in active record
     * objects.
     */
    public static ArrayList<DownloadFile> findAll(final String[] orderBy, DBFilter filter) {
        try {
            return DownloadFile.executeSelection(DownloadFile.SELECT_ALL + filter.buildWhereClause() + ActiveRecord.buildOrderBy(orderBy));
        } catch (DBException ex) {
            Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //Modification 
    /**
     * Insert the <code>DownloadFile</code> specified in this active record into
     * the database.
     *
     * @return Returns true if the insertion was successfull and false
     * otherwise.
     */
    @Override
    public boolean insert() {
        Connection con;
        String sql = "INSERT INTO " + DownloadFile.MODIFY_TABLE
                + "(" + DownloadFile.CLMN_TITLE + ", " + DownloadFile.CLMN_DESCRIPTION + ", " + DownloadFile.CLMN_FILENAME + ", " + DownloadFile.CLMN_FILESIZE + ", " + DownloadFile.CLMN_NUMBER_OF_DOWNLOADS + ", " + DownloadFile.CLMN_DIRECTORY + ")"
                + " VALUES "
                + "(?, ?, ?, ?, ?, ?)";

        try {
            con = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                stmt.setString(1, this.title);
                stmt.setString(2, this.description);
                stmt.setString(3, this.filename);
                stmt.setInt(4, this.filesize);
                stmt.setInt(5, this.numberOfDownloads);
                stmt.setInt(6, this.directory.getId());

                if (stmt.executeUpdate() != 1) {
                    Logger.getLogger(DownloadFile.class.getName()).log(Level.WARNING, "Failed to insert new download file in DB!");
                } else {
                    //Get the id of the new download file
                    ResultSet rs = stmt.getGeneratedKeys();
                    if (rs.next()) {
                        this.id = rs.getInt(1);
                    }
                }

            } catch (SQLException sqle) {
                //Statement failed
                String msg = "Failed to prepare the SQL statement.";
                Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
            }
        } catch (DBException ex) {
            //Establishing connection failed
            Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }

    /**
     * Update the <code>DownloadFile</code> specified in this active record in
     * the database.
     *
     * @return Returns true if the update was successfull and false otherwise.
     */
    @Override
    public boolean update() {
        if (this.id != 0
                && this.title != null
                && this.description != null
                && this.numberOfDownloads >= 0) {
            Connection con;
            String sql = "UPDATE " + DownloadFile.MODIFY_TABLE
                    + " SET " + DownloadFile.CLMN_TITLE + " = ? ,"
                    + " " + DownloadFile.CLMN_DESCRIPTION + " = ? ,"
                    + " " + DownloadFile.CLMN_NUMBER_OF_DOWNLOADS + " = ?"
                    + " WHERE " + DownloadFile.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setString(1, this.title);
                    stmt.setString(2, this.description);
                    stmt.setInt(3, this.numberOfDownloads);
                    stmt.setInt(4, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(DownloadFile.class.getName()).log(Level.WARNING, "Failed to update download file in DB!");
                    }
                } catch (SQLException sqle) {
                    //Statement failed
                    String msg = "Failed to prepare the SQL statement.";
                    Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the <code>DownloadFile</code> from the database.
     *
     * @return Returns true if the deletion was successfull and false otherwise.
     */
    @Override
    public boolean delete() {
        if (this.id != 0) {
            Connection con;
            String sql = "DELETE FROM " + DownloadFile.MODIFY_TABLE
                    + " WHERE " + DownloadFile.CLMN_ID + " = ?";

            try {
                con = DBConnection.getInstance().getConnection();
                try (PreparedStatement stmt = con.prepareStatement(sql)) {

                    stmt.setInt(1, this.id);

                    if (stmt.executeUpdate() != 1) {
                        Logger.getLogger(DownloadFile.class.getName()).log(Level.WARNING, "Failed to delete download file from DB!");
                    }
                } catch (SQLException sqle) {
                    //Statement failed
                    String msg = "Failed to prepare the SQL statement.";
                    Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, new DBException(msg, sqle, 2));
                }
            } catch (DBException ex) {
                //Establishing connection failed
                Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            return true;
        } else {
            return false;
        }
    }

    //Getter & Setter
    /**
     * Get the download file's id.
     *
     * @return the download file's id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the download file's title.
     *
     * @return the download file's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the download file's title.
     *
     * @param title the title as a string.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the download file's description.
     *
     * @return the download file's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the download file's description.
     *
     * @param description the description as a string.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the download file's upload date.
     *
     * @return the download file's upload date.
     */
    public Timestamp getUploadDate() {
        return uploadDate;
    }

    /**
     * Get the download file's name.
     *
     * @return the download file's name.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the download file's name. Only use initially. Will be ignored by
     * <code>update()</code>.
     *
     * @param filename the filename as a string.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Get the download file's size.
     *
     * @return the download file's size.
     */
    public int getFilesize() {
        return filesize;
    }

    /**
     * Set the download file's size. Only use initially. Will be ignored by
     * <code>update()</code>.
     *
     * @param filesize a file size in bytes.
     */
    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    /**
     * Get the download file's number of downloads.
     *
     * @return the download file's number of downloads.
     */
    public int getNumberOfDownloads() {
        return numberOfDownloads;
    }

    /**
     * Increment the number of downloads by one.
     */
    public void incrementNumberOfDownloads() {
        this.numberOfDownloads++;
    }

    /**
     * Get the download file's directory.
     *
     * @return the download file's directory.
     */
    public Directory getDirectory() {
        return directory;
    }

    /**
     * Set the download file's directory. Only use initially. Will be ignored by
     * <code>update()</code>.
     *
     * @param directory a <code>Directory</code> type.
     */
    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

}
