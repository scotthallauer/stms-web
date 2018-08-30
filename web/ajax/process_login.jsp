<%@ page import="com.stms.web.*" %>
<%@ page import="java.sql.SQLException" %>
<%! boolean authRequired = false; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%
    // get login credentials from POST parameters
    String user_email = request.getParameter("user_email");
    String user_password = request.getParameter("user_password");

    // only continue to validate if both email and password have been provided, otherwise return error code
    if(user_email == null && user_password == null) {
        out.print(2); // login failed - incorrect credentials
        return;
    }

    // Handle login attempt
    User user = null;
    try{
        user = new User(user_email);
    }catch(NullPointerException e){
        out.print(2); // login failed - incorrect credentials
        return;
    }catch(SQLException e) {
        out.print(3); // login failed - server error (failed to connect to database)
        return;
    }
    if(!user.isActivated()) {
        out.print(1); // login failed - account not activated
        return;
    }else if(user.checkPassword(user_password)) {
        session.setAttribute("auth", true);
        session.setAttribute("user", user);
        out.print(0); // login successful
        return;
    }else{
        out.print(2); // login failed - incorrect credentials
        return;
    }
%>