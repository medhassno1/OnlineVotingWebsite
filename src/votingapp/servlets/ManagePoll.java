package votingapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import votingapp.account.AuthenticationDetails;
import votingapp.database.PollDB;
import votingapp.exceptions.PollNotFoundException;
import votingapp.poll.PollDetails;

// this servlet retrieves parameter from managePoll.jsp and pollOutcome.jsp
public class ManagePoll extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PollDB pollDB;
	String redirect = null;
	AuthenticationDetails logged = null;
	String deadlineInString = null;
	int selectedPollID = 0;
	String errorMessage = null;
	String action = null;
	PollDetails pd = new PollDetails(null, null, null, null, null, null, null, null);
	public ManagePoll() {
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
			action = (String) request.getParameter("action");

			// for debug purposes
			System.out.println("action: " + action);

			if (action.equals("SHOW_JOINED_POLLS")) {
				// create a new HashMap object to store the poll ID and the
				// choice of each poll that user has joined in
				HashMap<Integer, String> table;
				try {
					// retrieve the value of this HashMap object from database,
					// with poll ID as key and choice as value
					table = pollDB.getJoinedPolls(email);
					// for debug purposes
					System.out.println("joined polls: " + table);
					// create a new Iterator object
					Iterator it = table.entrySet().iterator();

					// display HTML page
					out.println("<html><head><title>Polls you have joined</title></head>");
					out.println("<body>Polls you have joined:<br><br><table border=\"1\" style=\"width:50%\">");
					out.println("<tr><th>Poll ID</th><th>Choice</th><th>Deadline</th></tr><tr>");

					// create a new String array
					
					// iterate through the retrieved HashMap and display it as a table
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						// retrieve from database the deadlines of the polls in
						// HashMap and store them into the PollDetails object accordingly
						pd = pollDB.getPollDetails((int) (pair.getKey()));
						java.sql.Date deadline = pd.getDeadline();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						deadlineInString = formatter.format(deadline);

						// display to user all poll IDs, corresponding choices
						// and deadlines that he has joined in
						out.println("<td align=\"center\">" + pair.getKey() + "</td><td align=\"center\">"
								+ pair.getValue() + "</td><td align=\"center\">" + deadlineInString + "</td></tr>");

					}

					out.println("<br></table>");
					out.println(
							"<br><form><input type=button onClick=\"location.href='/OnlineVotingTestNew/managePoll.jsp'\" value='Back'>");

					out.println("</form></body></html>");

				} catch (PollNotFoundException e) {
					errorMessage = "Invalid Poll ID";
					request.setAttribute("ERROR", errorMessage);
					System.out.println("error: " + e);
					redirect = "/managePoll.jsp";
					dispatcher(request, response, redirect);
				}

			} else if (action.equals("SHOW_CREATED_POLLS")) {

				List<String> pollList = new ArrayList<String>();
				try {
					// retrieve from database the list of polls that user has created
					pollList = pollDB.getCreatedPolls(email);

					// display HTML page
					out.println("<html><head><title>Polls you have created</title></head>");

					if (pollList.size() != 0) {
						out.println("<body>Below is the list of polls you have created.<br><br>");
						out.println("<table border=\"1\" style=\"width:50%\">");
						out.println("<tr><th>Poll ID</th><th>Deadline</th></tr><tr>");
						// list is not empty, iterate through the retrieved list
						// and display it as a table
						for (int i = 0; i < pollList.size(); i++) {
							// retrieve from database the deadlines of polls
							// from the list and store them in the PollDetails object accordingly
							pd = pollDB.getPollDetails(Integer.valueOf(pollList.get(i)));
							java.sql.Date deadline = pd.getDeadline();
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
							deadlineInString = formatter.format(deadline);
							out.println("<td align=\"center\">" + pollList.get(i) + "</td><td align=\"center\">"
									+ deadlineInString + "</td></tr>");
							// for debug purposes
							System.out.println("created poll ID: " + pollList.get(i));
						}
						// prompt user to enter a poll ID and pass this
						// parameter to ShowPollResults servlet
						out.println(
								"</table><br><br><form method=\"get\" action=\"/OnlineVotingTestNew/ShowPollResults\"");
						out.println("<p>Please enter one of the above poll IDs to see the poll results:<br></p>");
						out.println("<input type=\"text\" name=\"createdPollID\" /><br><br>");
						out.println("<input type=\"submit\" value=\"Submit\">");
					} else if (pollList.size() == 0) {// list is empty, no poll
														// to display
						out.println("You have not created any polls.");
						out.println(
								"<br><br><form><input type=button onClick=\"location.href='/OnlineVotingTestNew/managePoll.jsp'\" value='Back'>");
					}
					out.println("</form></body></html>");

					pollDB.terminate();
				} catch (PollNotFoundException e) {
					errorMessage = "Invalid Poll ID";
					request.setAttribute("ERROR", errorMessage);
					System.out.println("error: " + e);
					redirect = "/managePoll.jsp";
					dispatcher(request, response, redirect);

				}

			} else if (action.equals("SHOW_POLL_OUTCOME")) {
				// this code is to display the result of an overdue poll after
				// owner and voters of that poll
				// have received the link to see the poll results via email

				String title = null;
				String option1 = null;
				String option2 = null;
				String option3 = null;
				String option4 = null;
				int countOption1 = 0;
				int countOption2 = 0;
				int countOption3 = 0;
				int countOption4 = 0;
				int total = 0;
				double ratio1 = 0;
				double ratio2 = 0;
				double ratio3 = 0;
				double ratio4 = 0;
				double totalRatio = 0.0;

				// retrieve the ID of the poll that user wants to check for
				// results from HTTP request
				selectedPollID = Integer.valueOf(request.getParameter("selectedPollID"));
				// get the current user's email
				email = logged.getEmail();
				List<String> voterList = new ArrayList<String>();
				List<String> inactivePolls = new ArrayList<String>();
				List<String> inactivePollOwner = new ArrayList<String>();
				boolean matchedPollID = false;
				boolean matchedVoterEmail = false;
				boolean matchedOwnerEmail = false;
				try {
					// get lists of inactive polls and corresponding poll owners
					// from database
					inactivePolls = pollDB.getInactivePolls(1);
					inactivePollOwner = pollDB.getInactivePolls(2);
					// iterate through the list of inactive polls until the
					// value matches the poll ID that user entered
					for (int i = 0; i < inactivePolls.size(); i++) {
						if (Integer.valueOf(inactivePolls.get(i)) == selectedPollID) {
							matchedPollID = true;
							// when matched, get the list of participants of
							// that poll
							voterList = pollDB.getPollParticipants(selectedPollID);
							break;
						}
					} // iterate through the list of participants until the
						// value matches the user's email
					for (int i = 0; i < voterList.size(); i++) {
						if (voterList.get(i).equals(email)) {
							matchedVoterEmail = true;

							break;
						}
					} // iterate through the list of inactive poll owners until
						// the value matches the user's email
					for (int i = 0; i < inactivePollOwner.size(); i++) {
						if (inactivePollOwner.get(i).equals(email)) {
							matchedOwnerEmail = true;

							break;
						}
					}

					request.setAttribute("selectedPollID", selectedPollID);
					if ((matchedVoterEmail && matchedPollID) || (matchedOwnerEmail && matchedPollID)) {
						try {
							// current user is indeed the owner or the voter of
							// the poll, retrieve the poll details and results from database
							pd = pollDB.getPollDetails(selectedPollID);
							title = pd.getTitle();
							option1 = pd.getOption1();
							option2 = pd.getOption2();
							option3 = pd.getOption3();
							option4 = pd.getOption4();
							
							// get the absolute number of voters who voted for each option 1 to 4 and total number of votes counted
							countOption1 = pollDB.getPollResults(selectedPollID, option1);
							countOption2 = pollDB.getPollResults(selectedPollID, option2);
							countOption3 = pollDB.getPollResults(selectedPollID, option3);
							countOption4 = pollDB.getPollResults(selectedPollID, option4);
							total = countOption1 + countOption2 + countOption3 + countOption4;
							totalRatio = 0.0;
							
							// calculate outcome of the poll in percentage
							try {
								ratio1 = (countOption1 * 100) / total;
								ratio2 = (countOption2 * 100) / total;
								ratio3 = (countOption3 * 100) / total;
								ratio4 = (countOption4 * 100) / total;
								totalRatio = 100.0;
							} catch (Exception e) {
								errorMessage = "There is no vote counted yet.";

							}

						} catch (PollNotFoundException e) {
							System.out.println("error: " + e);

						}
						// store the poll results into HTTP request and redirect to seePollResults.jsp
						request.setAttribute("ERROR", errorMessage);
						request.setAttribute("title", title);
						request.setAttribute("option1", option1);
						request.setAttribute("option2", option2);
						request.setAttribute("option3", option3);
						request.setAttribute("option4", option4);
						request.setAttribute("countOption1", countOption1);
						request.setAttribute("countOption2", countOption2);
						request.setAttribute("countOption3", countOption3);
						request.setAttribute("countOption4", countOption4);
						request.setAttribute("ratio1", ratio1);
						request.setAttribute("ratio2", ratio2);
						request.setAttribute("ratio3", ratio3);
						request.setAttribute("ratio4", ratio4);
						request.setAttribute("totalRatio", totalRatio);
						System.out.println("ratio: " + ratio1 + "," + ratio2 + "," + ratio3 + "," + ratio4);
						redirect = "/seePollResults.jsp";
						dispatcher(request, response, redirect);

						// for debug purposes
						System.out.println("matched email and pollID");
					} else if (!matchedPollID) {
						// entered poll ID is still active, redirect to main.jsp
						errorMessage = "The result of poll " + selectedPollID
								+ " is not available before the poll's deadline. You will be notified via email when it reaches the deadline.";
						request.setAttribute("ERROR", errorMessage);
						redirect = "/main.jsp";
						dispatcher(request, response, redirect);

						// for debug purposes
						System.out.println("unmatched PollID");
					} else if (!matchedVoterEmail || !matchedOwnerEmail) {

						errorMessage = "You do not have the authorization to view the results of Poll " + selectedPollID
								+ ". Please enter the ID of a poll that you participated in. <br>";

						request.setAttribute("ERROR", errorMessage);
						redirect = "/main.jsp";
						dispatcher(request, response, redirect);

						// for debug purposes
						System.out.println("unmatched Email");
					}

				} catch (PollNotFoundException e) {
					errorMessage = "Invalid Poll ID";
					request.setAttribute("ERROR", errorMessage);
					System.out.println("error: " + e);
					redirect = "/managePoll.jsp";
					dispatcher(request, response, redirect);

				}

			}

		} else if (action == null) {
			redirect = "/MainController";
			dispatcher(request, response, redirect);

		} else {
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
