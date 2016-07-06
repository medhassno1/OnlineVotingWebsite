package votingapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.xml.internal.ws.util.StringUtils;

import votingapp.account.AuthenticationDetails;
import votingapp.database.PollDB;
import votingapp.exceptions.PollNotFoundException;
import votingapp.poll.PollDetails;

// this servlet retrieves parameter "createdPollID" from ManagePoll servlet and displays the results to seePollDetails.jsp
public class ShowPollResults extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PollDB pollDB;
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
	double totalRatio = 100.0;

	AuthenticationDetails logged = null;
	String redirect = null;
	List<String> pollList = null;
	String errorMessage = null;

	public ShowPollResults() {
		super();

	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// get the current session
		HttpSession session = request.getSession(false);

		// create new Poll DB object
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
			dispatcher(request, response, redirect);
		}

		if (logged != null) {
			// user is already logged in, get current user's email
			String email = logged.getEmail();

			// retrieve the list of poll IDs that the user created
			try {
				pollList = pollDB.getCreatedPolls(email);
				// for debug purposes
				System.out.println("poll list: " + pollList);
			} catch (PollNotFoundException e) {
				e.printStackTrace();
			}

			// store the poll ID entered by the user into integer
			// "selectedPollID"
			String pollID = request.getParameter("createdPollID");
			int selectedPollID = Integer.parseInt(pollID);
			request.setAttribute("selectedPollID", selectedPollID);

			boolean matchedID = false;
			// check if the poll ID that user entered in was actually created by the user
			for (int i = 0; i < pollList.size(); i++) {
				if (pollID.equals(pollList.get(i))) {
					matchedID = true;

					// for debug purposes
					System.out.println("matched");
					break;

				}
			}

			// call the method to calculate outcome of the poll with selected poll ID
			getVoteTurnout(selectedPollID);

			// if the poll ID that user entered in was actually created by the user,
			// redirect to the results page
			if (matchedID == true) {
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

			} // otherwise, redirect user to MainController
			else if (matchedID == false) {
				System.out.println("unmatched");
				String errorMessage = "The poll ID you have entered is not owned by you. Please enter a valid poll ID that you created.";
				request.setAttribute("ERROR", errorMessage);
				redirect = "/MainController";

			}

			dispatcher(request, response, redirect);
		} else {
			// user is not logged in, redirect to MainController servlet
			String errorMessage = "You are not logged in.";
			request.setAttribute("ERROR", errorMessage);
			redirect = "/MainController";
			dispatcher(request, response, redirect);
		}

	}

	// this method calculates the outcome of the poll with given poll ID
	public void getVoteTurnout(int requiredPollID) {

		try {
			 // get the details of the poll to show results
			PollDetails pd = pollDB.getPollDetails(requiredPollID);
			title = pd.getTitle();
			option1 = pd.getOption1();
			option2 = pd.getOption2();
			option3 = pd.getOption3();
			option4 = pd.getOption4();
			
			// get the absolute number of voters who voted for each option 1 to 4 and the total number of votes counted
			countOption1 = pollDB.getPollResults(requiredPollID, option1);
			countOption2 = pollDB.getPollResults(requiredPollID, option2);
			countOption3 = pollDB.getPollResults(requiredPollID, option3);
			countOption4 = pollDB.getPollResults(requiredPollID, option4);
			total = countOption1 + countOption2 + countOption3 + countOption4;

			// calculate outcome of the poll in percentage
			try {
				ratio1 = (countOption1 * 100) / total;
				ratio2 = (countOption2 * 100) / total;
				ratio3 = (countOption3 * 100) / total;
				ratio4 = (countOption4 * 100) / total;
			} catch (Exception e) {
				errorMessage = "There is no vote counted yet.";
				ratio1 = 0.0;
				ratio2 = 0.0;
				ratio3 = 0.0;
				ratio4 = 0.0;
				totalRatio = 0.0;

			}

		} catch (PollNotFoundException e) {
			System.out.println("error: " + e);

		}
	}

	// this method creates a dispatcher object that redirects to the link stored in string "redirect"
	private void dispatcher(HttpServletRequest request, HttpServletResponse response, String uri)
			throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher(uri);
		rd.forward(request, response);
	}

}
