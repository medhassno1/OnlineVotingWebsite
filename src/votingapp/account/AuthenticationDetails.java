package votingapp.account;

//this class can be used to create an object that stores all details of a user
public class AuthenticationDetails {

	 private String email = null;
	 private String idkey = null;

	 
	 public AuthenticationDetails(String email, String idkey) {
	        this.email = email;
	        this.idkey = idkey;
	 }
	 
	 public String getEmail() {
	        return email;
	    }
	 
	 public String getIDkey() {
	        return idkey;
	    }

}
