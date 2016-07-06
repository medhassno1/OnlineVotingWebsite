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
		 <%} else {%>
	
	<%} 
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Poll Outcome</title>
</head>
<body>
<p>Please enter the ID of the poll you would like to check for results:<br></p>
<form method="get" action="/OnlineVotingTestNew/ManagePoll">
Poll ID:
<input type="text" size="10" name="selectedPollID" />
<br><br>
<input type="submit" value="Submit" />
<input type="hidden" name="action" value="SHOW_POLL_OUTCOME"/>
</form>
</body>
</html>