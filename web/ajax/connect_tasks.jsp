<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.time.LocalDate" %>
<%! boolean authRequired = true; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    String method = request.getMethod();
    User user = (User)session.getAttribute("user");

    // LOAD TASKS //
    if(method.equals("GET")){



    }

    // MODIFY TASKS //
    else{



    }

%>