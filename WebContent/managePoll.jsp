
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
<title>Manage Polls</title>
</head>
<body>
<p>Please choose your desired activity from the list.</p>
I would like to see:<br><br>
<form method="get" action="/OnlineVotingTestNew/ManagePoll">
<select name="action">
  		<option value="SHOW_JOINED_POLLS" />Polls I have joined</option>
    	<option value="SHOW_CREATED_POLLS" />Polls I have created</option>
    	
    	
</select>
<br><br>
<input type="submit" value="Submit">
<input type=button onClick="location.href='/OnlineVotingTestNew/main.jsp'" value='Back'>
</form>

</body>
</html>