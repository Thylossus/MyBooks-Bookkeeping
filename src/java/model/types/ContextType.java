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

package model.types;

/**
 * Enumerator for context types.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public enum ContextType {
    /**
     * Article context.
     */
    ARTICLE,
    /**
     * Balance sheet context.
     */
    BALANCE_SHEET,
    /**
     * Category context.
     */
    CATEGORY,
    /**
     * Edit uploaded file context.
     */
    EDIT_UPLOADED_FILE,
    /**
     * Password reset context.
     */
    PASSWORD_RESET,
    /**
     * Premium membership context.
     */
    PREMIUM_MEMBERSHIP,
    /**
     * Record context.
     */
    RECORD,
    /**
     * Sign in context.
     */
    SIGN_IN,
    /**
     * Upload file context.
     */
    UPLOAD_FILE,
    /**
     * User details context.
     */
    USER_DETAILS;
    
}