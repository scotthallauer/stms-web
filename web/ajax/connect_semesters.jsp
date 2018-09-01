<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.Types" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%! boolean authRequired = false; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    /*
    String method = request.getMethod();

    // LOAD SEMESTERS //
    if(method.equals("GET")){

        User user = (User)session.getAttribute("user");
        Semester[] semesters = user.getSemesters();

    }

    // MODIFY SEMESTERS //
    else{

    }
    */

%>