package userInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Logout extends BaseServlet{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = this.prepareResponse(response);
		HttpSession session = request.getSession();
		session.setAttribute(LOGIN, false);
		session.setAttribute(USERNAME, null);
		response.sendRedirect("/search");
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

	}
}
