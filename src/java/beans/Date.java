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

package beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Java Bean which wraps the functionality provided by <code>Gregorian Calendar</code>.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Date {

    /**
     * Calendar of the date.
     */
    private GregorianCalendar calendar;
    /**
     * Year.
     */
    private int year;
    /**
     * Month.
     */
    private int month;
    /**
     * Day of the month.
     */
    private int day;
    /**
     * Hour of the day (24h format).
     */
    private int hour;
    /**
     * Minute.
     */
    private int minute;
    /**
     * Second.
     */
    private int second;
    
    /**
     * The output format.
     */
    private String format;
    
    /**
     * Construct date (now).
     */
    public Date() {
        this.calendar = new GregorianCalendar();
        this.year = this.calendar.get(GregorianCalendar.YEAR);
        this.month = this.calendar.get(GregorianCalendar.MONTH);
        this.day = this.calendar.get(GregorianCalendar.DAY_OF_MONTH);
        this.hour = this.calendar.get(GregorianCalendar.HOUR_OF_DAY);
        this.minute = this.calendar.get(GregorianCalendar.MINUTE);
        this.second = this.calendar.get(GregorianCalendar.SECOND);
        this.format = "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * Set the calendar of the date and update all other fields accordingly.
     * @param calendar a calendar object.
     */
    public void setCalendar(GregorianCalendar calendar) {
        this.calendar = calendar;
        //Update other fields
        this.year = this.calendar.get(GregorianCalendar.YEAR);
        this.month = this.calendar.get(GregorianCalendar.MONTH);
        this.day = this.calendar.get(GregorianCalendar.DAY_OF_MONTH);
        this.hour = this.calendar.get(GregorianCalendar.HOUR_OF_DAY);
        this.minute = this.calendar.get(GregorianCalendar.MINUTE);
        this.second = this.calendar.get(GregorianCalendar.SECOND);
    }
    
    /**
     * Set the calendar of the date and update all other fields accordingly.
     * @param timestamp a timestamp (<code>java.sql.Timestamp</code>) object.
     */
    public void setCalendar(Timestamp timestamp) {
        this.calendar.setTimeInMillis(timestamp.getTime());
        //Update other fields
        this.year = this.calendar.get(GregorianCalendar.YEAR);
        this.month = this.calendar.get(GregorianCalendar.MONTH);
        this.day = this.calendar.get(GregorianCalendar.DAY_OF_MONTH);
        this.hour = this.calendar.get(GregorianCalendar.HOUR_OF_DAY);
        this.minute = this.calendar.get(GregorianCalendar.MINUTE);
        this.second = this.calendar.get(GregorianCalendar.SECOND);
    }
    
    /**
     * Set the calendar of the date and update all other fields accordingly.
     * @param date a date (<code>java.sql.Date</code>) object.
     */
    public void setCalendar(java.sql.Date date) {
        this.calendar.setTimeInMillis(date.getTime());
        //Update other fields
        this.year = this.calendar.get(GregorianCalendar.YEAR);
        this.month = this.calendar.get(GregorianCalendar.MONTH);
        this.day = this.calendar.get(GregorianCalendar.DAY_OF_MONTH);
        this.hour = this.calendar.get(GregorianCalendar.HOUR_OF_DAY);
        this.minute = this.calendar.get(GregorianCalendar.MINUTE);
        this.second = this.calendar.get(GregorianCalendar.SECOND);
    }
    
    /**
     * Get the date's calendar.
     * @return the date's calendar.
     */
    public GregorianCalendar getCalendar() {
        return this.calendar;
    }

    /**
     * Get the date's year.
     * @return the date's year.
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Get the date's month.
     * @return the date's month.
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Get the date's day of the month.
     * @return the date's day of the month.
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Get the date's hour of the day (24h format).
     * @return the date's hour of the day (24h format).
     */
    public int getHour() {
        return this.hour;
    }

    /**
     * Get the date's minute.
     * @return the date's minute.
     */
    public int getMinute() {
        return this.minute;
    }

    /**
     * Get the date's second.
     * @return the date's second.
     */
    public int getSecond() {
        return this.second;
    }

    /**
     * Get the date's printing format.
     * @return the date's printing format.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Set the date's printing format.
     * @param format a date format as a string.
     */
    public void setFormat(String format) {
        this.format = format;
    }
    
    /**
     * Provides a default string representation of the date.
     * @return the string representation of the date in the format yyyy-MM-dd hh:mm:ss.
     */
    @Override
    public String toString() {
        return this.toString(this.format);
    }
    
    /**
     * Provides a string representation of the date with the given format.
     * @param format the format as a string.
     * @return the formatted strin representation of the date.
     * @see SimpleDateFormat
     */
    public synchronized String toString(String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        sf.setCalendar(this.calendar);
        return sf.format(this.calendar.getTime());
    }
    
}
