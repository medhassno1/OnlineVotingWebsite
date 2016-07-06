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
	
	String title = (String)request.getAttribute("title");
	String pollID = String.valueOf(request.getAttribute("selectedPollID"));
	String option1 = String.valueOf(request.getAttribute("option1"));
	String option2 = String.valueOf(request.getAttribute("option2"));
	String option3 = String.valueOf(request.getAttribute("option3"));
	String option4 = String.valueOf(request.getAttribute("option4"));
	
	int countOption1 = Integer.valueOf(String.valueOf(request.getAttribute("countOption1")));
	int countOption2 = Integer.valueOf(String.valueOf(request.getAttribute("countOption2")));
	int countOption3 = Integer.valueOf(String.valueOf(request.getAttribute("countOption3")));
	int countOption4 = Integer.valueOf(String.valueOf(request.getAttribute("countOption4")));
	int total = countOption1 + countOption2 + countOption3 + countOption4;
	
	String ratio1 = String.valueOf(request.getAttribute("ratio1"));
	String ratio2 = String.valueOf(request.getAttribute("ratio2"));
	String ratio3 = String.valueOf(request.getAttribute("ratio3"));
	String ratio4 = String.valueOf(request.getAttribute("ratio4"));
	String totalRatio = String.valueOf(request.getAttribute("totalRatio"));
	
	if (errorMessage.equals("null") == false) {%>
    ERROR: <%=errorMessage%>
    <%};
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Poll results</title>
</head>
<body>
<p>Below is the results of Poll <%=pollID%> titled "<%=title%>":<br></p>
<table border="1" style="width: 50%">
			<tr>
				<th>Option</th>
				<th>Number of votes counted</th>
				<th>Percentage (%)</th>

			</tr>
			<tr>
				<td><%=option1%></td>
				<td><%=countOption1%></td>
				<td><%=ratio1%></td>

			</tr>
			<tr>
				<td><%=option2%></td>
				<td><%=countOption2%></td>
				<td><%=ratio2%></td>

			</tr>
			<tr>
				<td><%=option3%></td>
				<td><%=countOption3%></td>
				<td><%=ratio3%></td>

			</tr>
			<tr>
				<td><%=option4%></td>
				<td><%=countOption4%></td>
				<td><%=ratio4%></td>

			</tr>
			<tr>
				<td>Total</td>
				<td><%=total%></td>
				<td><%=totalRatio%></td>

			</tr>
			</table>
<form>
<br><input type=button onClick="location.href='/OnlineVotingTestNew/managePoll.jsp'" value='Back'/>
</form>
</body>
</html>