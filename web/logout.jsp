<%
    // Set the user to logged out and then send them to the login page
    session.setAttribute("auth", false);
    session.setAttribute("user", null);
    response.sendRedirect("/login.jsp");
%>