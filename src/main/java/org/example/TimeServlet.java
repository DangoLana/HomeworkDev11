package org.example;

import jakarta.servlet.annotation.WebServlet;
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

        if (timezone == null) {
            timezone = "UTC";
        }

        TimeZone timeZone;
        try {
            timeZone = TimeZone.getTimeZone(timezone);
        } catch (Exception e) {
            timeZone = TimeZone.getTimeZone("UTC");
        }

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);

        String formattedTime = sdf.format(new Date(currentTimeMillis));

        response.setContentType("text/html");
        response.getWriter().write("<html><body>");
        response.getWriter().write("<h1>Current time: " + formattedTime + " " + timezone + "</h1>");
        response.getWriter().write("</body></html>");
    }
}
