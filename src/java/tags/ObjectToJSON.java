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

import database.JSONable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author Tobias Kahse <tobias.kahse@outlook.com>
 */
public class ObjectToJSON extends SimpleTagSupport {

    private JSONable object;

    /**
     * Called by the container to invoke this tag. The implementation of this
     * method is provided by the tag library developer, and handles all tag
     * processing, body iteration, etc.
     *
     * @throws javax.servlet.jsp.JspException if an error occurs during writing
     * the JSON object.
     */
    @Override
    public void doTag() throws JspException {
        if (object != null) {
            //Use temporary output stream because jw.close() (called automatically by try) closes also the
            //underlying output stream and we do not want the jsp output stream to be closed...
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JspWriter out = getJspContext().getOut();
            JsonObject jo = object.toJSON();
            try (JsonWriter jw = Json.createWriter(baos)) {
                jw.writeObject(jo);
            }
            try {
                out.print(baos.toString());
            } catch (IOException ex) {
                Logger.getLogger(ObjectToJSON.class.getName()).log(Level.SEVERE, "IO exception during writing JSON string to JSP output. (" + ex + ")", ex);
                throw new JspException("IO exception during writing JSON string to JSP output. (" + ex + ")", ex);
            }
        } else {
            throw new JspException("Provided object is null!");
        }
    }

    public void setObject(JSONable object) {
        this.object = object;
    }

}
