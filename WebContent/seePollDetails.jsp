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
	int pollID = Integer.valueOf(String.valueOf(request.getAttribute("pollID")));
	String title = String.valueOf(request.getAttribute("title"));
	String question = String.valueOf(request.getAttribute("question"));
	String option1 = String.valueOf(request.getAttribute("option1"));
	String option2 = String.valueOf(request.getAttribute("option2"));
	String option3 = String.valueOf(request.getAttribute("option3"));
	String option4 = String.valueOf(request.getAttribute("option4"));
	String deadlineInString = (String)request.getAttribute("deadlineInString");
	if (errorMessage.equals("null") == false) {%>
    ERROR: <%=errorMessage%>
    <%};

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=title%></title>
</head>
<p> Welcome to Poll <%=title %>!
<br><br>Poll's ID: <%=pollID%>
<br><br>The topic question of this poll is: "<%=question%>"
<br><br> The deadline of this poll is <%=deadlineInString%>
<br><br>Please choose your desired vote. </p>
<form method="post" 
	action="/OnlineVotingTestNew/VoteOrEditVote">
		<table border="1" style="width: 50%">
			<tr>
				<td>Option:</td><br><br>
			</tr>
			<tr>
				<td><input type="radio" name="choice"
					value="<%=option1%>" checked><%=option1%></td>
			</tr>
			<tr>
				<td><input type="radio" name="choice"
					value="<%=option2%>"><%=option2%></td>
			</tr>
			<tr>
				<td><input type="radio" name="choice"
					value="<%=option3%>"><%=option3%></td>
			</tr>
			<tr>
				<td><input type="radio" name="choice"
					value="<%=option4%>"><%=option4%></td>
			</tr>
			<input type="hidden" name="action" value="UPDATEPOLL">
		</table>
		
		<br> <br> <input type="submit" name="confirm" value="Confirm">
					<input type="submit" name="confirm" value="Cancel">
	</form>

<body>

</body>
</html>