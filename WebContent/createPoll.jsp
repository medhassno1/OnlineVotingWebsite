<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>  
	<% 
	String logged = null;
	try {
    logged = String.valueOf(session.getAttribute("logged"));
	} catch (Exception e){
		
	}
	 if(logged.equals("null") || logged == null) {%>
	 	<jsp:forward page="/index.jsp"/>
		 <%}
     String errorMessage = String.valueOf(request.getAttribute("ERROR"));
     if (errorMessage.equals("null") == false) {%>
     ERROR: <%=errorMessage%>
     <%};
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create a new poll</title>

</head>
<body>
<h2>Please enter the details of your poll. </h2> 
<p>You can enter up to 4 options for this poll.</p>
<form action="/OnlineVotingTestNew/CreatePoll" method="POST">
		Poll title (eg. My Poll): <br> <br> <input
			type="text" name="title" size="50"/> <br> <br>
		Topic question (eg. What is your favorite band?):<br> <br> 
		<input type="text" name="question" size="100"> <br> <br> 
		Option 1: <br><br>
		<input type="text" name="option1" size="50"> <br> <br>
		Option 2: <br><br>
		<input type="text" name="option2" size="50"> <br> <br>
		Option 3: <br><br>
		<input type="text" name="option3" size="50"> <br> <br>
		Option 4: <br><br>
		<input type="text" name="option4" size="50"> <br> <br>
		Please set a deadline to your poll in the following format: yyyy-MM-dd (eg. 2016-01-01): <br><br>
		<input type="text" name="deadlineInString" size="25"> <br> <br>
		<input type="submit" value="Submit"/>
		<input type=button onClick="location.href='/OnlineVotingTestNew/main.jsp'" value='Back'>

</form>
</body>
</html>