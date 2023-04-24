/*
 * ***********************************************************
 * Created by Dmitry I. Gornev  30 нояб. 2022
 * dig@inock.ru  https://t.me/inock
 * **********************************************************
 */

package ru.inock.webServletResime.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import static java.util.Objects.nonNull;

public class IdentificationFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;
        final HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        if (nonNull(user) && nonNull(role)) {
            switch (role) {
                case("OTHER"):
                    req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
                    break;
            }


        } else {
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, res);
        }

        chain.doFilter(request, response);
    }
}
