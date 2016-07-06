<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%	String logged = null;
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
<title>Main page</title>
</head>
<body>
<h2>Welcome to online voting portal</h2>
<p>Here you can cast your vote and edit your vote within the poll's deadline.</p>
<p>You can also create a new poll and manage any polls you have created.</p>
<p>Your log in session will expire after 15 minutes.</p>
<p>Please choose your desired activity from the list.</p>
I would like to:<br><br>
		<form method="get" action="/OnlineVotingTestNew/MainController">
		 <select name="action">
  <option value="VOTE">Cast a vote</option>
  <option value="EDITVOTE">Edit my vote</option>
  <option value="CREATEPOLL">Create a poll</option>
  <option value="MANAGEPOLLS">Manage my polls</option>
   <option value="LOGOUT">Log out</option>
</select>
 <br><br>
  <input type="submit" value="Submit">
  <input type=button onClick="location.href='/OnlineVotingTestNew/index.jsp'" value='Back'>

</form>
</body>
</html>