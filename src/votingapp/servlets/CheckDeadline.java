package votingapp.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import votingapp.database.PollDB;
import votingapp.database.VotingDB;
import votingapp.exceptions.PollNotFoundException;

// this servlet loads every time the server is started

//this servlet checks for overdue polls and sends email to notify owner and participants of the poll
public class CheckDeadline extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PollDB pollDB;

	public CheckDeadline() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		// create new Poll database object
		try {
			pollDB = new PollDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// get lists of inactive polls and corresponding owners from Poll Database
		List<String> inactivePolls = pollDB.getInactivePolls(1);
		List<String> inactivePollOwner = pollDB.getInactivePolls(2);
		
		// for debug purposes
		System.out.println("inactive polls: " + inactivePolls);
		System.out.println("inactive poll owners: " + inactivePollOwner);

		// iterate through the list of inactive polls
		for (int i = 0; i < inactivePolls.size(); i++) {
			
			List<String> participantList;
			try {
				// get list of all participants of each poll
				participantList = pollDB.getPollParticipants(Integer.valueOf(inactivePolls.get(i)));
				
				// iterate through the list of participants
				for (int j = 0; j < participantList.size(); j++) {
					// for debug purposes
					System.out.println("poll ID: " + inactivePolls.get(i) + " voter email: " + participantList.get(j));
					
					// send email to all participants of each poll 
					try {
						sendEmail(Integer.valueOf(inactivePolls.get(i)), participantList.get(j), 2);

					} catch (NumberFormatException | MessagingException e) {
						
						e.printStackTrace();
					}
				}
			} catch (NumberFormatException | PollNotFoundException e1) {
				e1.printStackTrace();
			}

		} 
		// iterate through the list of inactive polls
		for (int i = 0; i < inactivePolls.size(); i++) {
			// for debug purposes
			System.out.println("poll ID: " + inactivePolls.get(i) + " owner email: " + inactivePollOwner.get(i));
			
			// send email to owner of each poll
			try {
				sendEmail(Integer.valueOf(inactivePolls.get(i)), inactivePollOwner.get(i), 1);

			} catch (NumberFormatException | MessagingException e) {

				e.printStackTrace();
			}

		}

	}

	// this method takes 3 parameters: integer pollID of the inactive poll, String email of the email recipient, 
	// and integer type: type 1 for owner of a poll, type 2 for participants of a poll
	public void sendEmail(int pollID, String email, int type) throws AddressException, MessagingException {
		/*final String sender = "testmail@uniassistant.com";
		String receiverEmailAddress = email;
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", "uniassistant.com");
		props.put("mail.smtp.port", "25");
		Session sessionEmail = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, "sPe6fk7LsCFe9S");
			}
		});
		Message msg = new MimeMessage(sessionEmail);
		msg.setFrom(new InternetAddress(sender));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmailAddress, false));
		// if the recipient is owner of the poll, the message of the email would be
		if (type == 1) {
			msg.setSubject("Turnout of a poll you have created");
			msg.setText("The poll with poll ID " + pollID + " that you created has passed its deadline."
					+ "\r\n Please log in and then enter this poll ID into the following link to see the poll's results."
					+ "\r\n http://localhost:8080/OnlineVotingTestNew/pollOutcome.jsp");
		} // if the recipient is a participant of the poll, the message of the email would be
		else if (type == 2) {
			msg.setSubject("Turnout of a poll you have participated in.");
			msg.setText("The poll with poll ID " + pollID + " that you participated in has passed its deadline."
					+ "\r\n Please log in and then enter this poll ID into the following link to see the poll's results."
					+ "\r\n http://localhost:8080/OnlineVotingTestNew/pollOutcome.jsp");
		}
		Transport.send(msg);
		// for debug purposes
		System.out.println("e-mail sent");

	*/
	}
}
