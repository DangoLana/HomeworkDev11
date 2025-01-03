package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String timezone = request.getParameter("timezone");

        if (timezone != null) {
            TimeZone timeZone = TimeZone.getTimeZone(timezone);
            if (!timeZone.getID().equals("GMT")) {
                Cookie timezoneCookie = new Cookie("lastTimezone", timezone);
                timezoneCookie.setMaxAge(60 * 60 * 24 * 30);
                response.addCookie(timezoneCookie);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("<html><body><h1>Invalid timezone</h1></body></html>");
                return;
            }
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("lastTimezone")) {
                        timezone = cookie.getValue();
                        break;
                    }
                }
            }

            if (timezone == null) {
                timezone = "UTC";
            }
        }

        if (timezone.startsWith("UTC")) {
            timezone = timezone.replace("UTC", "GMT");
        }

        TimeZone timeZone = TimeZone.getTimeZone(timezone);

        if (timeZone.getID().equals("GMT")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("<html><body><h1>Invalid timezone</h1></body></html>");
            return;
        }

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);

        String formattedTime = sdf.format(new Date(currentTimeMillis));

        response.setContentType("text/html");
        response.getWriter().write("<html><body>");
        response.getWriter().write("<h1>Current time: " + formattedTime + "</h1>");
        response.getWriter().write("<h1>Timezone: " + timezone + "</h1>");
        response.getWriter().write("</body></html>");
    }
}
