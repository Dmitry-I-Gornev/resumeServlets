/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  30 нояб. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import static ru.inock.webServletResime.util.SqlUtils.LogIn;

@WebServlet(name = "LogInServlet", value = "/LogInServlet")
public class LogInServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final HttpSession session = request.getSession();
        request.setCharacterEncoding("UTF-8");
        String userName = request.getParameter("LogIn");
        String userPassword =  request.getParameter("userPassword");
        String role;
        try {
            role = LogIn(userName,userPassword);
            //System.out.println(role);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        switch (role){
            case("OTHER"):
                eraseCookie(request,response);
                request.setAttribute("user", "LogIn");
                request.setAttribute("role", role);
                System.out.println("OTHER");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request,response);
            case("USER"):
                session.setMaxInactiveInterval(60*60*2);
                session.setAttribute("user",userName);
                session.setAttribute("role", role);
                /*Cookie c = new Cookie("JSESSION", session.getId());
                c.setMaxAge(60*60*24);
                response.addCookie(c);*/
                System.out.println("Resumes");
                response.sendRedirect("/Resumes");
        }
    }

    private void eraseCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }
    }
}
