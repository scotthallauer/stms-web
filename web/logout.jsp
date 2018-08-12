<%
    // Set the user to logged out and then send them to the login page
    session.setAttribute("authenticated", new Boolean(false));
    response.sendRedirect("/login.jsp");
%>