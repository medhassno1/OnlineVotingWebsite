package votingapp.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import votingapp.poll.*;
import votingapp.account.AuthenticationDetails;
import votingapp.exceptions.*;

// this class executes SQL statements for the "poll_index" and "vote_results" tables in database
public class PollDB extends DBConnection {
	public PollDB() throws Exception {
		super();
		
	}

	// boolean method which returns true if the poll with chosen poll ID is inactive and returns false otherwise
	public boolean pollDeactivated(int pollID) throws PollNotFoundException, InactivePollException {

		try {
			java.util.Date udate = new java.util.Date();
			java.sql.Date today = new java.sql.Date(udate.getTime());
			String selectStatement = "SELECT pollID from poll_index WHERE pollID = ? AND deadline >= ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, pollID);
			prepStmt.setDate(2, today);
			ResultSet rs = prepStmt.executeQuery();
			int returnedPollID = 0;
			while (rs.next()) {
				returnedPollID = rs.getInt(1);
			}
			if (returnedPollID == pollID) {

				prepStmt.close();
				return false;
			} else if (returnedPollID != pollID) {
				prepStmt.close();
				return true;
			}

		} catch (SQLException e) {
			throw new PollNotFoundException("Poll not found: " + pollID + " " + e.getMessage());
		}
		return true;
	}

	// this method creates a new poll with given poll details and saves them into database
	public void createPoll(PollDetails pd) throws ExistedPollException {
		try {
			String insertStatement = "INSERT INTO poll_index (owner, title, question, option1, option2, option3, option4, deadline) VALUES( "
					+ " ?, ?, ?, ?, ?, ?, ?, ?)";

			getConnection();

			PreparedStatement prepStmt = con.prepareStatement(insertStatement);
			prepStmt.setString(1, pd.getOwner());
			prepStmt.setString(2, pd.getTitle());
			prepStmt.setString(3, pd.getQuestion());
			prepStmt.setString(4, pd.getOption1());
			prepStmt.setString(5, pd.getOption2());
			prepStmt.setString(6, pd.getOption3());
			prepStmt.setString(7, pd.getOption4());
			prepStmt.setDate(8, pd.getDeadline());
			prepStmt.addBatch();
			prepStmt.executeBatch();

			prepStmt.close();

		} catch (SQLException e) {
			throw new ExistedPollException("Existed Poll Title: " + pd.getTitle() + " " + e.getMessage());

		}
	}

