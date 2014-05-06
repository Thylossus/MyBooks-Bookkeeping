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




//Define Months
var MONTH = {
    JAN: {value: 0, name: "January"},
    FEB: {value: 1, name: "February"},
    MAR: {value: 2, name: "March"},
    APR: {value: 3, name: "April"},
    MAY: {value: 4, name: "May"},
    JUN: {value: 5, name: "June"},
    JUL: {value: 6, name: "July"},
    AUG: {value: 7, name: "August"},
    SEP: {value: 8, name: "September"},
    OCT: {value: 9, name: "October"},
    NOV: {value: 10, name: "November"},
    DEC: {value: 11, name: "December"},
    next: function(month) {
        if (month.value === 11) {
            return MONTH.monthByValue(0);
        } else {
            return MONTH.monthByValue(month.value + 1);
        }
    },
    previous: function(month) {
        if (month.value === 0) {
            return MONTH.monthByValue(11);
        } else {
            return MONTH.monthByValue(month.value - 1);
        }
    },
    monthByValue: function(value) {
        switch (value) {
            case 0:
                return MONTH.JAN;
            case 1:
                return MONTH.FEB;
            case 2:
                return MONTH.MAR;
            case 3:
                return MONTH.APR;
            case 4:
                return MONTH.MAY;
            case 5:
                return MONTH.JUN;
            case 6:
                return MONTH.JUL;
            case 7:
                return MONTH.AUG;
            case 8:
                return MONTH.SEP;
            case 9:
                return MONTH.OCT;
            case 10:
                return MONTH.NOV;
            case 11:
                return MONTH.DEC;
        }
    }
};
