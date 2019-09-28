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
    private Gson gson = new Gson();

    public MyServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Random random = new Random();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (request.getParameter("reset") != null) {
            request.getSession().invalidate();
        }

        MyPosition myPosition = (MyPosition) request.getSession().getAttribute("myPosition");

        String chain = request.getParameter("chain");

        if (myPosition == null) {
            Integer steps = Integer.valueOf(1);
            Integer expected = Integer.valueOf(random.nextInt(1000));
            request.getSession().setAttribute("myPosition", new MyPosition(steps, expected));
            myPosition = (MyPosition) request.getSession().getAttribute("myPosition");
        } else {
            if (chain != null) {
                if (Integer.parseInt(chain) == myPosition.expected.intValue()) {
                    request.getSession().setAttribute("myPosition", myPosition.advance(random.nextInt(1000)));
                    myPosition = (MyPosition) request.getSession().getAttribute("myPosition");
                }
            }
        }
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        out.append(gson.toJson(myPosition));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

class MyPosition {
    public final Integer steps;
    public final Integer expected;
    
    public MyPosition(Integer steps, Integer expected) {
        this.steps = steps;
        this.expected = expected;
    }
    
    public MyPosition advance(int i) {
        return new MyPosition(this.steps + 1, i);
    }
}
