package votingapp.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import votingapp.account.AuthenticationDetails;
import votingapp.account.RandomStringGenerator;
import votingapp.database.VotingDB;
import votingapp.exceptions.ExistedUserException;
import votingapp.exceptions.IncorrectIDException;
import votingapp.exceptions.UserNotFoundException;

// this servlet retrieves parameters from register.jsp and sends the ID key to the newly registered user via email 
public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    String redirect = null;
    AuthenticationDetails logged = null;
    private VotingDB votingDB;
    
    public RegisterController() {
        super();
       
    }

	
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// to display HTML page
    	PrintWriter out = response.getWriter();
		response.setContentType("text/html");
    	
		// create new Voting database object
    	try {
			votingDB = new VotingDB();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    		String errorMessage = "";
    		String idkey = null;
    		// retrieve parameter from HTTP request
			String email = request.getParameter("email");
			
			try {
				// check if voter already exists in database 
				boolean existedVoter = votingDB.voterExist(email);
				
				// for debug purposes
				System.out.println("voter existed: " + existedVoter);
				

				if (existedVoter == false) {
					// voter not existent, create new account
					try {
						
						// generate a random ID key of length 10
						idkey = RandomStringGenerator.generateRandomString(10);
						
						// save email and ID key to database
						votingDB.saveEmailtoDB(email, idkey);
						
						// for debug purposes
						System.out.println(email + ", " + idkey);
						
						// send email to provided email address
						final String sender = "testmail@uniassistant.com";
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
						msg.setRecipients(Message.RecipientType.TO,
								InternetAddress.parse(receiverEmailAddress, false));
						
						// message with generated ID key
						msg.setSubject("Verification for Online Voting");
						msg.setText("Your authentification key is " + idkey
								+ ". \r\n Please click on the following link to log in."
								+ "\r\n http://localhost:8080/OnlineVotingTestNew/login.jsp");
						Transport.send(msg);
						
						// for debug purposes
						System.out.println("e-mail sent");

						// Display HTML page
						out.println("<html><head><title>Email address authentification</title></head><body>");
						out.println("The email address you have provided is " + receiverEmailAddress + ".<br><br>");
						out.println("Please check your email for authentication key.<br>");
						out.println(
								"<br><form><input type=button onClick=\"location.href='/OnlineVotingTestNew/index.jsp'\" value='Back'>");
						out.println(
								"<input type=button onClick=\"location.href='/OnlineVotingTestNew/login.jsp'\" value='Log in'></form>");
						out.println("</body></html>");

					} catch (Exception e) {
						// catch string generator exception
						e.printStackTrace();
						System.out.println("error: " + e);
					}

				} else if (existedVoter == true) {
					// voter already exists, redirect to Login page
					errorMessage = "Email is already registered. Please enter a different email or log in.";
					request.setAttribute("ERROR", errorMessage);
					redirect = "/login.jsp";
					dispatcher(request, response, redirect);
				}
			} catch (ExistedUserException e) {
				errorMessage = "Email is already registered. Please enter a different email or log in.";
				request.setAttribute("ERROR", errorMessage);
				e.printStackTrace();
				System.out.println("error: " + e);
				redirect = "/login.jsp";
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
