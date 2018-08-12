<%
    // Set up the session's "authenticated" attribute if it is null
    if(session.getAttribute("authenticated") == null){
        session.setAttribute("authenticated", new Boolean(false));
    }

    // If the user is on a restricted page and they are not logged in, then redirect them to the login page
    if(((Boolean)session.getAttribute("authenticated")).booleanValue() == false && authRequired == true){
        response.sendRedirect("/login.jsp");
    }
%>