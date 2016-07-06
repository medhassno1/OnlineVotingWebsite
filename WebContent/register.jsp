<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%	
     String errorMessage = String.valueOf(request.getAttribute("ERROR"));
 if (errorMessage.equals("null") == false) {%>
 ERROR: <%=errorMessage%>
 <%};
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
</head>
<body>
<h2>Please enter a valid email address:</h2>
<form method="post" action="/OnlineVotingTestNew/RegisterController" >

	
		Email:<br>
		<input id="email" type="text" name="email"  value="xyz@mail.com"/>
		
		<br><br>
		<input type="submit" value="Register" />
		<input type=button onClick="location.href='/OnlineVotingTestNew/index.jsp'" value='Back'/>
</body>
</html>