<%@ page import="com.stms.web.*" %>
<%@ page import="java.sql.SQLException" %>
<%! boolean authRequired = false; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%
    // get registration fields from POST parameters
    String user_fname = request.getParameter("user_fname");
    String user_lname = request.getParameter("user_lname");
    String user_email = request.getParameter("user_email");
    String user_password_1 = request.getParameter("user_password_1");
    String user_password_2 = request.getParameter("user_password_2");

    // only continue to create account if all fields have been provided in valid format, otherwise return error code
    if(user_fname == null || user_email == null || user_password_1 == null || user_password_2 == null || !Utilities.validateEmail(user_email)
            || !user_password_1.equals(user_password_2) || user_password_1.length() < 8) {
        out.print(2); // registration failed - invalid account details
        return;
    }

    // Handle register attempt
    User user = null;
    try{
        // check if account already exists
        user = new User(user_email);
        // no exception thrown! so account already exists
        out.print(1); // registration failed - account with same email already exists
        return;
    }catch(SQLException e){
        out.print(3); // registration failed - server error (failed to connect to database)
        return;
    }catch(NullPointerException e){ // account does not exist, so we can continue
        user = new User();
        user.setFirstName(user_fname);
        user.setLastName(user_lname);
        user.setEmail(user_email);
        user.setPassword(user_password_1);
        if(user.save()){
            out.print(0); // registration successful
            return;
        }else{
            out.print(2); // registration failed - invalid account details
            return;
        }
    }
%>