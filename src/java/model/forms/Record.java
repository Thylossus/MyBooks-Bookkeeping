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
import java.util.GregorianCalendar;
import model.components.Input;

/**
 * Context for the create and change record forms.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class Record extends Context {

    /**
     * The record's title.
     */
    private String title;
    /**
     * The record's description.
     */
    private String description;
    /**
     * The record's amount.
     */
    private double amount;
    /**
     * The record's date.
     */
    private GregorianCalendar date;
    /**
     * The id of the record's category.
     */
    private int category;

    /**
     * Construct the record context.
     *
     * @param inputList
     */
    public Record(ArrayList<Input> inputList) {
        this.inputList = inputList;
        
        this.title = null;
        this.description = null;
        this.amount = 0.0;
        this.date = null;
        this.category = 0;
    }

    /**
     * Validate the given inputs for the record context.
     *
     * @return
     */
    @Override
    public boolean validate() {
        //Check required parameters
        //Check title
        if (inputList.get(0).getKey().equals("title")) {
            this.title = (String) inputList.get(0).getValue();
        } else {
            this.title = (String) this.searchInputValue("title");
            if (this.title == null) {
                return false;
            }
        }
        //Check description
        if (inputList.get(1).getKey().equals("description")) {
            this.description = (String) inputList.get(1).getValue();
        } else {
            this.description = (String) this.searchInputValue("description");
            if (this.description == null) {
                return false;
            }
        }
        //Check amount
        if (inputList.get(2).getKey().equals("amount")) {
            this.amount = (double) inputList.get(2).getValue();
        } else {
            this.amount = (double) this.searchInputValue("amount");
        }
        //Check date
        if (inputList.get(3).getKey().equals("date")) {
            this.date = (GregorianCalendar) inputList.get(3).getValue();
        } else {
            this.date = (GregorianCalendar) this.searchInputValue("date");
            if (this.date == null) {
                return false;
            }
        }
        //Check category
        if (inputList.get(4).getKey().equals("category")) {
            this.category = (int) inputList.get(4).getValue();
        } else {
            this.category = (int) this.searchInputValue("category");
            if (this.category == 0) {
                return false;
            }
        }

        //Check length of title and description
        if (this.title.length() > 30) {
            return false;
        }
        if (this.description.length() > 1500) {
            return false;
        }

        return true;
    }

}
