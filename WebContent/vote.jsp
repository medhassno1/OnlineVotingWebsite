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
<title>Cast your vote</title>
</head>
<body>
<p>Please enter a valid Poll ID to join the poll:<br></p>
<form method="get" action="/OnlineVotingTestNew/UpdateVote">
<input type="text" name="poll_ID" />
<br><br>By default, entering 1 into the above field will take you to the democratic voting poll.
<br> <br> <input type="submit" value="Submit" >
<input type=button onClick="location.href='/OnlineVotingTestNew/main.jsp'" value='Back'/>
</form>
</body>
</html>