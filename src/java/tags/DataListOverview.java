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
package tags;

import beans.Date;
import controller.Controller;
import controller.ScopeHandler;
import controller.URLProber;
import database.ActiveRecord;
import database.Article;
import database.BalanceSheet;
import database.Category;
import database.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import database.Record;

/**
 * Parses a list of active records and provides variables to the page context
 * that can be used to print the body. Does not provide the container for the
 * generated elements!
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class DataListOverview extends BodyTagSupport {

    private String type;
    private String order;
    private ArrayList<ActiveRecord> data;
    private int counter;

    /**
     * This method is called when the JSP engine encounters the start tag, after
     * the attributes are processed. Scripting variables (if any) have their
     * values set here.
     *
     * @return EVAL_BODY_BUFFERED if the JSP engine should evaluate the tag
     * body, otherwise return SKIP_BODY.
     * @throws javax.servlet.jsp.JspException
     */
    @Override
    public int doStartTag() throws JspException {
        if (this.data == null) {

            //load data 
            HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
            User user = (User) ScopeHandler.getInstance().load(request, "user", "session");
            if (user != null) {
                URLProber up;
                switch (this.type) {
                    case "BalanceSheet":
                        up = new URLProber("/MyBooks-Bookkeeping/bsm/balancesheets/orderby-" + this.order.toLowerCase());
                        break;
                    default:
                        throw new JspException("Could not load data because specified type is not supported.");
                }

                ScopeHandler.getInstance().store(request, "url", up);
                ScopeHandler.getInstance().store(request, "dispatch", false);
                try {
                    new Controller().processRequest(request, response);
                } catch (ServletException | IOException ex) {
                    Logger.getLogger(DataListOverview.class.getName()).log(Level.SEVERE, null, ex);
                }

                this.data = (ArrayList<ActiveRecord>) ScopeHandler.getInstance().load(request, "dataList");

                //Check if data list is still empty
                if (this.data == null) {
                    return SKIP_BODY;
                }
            }

        }

        if (data.isEmpty()) {
            return SKIP_BODY;
        }

        this.provideValuesToPageContext(this.data.get(0));
        this.counter = 1;

        return EVAL_BODY_INCLUDE;

    }

    /**
     * This method is called after the JSP engine finished processing the tag.
     *
     * @return EVAL_PAGE.
     * @throws javax.servlet.jsp.JspException
     */
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /**
     * This method is called after the JSP engine processes the body content of
     * the tag.
     *
     * @return EVAL_BODY_AGAIN if the JSP engine should evaluate the tag body
     * again, otherwise return SKIP_BODY.
     * @throws javax.servlet.jsp.JspException
     */
    @Override
    public int doAfterBody() throws JspException {
        if (this.counter < this.data.size()) {
            this.provideValuesToPageContext(this.data.get(this.counter));
            this.counter++;
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }

    /**
     * Set the order attribute.
     *
     * @param order value of the order attribute.
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Set the data attibute.
     *
     * @param data value of the data attribute.
     */
    public void setData(ArrayList data) {
        this.data = data;
    }

    /**
     * Set the type attribute.
     *
     * @param type value of the type attribute.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Call the <code>provideValuesToPageContext</code> method, which is
     * appropriate to the type of data.
     *
     * @param data an active record.
     * @throws JspException if the type of active record is not supported.
     */
    private void provideValuesToPageContext(ActiveRecord data) throws JspException {
        if (data instanceof BalanceSheet) {
            this.provideValuesToPageContext((BalanceSheet) data);
        } else if (data instanceof Record) {
            this.provideValuesToPageContext((Record) data);
        } else if (data instanceof Category) {
            this.provideValuesToPageContext((Category) data);
        } else if (data instanceof User) {
            this.provideValuesToPageContext((User) data);
        } else if (data instanceof Article) {
            this.provideValuesToPageContext((Article) data);
        } else {
            throw new JspException("Type of active record is not supported!");
        }
    }

    /**
     * Provide the parameters required by the body to the page context.
     *
     * @param data a balance sheet object.
     */
    private void provideValuesToPageContext(BalanceSheet data) {
        String title = data.getTitle();
        Date editedDate = new Date();
        Date createdDate = new Date();

        editedDate.setCalendar(data.getDateOfLastChange());
        createdDate.setCalendar(data.getDateOfCreation());

        String edited = editedDate.toString();
        String created = createdDate.toString();

        this.pageContext.setAttribute("bsId", data.getId());
        this.pageContext.setAttribute("bsTitle", title);
        this.pageContext.setAttribute("bsEdited", edited);
        this.pageContext.setAttribute("bsCreated", created);
    }

    /**
     * Provide the parameters required by the body to the page context.
     *
     * @param data a balance sheet object.
     */
    private void provideValuesToPageContext(Record data) {
        int balanceSheet = data.getBalanceSheet();
        int recordId = data.getId();
        String title = data.getTitle();
        String description = data.getDescription();
        double amount = data.getAmount();
        Date dateRecordDate = new Date();
        int rCatId = data.getCatId();
        String catName = data.getCatName();
        String catColour = data.getCatColour();

        dateRecordDate.setCalendar(data.getRecordDate());

        String recordDate = dateRecordDate.toString("yyyy-MM-dd");

        this.pageContext.setAttribute("rBalanceSheet", balanceSheet);
        this.pageContext.setAttribute("rId", recordId);
        this.pageContext.setAttribute("rTitle", title);
        this.pageContext.setAttribute("rDescription", description);
        this.pageContext.setAttribute("rAmount", amount);
        this.pageContext.setAttribute("rDate", recordDate);
        this.pageContext.setAttribute("rCatId", rCatId);
        this.pageContext.setAttribute("rCatName", catName);
        this.pageContext.setAttribute("rCatColour", catColour);
    }

    /**
     * Provide the parameters required by the body to the page context.
     *
     * @param data a category object.
     */
    private void provideValuesToPageContext(Category data) {
        int cId = data.getId();
        String cName = data.getName();
        String cColour = data.getColour();

        this.pageContext.setAttribute("cId", cId);
        this.pageContext.setAttribute("cName", cName);
        this.pageContext.setAttribute("cColour", cColour);
    }

    /**
     * Provide the parameters required by the body to the page context.
     *
     * @param data a user object.
     */
    private void provideValuesToPageContext(User data) {
        int uId = data.getId();
        String firstname = data.getFirstname();
        String lastname = data.getLastname();
        String mail = data.getMail();
        Date dateLastSignInDate = new Date();
        dateLastSignInDate.setCalendar(data.getLastSignInDate());
        Date dateSignUpDate = new Date();
        dateSignUpDate.setCalendar(data.getSignUpDate());
        String stringLastSignInDate = dateLastSignInDate.toString();
        String stringSignUpDate = dateSignUpDate.toString("yyyy-MM-dd");
        String userType = data.getUserType().getIdentifier();

        this.pageContext.setAttribute("id", uId);
        this.pageContext.setAttribute("firstname", firstname);
        this.pageContext.setAttribute("lastname", lastname);
        this.pageContext.setAttribute("mail", mail);
        this.pageContext.setAttribute("lastSignInDate", stringLastSignInDate);
        this.pageContext.setAttribute("signUpDate", stringSignUpDate);
        this.pageContext.setAttribute("userType", userType);
    }

    /**
     * Provide the parameters required by the body to the page context.
     *
     * @param data an article object.
     */
    private void provideValuesToPageContext(Article data) {
        Date date = new Date();
        date.setCalendar(data.getDate());

        this.pageContext.setAttribute("title", data.getTitle());
        this.pageContext.setAttribute("date", date);
        this.pageContext.setAttribute("author", data.getAuthorName() + "(" + data.getAuthorMail() + ")");
        this.pageContext.setAttribute("content", data.getContent());
        if (data.getEditorId() != 0) {
            this.pageContext.setAttribute("editor", data.getEditorName() + "(" + data.getEditorMail() + ")");

            Date editDate = new Date();
            editDate.setCalendar(data.getEditDate());

            this.pageContext.setAttribute("editDate", editDate);
        } else {
            this.pageContext.setAttribute("editor", null);
            this.pageContext.setAttribute("editDate", null);
        }
    }

}
