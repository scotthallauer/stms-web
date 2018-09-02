<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.Types" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%! boolean authRequired = true; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    String method = request.getMethod();

    // LOAD SEMESTERS/COURSES //
    if(method.equals("GET")){

        User user = (User)session.getAttribute("user");
        Semester[] semesters = user.getSemesters();

        JSONArray jsa = new JSONArray(); // JSON semester array
        for(int i = 0 ; i < semesters.length ; i++){
            JSONObject jso = new JSONObject(); // JSON semester object
            jso.put("id", semesters[i].getSemesterID());
            jso.put("name", semesters[i].getName());
            jso.put("start", semesters[i].getStartDate());
            jso.put("end", semesters[i].getEndDate());
            Course[] courses = semesters[i].getCourses();
            JSONArray jca = new JSONArray(); // JSON course array
            for(int j = 0 ; j < courses.length ; j++){
                JSONObject jco = new JSONObject(); // JSON course object
                jco.put("id", courses[j].getCourseID());
                jco.put("semester_id_1", courses[j].getSemesterID1());
                jco.put("name", courses[j].getName());
                jco.put("code", courses[j].getCode());
                jco.put("colour", courses[j].getColour());
                jca.put(jco);
            }
            jso.put("courses", jca);
            jsa.put(jso);
        }
        out.print(jsa);

    }

    // MODIFY SEMESTERS/COURSES //
    else{



    }

%>