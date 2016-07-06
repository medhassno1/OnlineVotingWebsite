package votingapp.database;

import java.sql.Timestamp;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import votingapp.account.*;
import votingapp.exceptions.*;
import votingapp.database.*;

//this class executes SQL statements for the "personal_data" table in database
public class VotingDB extends DBConnection {
	
	public VotingDB() throws Exception {
		super();
		
	}

	// boolean method which returns true if user's email already exists in database and returns false otherwise
	public boolean voterExist(String email) throws ExistedUserException {

		try {
			
			String selectStatement = "SELECT * FROM personal_data WHERE email = ?";
			getConnection();

			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, email);

			ResultSet rs = prepStmt.executeQuery();

			if (rs.next()) {
				prepStmt.close();
				return true;
			} else {
				prepStmt.close();
				return false;
			}
		} catch (SQLException ex) {
			throw new ExistedUserException("Email already registered: " + email + " " + ex.getMessage());
		}
	}

	// boolean method which returns true if the log in details entered by user are correct and returns false otherwise
	public boolean checkID(String email, String idkey) throws UserNotFoundException, IncorrectIDException {

		try {
			
			String selectStatement = "SELECT * FROM personal_data WHERE email = ? AND idkey = ?";
			getConnection();

			PreparedStatement prepStmt = con.prepareStatement(selectStatement);
			prepStmt = con.prepareStatement(selectStatement);
			prepStmt.setString(1, email);
			prepStmt.setString(2, idkey);

			ResultSet rs = prepStmt.executeQuery();

			if (rs.next()) {
				
				prepStmt.close();
				return true;
			
			} else {
				prepStmt.close();
				return false;
			}
		} catch (SQLException ex) {
			throw new IncorrectIDException("Incorrect Email or ID Key: " + ex.getMessage());
		}
	}
	
	// method to save the user's email and given ID key into "personal_data" table in database
	public void saveEmailtoDB(String email, String idkey) throws ExistedUserException {
		try {
			String insertStatement = "INSERT INTO personal_data (email, idkey) VALUES( " + "?, ?)";
			
			getConnection();
			
			// Start the prepared statement
			PreparedStatement prepStmt = con.prepareStatement(insertStatement);
			prepStmt.setString(1, email);
			prepStmt.setString(2, idkey);
			prepStmt.addBatch();
			int[] updateCounts = prepStmt.executeBatch();
		
			prepStmt.close(); 

		} catch (SQLException ex) {
			throw new ExistedUserException("Existed Email: " + email + " " + ex.getMessage());
			
		} 
	}
	
	
}
