<%!
    public boolean bool(Object obj){
        return ((Boolean)obj).booleanValue();
    }
%>
<%
    // Set up the session's "auth" attribute to initial value
    if(session.getAttribute("auth") == null){
        session.setAttribute("auth", false);
    }

    // If the user is on a restricted page and they are not logged in, then redirect them to the login page
    if(authRequired && !bool(session.getAttribute("auth"))){
        response.sendRedirect("/login.jsp");
    }
%>