	// this method returns the poll ID of the poll that user has just created
	public int lastInsertedPollID(String email) throws UserNotFoundException {
		int pollID = 0;
		try {

			String selectStatement = "SELECT max(pollID) FROM poll_index WHERE owner = ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, email);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				pollID = rs.getInt(1);
			}

			prepStmt.close();
			return pollID;

		} catch (SQLException e) {
			throw new UserNotFoundException("Invalid email: " + email + " " + e.getMessage());
		}
	}

	// boolean method which returns true if user has already voted for the chosen poll and returns false otherwise
	public boolean existedVote(String email, int pollID) throws PollNotFoundException {
		try {
			String selectStatement = "SELECT * FROM vote_results WHERE email = ? AND pollID = ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, email);
			prepStmt.setInt(2, pollID);
			ResultSet rs = prepStmt.executeQuery();
			int returnedPollID = 0;

			while (rs.next()) {
				returnedPollID = rs.getInt(1);
			}
			if (returnedPollID == pollID) {

				prepStmt.close();
				return true;
			} else if (returnedPollID != pollID) {
				prepStmt.close();
				return false;
			}
		} catch (SQLException e) {
			throw new PollNotFoundException("Invalid pollID: " + pollID + " " + e.getMessage());
		}

		return false;
	}

	// this method inserts the user's vote into the "vote_results" table
	public void updatePoll(PollResults pr) throws PollNotFoundException {
		try {

			String insertStatement = "INSERT INTO vote_results (pollID, email, choice) VALUES( " + "?, ?, ?)";

			getConnection();

			PreparedStatement prepStmt = con.prepareStatement(insertStatement);
			prepStmt.setInt(1, pr.getPollID());
			prepStmt.setString(2, pr.getEmail());
			prepStmt.setString(3, pr.getChoice());

			prepStmt.addBatch();
			prepStmt.executeBatch();

			prepStmt.close();

		} catch (SQLException e) {
			throw new PollNotFoundException("Poll is deactivated " + e.getMessage());

		}
	}

	// this method updates the user's existing vote in the "vote_results" table
	public void editVote(PollResults pr) throws PollNotFoundException {
		try {

			String updateStatement = "Update vote_results set choice = ?  where email = ? and pollID = ?";

			getConnection();

			PreparedStatement prepStmt = con.prepareStatement(updateStatement);
			prepStmt.setString(1, pr.getChoice());
			prepStmt.setInt(3, pr.getPollID());
			prepStmt.setString(2, pr.getEmail());

			prepStmt.addBatch();
			prepStmt.executeBatch();

			prepStmt.close();

		} catch (SQLException ex) {
			throw new PollNotFoundException("Poll is deactivated " + ex.getMessage());

		}
	}

	// this method returns a HashMap that stores the Poll IDs and the corresponding choices that the user has voted so far
	public HashMap<Integer, String> getJoinedPolls(String email) throws PollNotFoundException {
		try {
			String selectStatement = "SELECT pollID, choice FROM vote_results WHERE email = ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, email);
			HashMap<Integer, String> table = new HashMap<Integer, String>();
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				table.put(rs.getInt(1), rs.getString(2));
			}
			prepStmt.close();
			return table;
		} catch (SQLException e) {
			throw new PollNotFoundException("Couldn't find User: " + email + " " + e.getMessage());
		}

	}

	// this method returns a list of variable length containing polls that the user has created so far
	public List<String> getCreatedPolls(String email) throws PollNotFoundException {
		String pollID = null;
		try {
			String selectStatement = "SELECT pollID from poll_index WHERE owner = ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, email);
			ResultSet rs = prepStmt.executeQuery();
			List<String> results = new ArrayList<String>();
			while (rs.next()) {
				results.add(String.valueOf(rs.getInt(1)));

			}
			prepStmt.close();
			return results;

		} catch (SQLException e) {
			throw new PollNotFoundException("Poll Not Found: " + pollID + " " + e.getMessage());
		}

	}

	// this method returns a PollDetails object that stores all details of a chosen poll
	public PollDetails getPollDetails(int pollID) throws PollNotFoundException {
		try {
			String selectStatement = "SELECT * from poll_index WHERE pollID = ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, pollID);
			ResultSet rs = prepStmt.executeQuery();
			String owner = null;
			String title = null;
			String question = null;
			String option1 = null;
			String option2 = null;
			String option3 = null;
			String option4 = null;
			Date deadline = null;

			while (rs.next()) {
				owner = rs.getString(2);
				title = rs.getString(3);
				question = rs.getString(4);
				option1 = rs.getString(5);
				option2 = rs.getString(6);
				option3 = rs.getString(7);
				option4 = rs.getString(8);
				deadline = rs.getDate(9);

			}

			prepStmt.close();
			PollDetails pd = new PollDetails (owner, title, question, option1, option2, option3, option4, deadline);
			return pd;
		} catch (SQLException e) {
			throw new PollNotFoundException("Poll not found: " + pollID + " " + e.getMessage());
		}

	}

	// this method returns the number of users who voted for a chosen option of a chosen poll
	public int getPollResults(int pollID, String choice) throws PollNotFoundException {
		try {
			String selectStatement = "SELECT * from vote_results WHERE pollID = ? AND choice =?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, pollID);
			prepStmt.setString(2, choice);
			ResultSet rs = prepStmt.executeQuery();
			int count = 0;

			while (rs.next()) {
				++count;

			}
			prepStmt.close();
			return count;

		} catch (SQLException e) {
			throw new PollNotFoundException("Poll not found: " + pollID + " " + e.getMessage());
		}

	}

	// this method returns a list of variable length 
	// the list is a list of all inactive polls if integer "type" equals 1
	// and is a list of corresponding owners of these inactive polls if integer "type" equals 2
	public List<String> getInactivePolls(int type) {

		List<String> inactivePolls = new ArrayList<String>();
		List<String> inactivePollOwner = new ArrayList<String>();
		try {
			java.util.Date udate = new java.util.Date();
			java.sql.Date today = new java.sql.Date(udate.getTime());
			String selectStatement = "SELECT pollID, owner from poll_index WHERE deadline < ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setDate(1, today);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				inactivePolls.add(String.valueOf(rs.getInt(1)));
				inactivePollOwner.add(String.valueOf(rs.getString(2)));
			}
			prepStmt.close();

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		if (type == 1) {
			return inactivePolls;
		} else if (type == 2) {
			return inactivePollOwner;
		} else
			return null;

	}
	
	// method which returns a list of variable length containing all voters of a chosen poll
	public List<String> getPollParticipants(int inactivePollID) throws PollNotFoundException {
		List<String> participantList = new ArrayList<String>();
		try {
			String selectStatement = "SELECT email from vote_results WHERE pollID = ?";
			getConnection();
			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setInt(1, inactivePollID);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				participantList.add(String.valueOf(rs.getString(1)));
			}
			prepStmt.close();

		} catch (SQLException e) {
			System.out.println("error: " + e);
		}
		return participantList;
	}

}
