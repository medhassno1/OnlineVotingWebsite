package votingapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// this class establishes a connection with database
public class DBConnection {

	Connection con;
	private String DatabaseURL = "jdbc:derby:C:\\Users\\YTN\\VotingTest;create=true";
	private String DatabaseDRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private String DatabaseUser = "onlinevotingtest";
	private String DatabasePassword = "v2015";
	
	//constructor of class which instantiates connection and throw exception
	public DBConnection () throws Exception {
		try  {               
			// Load the driver
			Class.forName(DatabaseDRIVER).newInstance();
			
			// Get the connection
			con = DriverManager.getConnection(DatabaseURL,DatabaseUser,DatabasePassword);
			
		} catch (Exception ex) {
			throw new Exception("Couldn't open connection to database: " + ex.getMessage());
		}       
	}
	
	//close the connection
	public void terminate () {
		try {
			con.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	protected Connection getConnection() {

		return con;
	}
	
	protected void releaseConnection() {
		terminate();
	}
	

}
