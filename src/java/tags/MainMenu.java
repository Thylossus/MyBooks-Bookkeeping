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

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag handler for the main menu tag.
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class MainMenu extends SimpleTagSupport {

    /**
     * The bean of the main menu.
     */
    private beans.MainMenu mainMenu;

    /**
     * Process the tags functionality.
     *
     * @throws JspException if no main menu is specified.
     * @throws java.io.IOException if writing html is not possible.
     */
    @Override
    public void doTag() throws JspException, IOException {

        this.mainMenu = (beans.MainMenu) getJspContext().findAttribute("mainMenu");
       
        if (this.mainMenu != null) {

            JspWriter out = getJspContext().getOut();
            out.print(this.buildHead());
            out.print(this.buildBody());
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
                + "          <a class=\"navbar-brand\" href=\"" + this.mainMenu.getBaseURL() + "\"> " + this.mainMenu.getTitle() + "</a>\n"
                + "        </div>\n"
                + "        <div class=\"collapse navbar-collapse\">\n"
                + "          <ul class=\"nav navbar-nav\">\n";
    }

    /**
     * Build the body of the menu's html code. Submenus are built recursivley.
     *
     * @return body of the menu's html code.
     */
    private String buildBody() {
        String body = "";

        for (beans.MenuItem item : this.mainMenu.getMenuItems()) {
            body += item.toString();
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
                + this.mainMenu.getAuthenticationMenu().toString()
                + "        </div><!--/.nav-collapse -->\n"
                + "      </div>\n"
                + "    </div>";
    }


}
