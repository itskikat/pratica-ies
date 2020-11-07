package pt.ua.u93102;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// via - https://www.tutorialride.com/servlets/servlets-request-response-interface.htm

@WebServlet(name="CustomizedMessageName", urlPatterns="/CustomizedMessageName")
public class CustomizedMessageName extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		try{
			String username = req.getParameter("username");
			if (username != null){
				out.println("<html><body>");
				out.println("Hello there, " + username + "!");
				out.println("</body></html>");
			}
			else {
				out.println("<html><body>");
				out.println("Hello World!");
				out.println("<p>In order to receive a custom message from the server, be sure to put your name in the url!</p>");
				out.println("<p> 'http://localhost:8080/lab2.1Tomcat-1.0-SNAPSHOT/CustomizedMessageName?username=[YOUR NAME HERE]' </p>");
				out.println("</body></html>");
			}
		}
		finally{
			out.close();
		}
		
		
	}
}
