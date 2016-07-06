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
	String pollID = String.valueOf(request.getAttribute("pollID")); 
	if (errorMessage.equals("null") == false) {%>
    ERROR: <%=errorMessage%>
    <%};
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Poll successfully created.</title>
</head>
<body>
<p>You have successfully created a new poll.</p>
The Poll ID of this poll is <%=pollID%>.<br><br>
Please provide this poll ID to your voters in order for them to cast their votes.<br><br>
<form>
<input type=button onClick="location.href='/OnlineVotingTestNew/main.jsp'" value='Back'/>
</form>
</body>
</html>