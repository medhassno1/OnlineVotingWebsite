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
<title>Log In</title>
</head>
<body>
<h2>Please enter your authentication credentials</h2>
<form id="login" method="post" action="/OnlineVotingTestNew/LogInController" >

	
		Email:<br>
		<input id="username" type="text" name="email"  value="abc@xyz.com"/>
	
		<br><br>ID Key:<br>
		<input id="idkey" type="password" name="idkey"  value="1111" />
		
		<br><br>
		
		<input type="submit" value="Log In" />
		<input type=button onClick="location.href='/OnlineVotingTestNew/index.jsp'" value='Back'/>


</form>
</body>
</html>