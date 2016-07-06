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
<title>Log in or Register to Online Voting</title>
</head>
<body>
<h2>Log in or Register to Online Voting</h2>
<a href="/OnlineVotingTestNew/login.jsp">Log in</a><br><br>
<a href="/OnlineVotingTestNew/register.jsp">Register</a><br><br>
</body>
</html>