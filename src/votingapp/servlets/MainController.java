package votingapp.servlets;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.catalina.connector.Request;

import java.util.*;
import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import votingapp.account.*;
import votingapp.database.*;
import votingapp.exceptions.*;
import votingapp.poll.PollDetails;
import votingapp.poll.PollResults;

// this servlet retrieves parameters from main.jsp and redirects the user to other JSPs depending on the action chosen
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VotingDB votingDB;
	private PollDB pollDB;
	HttpSession session = null;

	public MainController() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// create new database objects
		try {
			votingDB = new VotingDB();
			pollDB = new PollDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// get the current session
		session = request.getSession(false);
		
		
		String redirect = null;
		int pollID = 0;
		AuthenticationDetails logged = null;
		// retrieve the "action" parameter from HTTP request
		String action = request.getParameter("action");
		
		// retrieve value of the AuthenticationDetails object from session if available
		try {
			logged = (AuthenticationDetails) session.getAttribute("logged");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("not logged. error: " + e);
		}
		
		
		if (logged == null) {
			// user is not logged in, redirect to index.jsp
			redirect = "/index.jsp";
			String errorMessage = "You are not logged in.";
			request.setAttribute("ERROR", errorMessage);
			
			// for debug purposes
			System.out.println("logged is null. session: " + session);
			
		}
		
		else if (action == null && logged != null) {
			// user is already logged in but there is no chosen action, redirect to main page
			redirect = "/main.jsp";
			
			// for debug purposes
			System.out.println("action: " + action);
			System.out.println("session: " + session);
		}
		
		else if (action != null && logged != null) {
			// for debug purposes
			System.out.println("action: " + action);
			System.out.println("session: " + session);
			System.out.println("logged: " + logged);
			
			// user is already logged in, check for the value of string "action" and redirect to corresponding page
			if (action.equals("LOGOUT")) {

				// Invalidate Session

				session.invalidate();
				session = null;
				redirect ="/index.jsp";
				// for debug purposes
				System.out.println("Logged out");

			} else if (action.equals("VOTE") || action.equals("EDITVOTE") ) {
				// store current value of action into a new string "action_temp" 
				String action_temp = request.getParameter("action");
				
				// store "action_temp" into session to be used later in UpdateVote servlet and VoterOrEditVote servlet
				session.setAttribute("action_temp", action_temp);
				redirect = "/vote.jsp";
				
			} 
				
			 else if (action.equals("CREATEPOLL")) {
				redirect = "/createPoll.jsp";
			
			}  else if (action.equals("MANAGEPOLLS")) {
				redirect = "/managePoll.jsp";
			
			} else {
				redirect = "/index.jsp";
			}

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
