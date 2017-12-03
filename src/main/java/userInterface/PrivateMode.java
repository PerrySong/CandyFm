package userInterface;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PrivateMode extends BaseServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {

	}
	
	//This doPost method is to change the search mode. If the mode is private, change it to public
	//If the mode is public, change it to private.
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		//Change the mode
		if((boolean)session.getAttribute(PRIVATE)) {
			session.setAttribute(PRIVATE, false);
		} else {
			session.setAttribute(PRIVATE, true);
		}

		try {
			response.sendRedirect(response.encodeRedirectURL("/search"));
		} catch (IOException e) {
			System.out.println("PrivateMode Exception!!" + e.getMessage());
		}
	}
}
