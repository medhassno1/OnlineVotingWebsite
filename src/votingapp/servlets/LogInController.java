package votingapp.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import votingapp.account.AuthenticationDetails;
import votingapp.database.PollDB;
import votingapp.database.VotingDB;
import votingapp.exceptions.IncorrectIDException;
import votingapp.exceptions.UserNotFoundException;

// this servlet retrieves parameters from login.jsp and logs the user in if the authentication details are correct
public class LogInController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       HttpSession session = null;
       String redirect = null;
       AuthenticationDetails logged = null;
       private VotingDB votingDB;
   
    public LogInController() {
        super();
        
    }


	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// retrieve parameters from HTTP request 
		String email = request.getParameter("email");
		String idkey = request.getParameter("idkey");
		
		// create new Voting database object
		try {
			votingDB = new VotingDB();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		boolean correctID = false;
		// Check if user details are correct
		try {
		
			correctID = votingDB.checkID(email, idkey);


		} catch (IncorrectIDException | UserNotFoundException e) {

			String errorMessage = "Invalid email or idkey. Please try again";
			request.setAttribute("errorMessage", errorMessage);
			redirect = "/login.jsp";
			// for debug purposes
			System.out.println("error: " + e);
		}
		
			
			if (correctID) {
				// details are correct, user is logged in
				// create a new session which lasts for 15 minutes, store user details into logged object and in session
				session = request.getSession();	
				logged = new AuthenticationDetails(email, idkey);
				session.setAttribute("logged", logged);
				session.setMaxInactiveInterval(900);
				redirect = "/MainController";
				
				//for debug purposes
				System.out.println("logged in @ LoginController");
				
				
			
			} else if (!correctID) {
				// user details are incorrect, store the error message into HTTP request to be displayed on JSP page
				String errorMessage = "Invalid email or idkey. Please try again";
				request.setAttribute("errorMessage", errorMessage);
				redirect = "/login.jsp";
				
			}
			
			dispatcher(request, response, redirect);
		
	}
	
	// this method creates a dispatcher object that redirects to the link stored in string "redirect"
	private void dispatcher(HttpServletRequest request, HttpServletResponse response, String uri)
			throws ServletException, IOException {


		RequestDispatcher rd = request.getRequestDispatcher(uri);
		rd.forward(request, response);
	}

}
