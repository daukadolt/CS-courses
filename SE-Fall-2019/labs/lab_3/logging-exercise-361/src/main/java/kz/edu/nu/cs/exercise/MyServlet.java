package kz.edu.nu.cs.exercise;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = { "/myservlet" })
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static ArrayList logs;
    public MyServlet() {
        super(); this.logs = new ArrayList();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String logLine = "Served at: " + dtf.format(now)  + " at remote host: " + request.getRemoteHost() +
                " at path: " + request.getRequestURL();
        this.logs.add(logLine);
        response.getWriter()
                .append(generateLogs());
//                .append("Served at: ")
//                .append(request.getContextPath())
//                .append(" at remote host ")
//                .append(request.getRemoteHost())
//                .append(" at path: ").append(request.getContextPath());

    }

    protected String generateLogs() {
        String allLogs = "";
        for(int i = 0; i<this.logs.size(); i++) {
            allLogs += this.logs.get(i) + "\n";
        }
        return allLogs;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
