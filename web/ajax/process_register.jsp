<%! boolean authRequired = false; %>
<%@ include file="../includes/session.jsp" %>
<%
    // Currently hard-coded for testing purposes, will be completed in future iteration

    // Handle register attempt
    String user_name = request.getParameter("user_name");
    String user_email = request.getParameter("user_email");
    String user_password_1 = request.getParameter("user_password_1");
    String user_password_2 = request.getParameter("user_password_2");
    if(user_name != null && user_email != null && user_password_1 != null && user_password_2 != null){
        // registration successful
        if(user_email.equals("test@gmail.com") && user_password_1.equals("12345678")) {
            out.print("0");
        // registration failed - account with same email already exists
        }else if(user_email.equals("test@gmail.com") && user_password_1.equals("123456789")){
            out.print("1");
        // registration failed - invalid account details
        }else{
            out.print("2");
        }
    }
%>