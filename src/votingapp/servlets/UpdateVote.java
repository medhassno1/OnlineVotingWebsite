package votingapp.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import votingapp.account.AuthenticationDetails;
import votingapp.database.PollDB;
import votingapp.exceptions.InactivePollException;
import votingapp.exceptions.PollNotFoundException;
import votingapp.poll.PollDetails;

// this serlvet retrieves parameter from vote.jsp and display the details of the poll user wants to vote for
public class UpdateVote extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PollDB pollDB;
	String action_temp = null;
	String errorMessage = null;
	public UpdateVote() {
		super();

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		AuthenticationDetails logged = null;
		int pollID = 0;
		String redirect = null;
		

		// create new Poll Database object
		try {
			pollDB = new PollDB();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error: " + e);
		}

		try {
			logged = (AuthenticationDetails) session.getAttribute("logged");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error: " + e);
			redirect = "/MainController";

		}

		if (logged != null) {
			// user is already logged in, get current user's email
			String email = logged.getEmail();

			// retrieve the poll ID that user entered from HTTP request
			pollID = Integer.parseInt(request.getParameter("poll_ID"));
			request.setAttribute("pollID", pollID);
			session.setAttribute("pollID", pollID);
			boolean pollDeact = true;
			boolean existedVote = false;

			// retrieve value of "action_temp" from session
			action_temp = String.valueOf(session.getAttribute("action_temp"));

			// check if the poll with above poll ID is still active, and if the
			// user has already voted for this poll
			try {
				pollDeact = pollDB.pollDeactivated(pollID);
				existedVote = pollDB.existedVote(email, pollID);
				System.out.println("poll deactivated: " + pollDeact);
			} catch (PollNotFoundException | InactivePollException e) {
				errorMessage = "Invalid Poll ID. Please try again.";
				request.setAttribute("ERROR", errorMessage);
				System.out.println("error: " + e);
				redirect = "/vote.jsp";
			}

			
			if (pollDeact == false) {
				// poll is still active, create a new PollDetails object to store details of the poll
				PollDetails pd = new PollDetails(null, null, null, null, null, null, null, null);
				if (existedVote && action_temp.equals("VOTE")) {
					// if user choose to cast a vote for a poll he already
					// voted, redirect to main page
					errorMessage = "You have already voted for this poll. Please select 'Edit your vote' instead.";
					request.setAttribute("ERROR", errorMessage);
					redirect = "/main.jsp";
				} else if (!existedVote && action_temp.equals("EDITVOTE")) {

					// if user choose to edit his vote for a poll he has not yet
					// voted, redirect to main page
					errorMessage = "You have not voted for this poll. Please select 'Cast your vote' instead.";
					request.setAttribute("ERROR", errorMessage);
					redirect = "/main.jsp";
				} else {

					// otherwise, get the details of the poll with above poll ID and display them in seePollDetails.jsp for user to vote
					try {
						pd = pollDB.getPollDetails(pollID);
					} catch (PollNotFoundException e) {
						errorMessage = "Invalid Poll ID. Please try again.";
						request.setAttribute("ERROR", errorMessage);
						System.out.println("error: " + e);
						redirect = "/vote.jsp";
					}
					java.sql.Date deadline = pd.getDeadline();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String deadlineInString = formatter.format(deadline);
					request.setAttribute("title", pd.getTitle());
					request.setAttribute("question", pd.getQuestion());
					request.setAttribute("option1", pd.getOption1());
					request.setAttribute("option2", pd.getOption2());
					request.setAttribute("option3", pd.getOption3());
					request.setAttribute("option4", pd.getOption4());
					request.setAttribute("deadlineInString", deadlineInString);

					redirect = "/seePollDetails.jsp";
				}
			} else if (pollDeact) {
				// poll is inactive, redirect to vote.jsp
				errorMessage = "This poll is either inactive or unavailable. Please enter another valid pollID.";
				request.setAttribute("ERROR", errorMessage);
				System.out.println(errorMessage);
				redirect = "/vote.jsp";
			}

		} else if (action_temp == null) { 
			redirect = "/main.jsp";
		
		} else {
			// if user is not logged in, redirect to MainController servlet
			errorMessage = "You are not logged in.";
			request.setAttribute("ERROR", errorMessage);
			redirect = "/MainController";

		}
		dispatcher(request, response, redirect);
	}

	// this method creates a dispatcher object that redirects to the link stored
	// in string "redirect"
	private void dispatcher(HttpServletRequest request, HttpServletResponse response, String uri)
			throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher(uri);
		rd.forward(request, response);
	}

}
