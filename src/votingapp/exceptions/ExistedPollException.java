package votingapp.exceptions;

public class ExistedPollException extends Exception {
	 public ExistedPollException () { }

	    public ExistedPollException (String msg) {
	        super(msg);
	    } 
}
