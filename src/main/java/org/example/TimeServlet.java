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
import java.util.logging.Logger;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(TimeServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String timezone = request.getParameter("timezone");

        logger.info("Received timezone parameter: " + timezone);

        if (timezone == null) {
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

        logger.info("Processed timezone: " + timezone);

        TimeZone timeZone = TimeZone.getTimeZone(timezone);

        logger.info("Resolved TimeZone: " + timeZone.getID());

        if (timeZone.getID().equals("GMT")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("<html><body><h1>Invalid timezone</h1></body></html>");
            return;
        }

        Cookie timezoneCookie = new Cookie("lastTimezone", timezone);
        timezoneCookie.setMaxAge(60 * 60 * 24 * 30); // Термін дії cookie - 30 днів
        response.addCookie(timezoneCookie);

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
