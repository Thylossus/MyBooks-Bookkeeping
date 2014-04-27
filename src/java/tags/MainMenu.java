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

import controller.ScopeHandler;
import database.User;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import model.components.auth.CheckUserCredentials;

/**
 * Tag handler for the main menu tag.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class MainMenu extends SimpleTagSupport {

    //Probably needs to be transferre to another class.
    /**
     * The project's name.
     */
    private static final String PROJECT_NAME = "MyBooks";

    /**
     * The bean of the main menu.
     */
    private beans.MainMenu mainMenu;
    /**
     * The projet's base url.
     */
    private String baseUrl;
    /**
     * Request object.
     */
    private HttpServletRequest request;
    /**
     * Response object.
     */
    private HttpServletResponse response;

    /**
     * Process the tags functionality.
     *
     * @throws JspException if no main menu is specified.
     * @throws java.io.IOException if writing html is not possible.
     */
    @Override
    public void doTag() throws JspException, IOException {

        this.mainMenu = (beans.MainMenu) getJspContext().findAttribute("mainMenu");
        this.baseUrl = (String) getJspContext().findAttribute("baseUrl");
        this.request = (HttpServletRequest) ((PageContext) getJspContext()).getRequest();
        this.response = (HttpServletResponse) ((PageContext) getJspContext()).getResponse();

        if (this.baseUrl == null) {
            //The base url should be in the page context scope but to ensure that everythins works as expected
            //the base url is retrieved here again if it was not found.
            this.baseUrl = "http://" + this.request.getServerName() + ":" + this.request.getServerPort() + this.request.getContextPath();
        }
        if (this.mainMenu != null) {

            JspWriter out = getJspContext().getOut();
            out.print(this.buildHead());
            out.print(this.buildBody(this.mainMenu.getMenuItems()));
            out.print(this.buildTail());

        } else {
            throw new JspException("No main menu specified!");
        }

    }

    /**
     * Build the head of the menu's html code.
     *
     * @return head of the menu's html code.
     */
    private String buildHead() {
        return "<div class=\"navbar navbar-inverse navbar-fixed-top\" role=\"navigation\">\n"
                + "      <div class=\"container\">\n"
                + "        <div class=\"navbar-header\">\n"
                + "          <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">\n"
                + "            <span class=\"sr-only\">Toggle navigation</span>\n"
                + "            <span class=\"icon-bar\"></span>\n"
                + "            <span class=\"icon-bar\"></span>\n"
                + "            <span class=\"icon-bar\"></span>\n"
                + "          </button>\n"
                + "          <a class=\"navbar-brand\" href=\"" + this.baseUrl + "\"> " + MainMenu.PROJECT_NAME + "</a>\n"
                + "        </div>\n"
                + "        <div class=\"collapse navbar-collapse\">\n"
                + "          <ul class=\"nav navbar-nav\">\n";
    }

    /**
     * Build the body of the menu's html code. Submenus are built recursivley.
     *
     * @return body of the menu's html code.
     */
    private String buildBody(ArrayList<beans.MainMenuItem> items) {
        String body = "";

        for (beans.MainMenuItem item : items) {
            body += "<li";

            if (item.equals(this.mainMenu.getActiveItem())) {
                body += " class=\"active\"";
            }

            body += "><a href=\"";
            body += item.getLink();
            body += "\">";
            body += item.getLabel();

            body += "</a>";

            //Check whether a submenu is available.
            if (item.getSubmenu() != null && !item.getSubmenu().isEmpty()) {
                //TODO: check how to build a submenu with bootstrap...
                body += "";
                body += this.buildBody(item.getSubmenu());
                body += "";
            }

            body += "</li>\n";
        }

        return body;
    }

    /**
     * Build the tail of the menu's html code.
     *
     * @return tail of the menu's html code.
     */
    private String buildTail() {
        return "          </ul>\n"
                + this.buildAuthenticationMenu()
                + "        </div><!--/.nav-collapse -->\n"
                + "      </div>\n"
                + "    </div>";
    }

    private String buildAuthenticationMenu() {
        CheckUserCredentials cuc = new CheckUserCredentials(this.request, this.response);
        if (cuc.validateLogIn()) {
            User user = (User)ScopeHandler.getInstance().load(this.request, "user", "session");
            return "<ul class=\"nav navbar-nav pull-right\">\n"
                    + "  <li>\n"
                    + "    <a href=\"#\">"
                    + "      <span class=\"glyphicon glyphicon-user main-menu-glyphicon\"></span> "
                    + user.getFirstname() + " "
                    + user.getLastname()
                    + " ("
                    + user.getLastSignInDate().toString()
                    + ")"
                    + "    </a>"
                    + "  </li>\n"
                    + "  <li>\n"
                    + "    <a href=\"" + this.baseUrl + "/auth/signout\"><span class=\"glyphicon glyphicon-off main-menu-glyphicon\"></span> Sign out</a>"
                    + "  </li>\n"
                    + "</ul>";
        } else {
            return "<ul class=\"nav navbar-nav pull-right\">\n"
                    + "          <li class=\"dropdown\">\n"
                    + "            <a class=\"dropdown-toggle\" href=\"#\" data-toggle=\"dropdown\">Login</a>\n"
                    + "            <div class=\"dropdown-menu\">\n"
                    + "              <form class=\"form-signin\" role=\"form\" method=\"POST\" action=\"" + this.baseUrl + "/auth/signin\"> \n"
                    + "                <input name=\"mail\" class=\"form-control\" type=\"email\" placeholder=\"Email Address\" required autofocus> \n"
                    + "                <input name=\"password\" class=\"form-control\" type=\"password\" placeholder=\"Password\" required><br>\n"
                    + "                <button type=\"submit\" name=\"submit\" class=\"btn btn-lg btn-primary btn-block\">Login</button>\n"
                    + "              </form>\n"
                    + "            </div>\n"
                    + "          </li>\n"
                    + "        </ul>";
        }
    }

}
