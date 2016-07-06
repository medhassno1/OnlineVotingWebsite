package votingapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import votingapp.account.AuthenticationDetails;
import votingapp.database.PollDB;
import votingapp.exceptions.PollNotFoundException;
import votingapp.poll.PollResults;

// this servlet retrieves parameter from seePollDetails.jsp and save the user's vote to database 
public class VoteOrEditVote extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PollDB pollDB;

	public VoteOrEditVote() {
		super();
		
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// to display HTML page
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		// get the current session
		HttpSession session = request.getSession(false);
		AuthenticationDetails logged = null;
		String redirect = null;
		String action_temp = null;
		int pollID = 0;
		
		// create new Poll database object
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
			// user is already logged in, retrieve current user's email
			String email = logged.getEmail();
			
			// retrieve user's choice from seePollDetails.jsp
			String choice = request.getParameter("choice");
			String confirm = request.getParameter("confirm");
			
			// retrieve value of string "action_temp" and "pollID" from session
			action_temp = String.valueOf(session.getAttribute("action_temp"));
			pollID = (int) session.getAttribute("pollID");
			
				if (confirm.equals("Cancel")) {
				// user discards the vote, redirect to vote.jsp
				redirect = "/vote.jsp";
				dispatcher(request, response, redirect);
				} else if (confirm.equals("Confirm")) {
					// user confirms the vote, store the user's email and the vote to a new PollResults object
					PollResults pr = new PollResults(pollID, email, choice);
					try {
					if (action_temp.equals("VOTE")){
							// if user chooses to cast a vote, insert the PollReulst object it to database 
							pollDB.updatePoll(pr);
							System.out.println("voted");
							
						
					} else if (action_temp.equals("EDITVOTE")) {
						// if user chooses to edit his vote, update his vote in database
						pollDB.editVote(pr);
						System.out.println("vote updated");
						
					}
					// display HTML page to informs user of successful vote
					out.println("<html><head><title>Vote sucessful</title></head>");
					out.println("<body>");
					out.println("Thank you for voting.<br><br>");
					out.println("You have successfully voted for " + choice + ".<br><br>");
					out.println("If you wish to change your vote, you may go back or log in again to revote.<br>");
					out.println(
							"<br><form><input type=button onClick=\"location.href='/OnlineVotingTestNew/main.jsp'\" value='Back'>");
					out.println(
							"<input type=button onClick=\"location.href='/OnlineVotingTestNew/MainController?action=LOGOUT'\" value='Log out'></form>");
					out.println("</body></html>");
					} catch (PollNotFoundException e) {
					String errorMessage = "Invalid poll ID. Please try again.";
					request.setAttribute("ERROR", errorMessage);
					System.out.println("error: " + e);
					e.printStackTrace();
					} 
				}
		} else if (logged == null || action_temp == null){
			// user is not logged in, redirect to MainController servlet
			String errorMessage = "You are not logged in.";
			request.setAttribute("ERROR", errorMessage);
			redirect = "/MainController";
			dispatcher(request, response, redirect);
			
		}
		
	}

	// this method creates a dispatcher object that redirects to the link stored in string "redirect"
	private void dispatcher(HttpServletRequest request, HttpServletResponse response, String uri)
			throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher(uri);
		rd.forward(request, response);
	}

}
