<%@ page import="com.stms.web.Database" %>
<%! boolean authRequired = false; %>
<%@ include file="../includes/session.jsp" %>
<%@ page import="com.stms.web.*" %>
<%
    // Handle login attempt
    String user_email = request.getParameter("user_email");
    String user_password = request.getParameter("user_password");
    if(user_email != null && user_password != null){
        // login successful
        if(user_email.equals("test@gmail.com") && user_password.equals("1234")) {
            session.setAttribute("authenticated", true);
            out.print("0");
        // login failed - account not activated
        }else if(user_email.equals("test@gmail.com") && user_password.equals("123")){
            out.print("1");
        // login failed - incorrect credentials
        }else{
            out.print("2");
        }
    }else{
        out.print("2");
    }
%>