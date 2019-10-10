package kz.edu.nu.cs.se;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet(urlPatterns = { "/myservlet" })
public class MyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MyServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        out.append(gson.toJson(new HelperClass()));
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

class HelperClass {
    final private static String[] firstNames = {"Cedar", "Cypress", "Fir", "Juniper", "Larch"};
    final private static String[] secondNames = {"Red", "Orange", "Green", "Blue", "Indigo", "Violet"};
    
    private String first;
    private String second;
    private String name;
    
    public HelperClass() {
        Random random = new Random();
        
        this.first = firstNames[random.nextInt(5)];
        this.second = secondNames[random.nextInt(6)];
        this.name = "Default";
    }
    
    public void setName(String s) {
        this.name = s;
    }
}