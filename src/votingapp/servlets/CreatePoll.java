package votingapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import votingapp.account.AuthenticationDetails;
import votingapp.database.PollDB;
import votingapp.exceptions.ExistedPollException;
import votingapp.exceptions.UserNotFoundException;
import votingapp.poll.PollDetails;

// this servlet retrieves parameter from createPoll.jsp and create a new poll by saving the poll's details into Poll database
public class CreatePoll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PollDB pollDB;
	String redirect = null;
	int pollID = 0;

	public CreatePoll() {
		super();

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// get the current session
		HttpSession session = request.getSession(false);

		// create an AuthenticationDetails object to store email and ID key of user
		AuthenticationDetails logged = null;

		// create new Poll database object
		try {
			pollDB = new PollDB();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error: " + e);
		}

		// retrieve value of the AuthenticationDetails object from session if available
		try {
			logged = (AuthenticationDetails) session.getAttribute("logged");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error: " + e);
			redirect = "/MainController";

		}

		if (logged != null) {

			// user is already logged in, retrieve the poll's details from HTTP session and HTTP request
			String owner = logged.getEmail();
			String title = request.getParameter("title");
			String question = request.getParameter("question");
			String option1 = request.getParameter("option1");
			String option2 = request.getParameter("option2");
			String option3 = request.getParameter("option3");
			String option4 = request.getParameter("option4");
			String deadlineInString = request.getParameter("deadlineInString");

			// cast the string deadlineInString into type java.sql.Date
			java.text.DateFormat formatter = java.text.DateFormat.getDateInstance();
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date udate = new java.util.Date();
			java.sql.Date deadline = null;
			try {
				udate = formatter.parse(deadlineInString);
				deadline = new java.sql.Date(udate.getTime());
				
				// for debug purposes
				System.out.println(deadline);

			} catch (ParseException e) {
				e.printStackTrace();
				System.out.println("error: " + e);
			}

			try {
				// store retrieved poll details into new PollDetails object
				PollDetails pd = new PollDetails(owner, title, question, option1, option2, option3, option4, deadline);

				// save the retrieved poll details into database
				pollDB.createPoll(pd);
				// for debug purposes
				System.out.println("poll created");

				// retrieve the poll ID of the last created poll
				String email = owner;
				pollID = pollDB.lastInsertedPollID(email);
				request.setAttribute("pollID", pollID);

				// for debug purposes
				System.out.println("created poll ID: " + pollID);
				
				// display JSP page to inform user of the poll ID of successfully created poll 
				redirect = "/pollCreated.jsp";
			} catch (ExistedPollException e) {
				String errorMessage = "Poll already exists. Please enter another poll title.";
				request.setAttribute("ERROR", errorMessage);
				System.out.println("error: " + e);
				redirect = "/createPoll.jsp";
			} catch (UserNotFoundException e) {
				String errorMessage = "Invalid email";
				request.setAttribute("ERROR", errorMessage);
				System.out.println("error: " + e);
				redirect = "/createPoll.jsp";
			}

		} else if (logged == null) {
			// if user is not logged in, redirect to MainController servlet
			String errorMessage = "You are not logged in.";
			request.setAttribute("ERROR", errorMessage);
			redirect = "/MainController";

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
