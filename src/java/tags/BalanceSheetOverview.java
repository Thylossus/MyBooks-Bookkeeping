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
import database.BalanceSheet;
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

/**
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class BalanceSheetOverview extends BodyTagSupport {

    private String order;
    private ArrayList<BalanceSheet> data;
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
        if (this.data == null || this.data.isEmpty()) {
            
           //load data
            HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
            User user = (User) ScopeHandler.getInstance().load(request, "user", "session");
            if (user != null) {
                URLProber up = new URLProber("/MyBooks-Bookkeeping/bsm/balancesheets/orderby-" + this.order.toLowerCase());
                ScopeHandler.getInstance().store(request, "url", up);
                ScopeHandler.getInstance().store(request, "dispatch", false);
                try {
                    new Controller().processRequest(request, response);
                } catch (ServletException | IOException ex) {
                    Logger.getLogger(BalanceSheetOverview.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                this.data = (ArrayList<BalanceSheet>) ScopeHandler.getInstance().load(request, "dataList");

                //Check if data list is still empty
                if (this.data == null || this.data.isEmpty()) {
                    return SKIP_BODY;
                }
            }

        }

        this.provideValuesToPageContext(this.data.get(0));
        this.counter = 1;
        try {
            this.pageContext.getOut().print("<ul class=\"balance-sheet-list\">");
        } catch (IOException ex) {
            Logger.getLogger(BalanceSheetOverview.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
        try {
            this.pageContext.getOut().print("</ul>");
        } catch (IOException ex) {
            Logger.getLogger(BalanceSheetOverview.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
        
        this.pageContext.setAttribute("bsTitle", title);
        this.pageContext.setAttribute("bsEdited", edited);
        this.pageContext.setAttribute("bsCreated", created);
    }

}
