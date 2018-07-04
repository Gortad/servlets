package com;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        java.io.Writer writer = response.getWriter();
        writer.append("<html>");
        writer.append("<body>");
        writer.append("<h1>Hello world</h1>");
        writer.append("</body>");
        writer.append("</html>");
    }
}