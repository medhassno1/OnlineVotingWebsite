package votingapp.exceptions;

public class PollNotFoundException extends Exception {
	public PollNotFoundException() { }

    public PollNotFoundException (String msg) {
        super(msg);
    } 
}
