package votingapp.poll;

//this class can be used to create an object that stores the result of a poll
public class PollResults {
	 private int pollID = 0;
	 private String email = null;
	 private String choice = null;

	 
	 public PollResults(int pollID, String email, String choice) {
	        this.pollID = pollID;
		 	this.email = email;
	        this.choice = choice;
	 }
	 
	 public int getPollID() {
	        return pollID;
	    }
	 
	 public String getEmail() {
	        return email;
	    }
	 
	 public String getChoice() {
	        return choice;
	    }

}
