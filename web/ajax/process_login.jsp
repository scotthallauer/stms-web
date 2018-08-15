<%@ page import="com.stms.web.*" %>
<%! boolean authRequired = false; %>
<%@ include file="../includes/session.jsp" %>
<%
    // get login credentials from POST parameters
    String user_email = request.getParameter("user_email");
    String user_password = request.getParameter("user_password");

    // only try to validate if both email and password have been provided, otherwise return error code
    if(user_email != null && user_password != null) {
        Database db = new Database();
        if (db.isConnected()) {
            User user = null;
            try{
                user = new User(user_email);
            }catch(Exception e){
                // login failed - incorrect credentials
                out.print(2);
                return;
            }
            if(user.checkPassword(user_password)) {
                session.setAttribute("auth", true);
                session.setAttribute("user", user);
                // login successful
                out.print(0);
                return;
            }else if(user_email.equals("test@gmail.com") && user_password.equals("123")){
                // login failed - account not activated
                out.print(1);
                return;
            }else{
                // login failed - incorrect credentials
                out.print(2);
                return;
            }
        } else {
            // login failed - server error (failed to connect to database)
            out.print(3);
            return;
        }
    }else{
        // login failed - incorrect credentials
        out.print(2);
        return;
    }
%>