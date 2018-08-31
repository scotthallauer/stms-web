<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.Types" %>
<%@ page import="java.sql.Timestamp" %>
<%! boolean authRequired = true; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%!

    public String getColourCode(String name){
        if(name.equals("red")) {
            return "#C3272B";
        }else if(name.equals("orange")){
            return "#F9690E";
        }else if(name.equals("yellow")){
            return "#FFA400";
        }else if(name.equals("green")){
            return "#26A65B";
        }else if(name.equals("blue")){
            return "#22A7F0";
        }else if(name.equals("purple")){
            return "#875F9A";
        }else{
            return "#95A5A6"; // grey
        }
    }

%>
<%

    String method = request.getMethod();

    // LOAD SEMESTERS //
    if(method.equals("GET")){

        User user = (User)session.getAttribute("user");
        Semester[] semesters = user.getSemesters();

    }

    // MODIFY SEMESTERS //
    else{

    }

%>