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

package model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.components.*;
import model.components.auth.*;
import model.components.bsm.*;
import model.components.editor.category.*;
import model.components.editor.record.*;
import model.components.sysmgmt.*;
import model.components.user.*;

/**
 * Construct specific model components by a string identifier.
 * The string identifiers are given by the module component names in the specification using camel-case.
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public abstract class ModelComponentFactory {

    /**
     * Construct a module component based on an identifier.
     * @param request The request's request object
     * @param response The request's response object
     * @param identifier A string that identifies a module component.
     * @return the identified module component.
     * @throws Exception when the provided identifier does not identify any module component.
     */
    public static ModelComponent createModuleComponent(HttpServletRequest request, HttpServletResponse response, String identifier) throws Exception{
        switch (identifier) {
            case "CreateDataList":
                return new CreateDataList(request, response);
            case "SemanticInputValidation":
                return new SemanticInputValidation(request, response);
            case "SyntactialInputValidation":
                return new SyntactialInputValidation(request, response);
            case "RegisterUser":
                return new RegisterUser(request, response);
            case "CreateMainMenu":
                return new CreateMainMenu(request, response);
            case "CheckUserCredentials":
                return new CheckUserCredentials(request, response);
            case "CreateBalanceSheet":
                return new CreateBalanceSheet(request, response);
            case "LoadBalanceSheet":
                return new LoadBalanceSheet(request, response);
            case "CloseBalanceSheet":
                return new CloseBalanceSheet(request, response);
            case "DeleteBalanceSheet":
                return new DeleteBalanceSheet(request, response);
            case "CreateCategory":
                return new CreateCategory(request, response);
            case "DeleteCategory":
                return new DeleteCategory(request, response);
            case "LoadCategory":
                return new LoadCategory(request, response);
            case "UpdateCategory":
                return new UpdateCategory(request, response);
            case "ChangeRecords":
                return new ChangeRecords(request, response);
            case "CreateRecords":
                return new CreateRecords(request, response);
            case "DeleteRecords":
                return new DeleteRecords(request, response);
            case "GetCategoryRecords":
                return new GetCategoryRecords(request, response);
            case "LoadRecord":
                return new LoadRecord(request, response);
            case "LoadRecordsOfAMonth":
                return new LoadRecordsOfAMonth(request, response);
            case "DeleteUser":
                return new DeleteUser(request, response);
            case "GenerateSystemStatistics":
                return new GenerateSystemStatistics(request, response);
            case "AddPremiumMembership":
                return new AddPremiumMembership(request, response);
            case "CheckPremiumMembership":
                return new CheckPremiumMembership(request, response);
            case "StorePaymentDetails":
                return new StorePaymentDetails(request, response);
            case "UpdateUser":
                return new UpdateUser(request, response);
            default:
                throw new Exception("Invalid identifier for a model component!");
        }
    }
    
}
