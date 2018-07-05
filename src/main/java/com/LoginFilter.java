package com;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        java.io.Writer writer = response.getWriter();

        String action = httpRequest.getParameter("action");

        if (action != null && action.equals("login")) {
            String user = httpRequest.getParameter("userId");
            String password = httpRequest.getParameter("password");
            if (user.equals("user") && password.equals("123456")) {
                writer.append("test");
                httpRequest.getSession().setAttribute("userId", "user");
            }
        }

        if (action != null && action.equals("logout")) {
            httpRequest.getSession().invalidate();
        }

        String userId = (String) httpRequest.getSession().getAttribute("userId");


        if (userId == null) {
            writer.append("<html>");
            writer.append("<body>");
            writer.append("<form action='?action=login' method='post'>");
            writer.append("Użytkownik: <input type='text' name='userId'> <br>");
            writer.append("Hasło: <input type='password' name='password'> <br>");
            writer.append("<input type='submit' value='Zaloguj'>");
            writer.append("</form>");
            writer.append("</html>");
            writer.append("</body>");
            return;
        }

        chain.doFilter(request, response);
    }

    public void destroy() {

    }
}
