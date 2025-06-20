import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        ServletContext context = getServletContext();
        Map<String, String> database = (Map<String, String>) context.getAttribute("database");

        if (database.containsKey(username) && database.get(username).equals(password)) {
            request.setAttribute("username", username);
            request.getRequestDispatcher("/welcome.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Wrong username or password. Try again.");
            request.getRequestDispatcher("/tryAgain.jsp").forward(request, response);
        }
    }
}
