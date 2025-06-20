import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        ServletContext context = getServletContext();
        Map<String, String> database = (Map<String, String>) context.getAttribute("database");
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match. Try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        if (database.containsKey(username)) {
            request.setAttribute("error", username + " already exists.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else {
            database.put(username, password);
            request.setAttribute("message", "Registration successful! Now log in.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
