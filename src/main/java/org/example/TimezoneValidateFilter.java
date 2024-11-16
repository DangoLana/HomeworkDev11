package org.example;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.TimeZone;

@WebFilter("/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String timezone = ((HttpServletRequest) request).getParameter("timezone");

        if (timezone != null) {
            TimeZone timeZone = TimeZone.getTimeZone(timezone);
                if (timeZone.getID().equals("GMT")) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_BAD_REQUEST); // HTTP код 400
                response.getWriter().write("<html><body><h1>Invalid timezone</h1></body></html>");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
