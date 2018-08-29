<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.DecimalFormat" %>
<%! boolean authRequired = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    User user = (User)session.getAttribute("user");
    Semester[] semesters = user.getSemesters();
    Timestamp now = new Timestamp(System.currentTimeMillis());

    JSONArray ja = new JSONArray();
    for(int i = 0 ; i < semesters.length ; i++) {
        Course[] courses = semesters[i].getCourses();
        for (int j = 0; j < courses.length; j++) {
            CourseSession[] sessions = courses[j].getGradedSessions(); // only interested in graded sessions
            for (int k = 0; k < sessions.length; k++) {
                if(now.before(sessions[k].getStartDate())) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", String.valueOf(sessions[k].getSessionID()));
                    if(sessions[k].getType().equals("test") || sessions[k].getType().equals("exam")){
                        jo.put("action", "Study for");
                    }else{
                        jo.put("action", "Prepare for");
                    }
                    jo.put("type", sessions[k].getType());
                    jo.put("courseName", courses[j].getName());
                    if(courses[j].getCode() == null){
                        jo.put("courseCode", "null");
                    }else {
                        jo.put("courseCode", courses[j].getCode());
                    }
                    jo.put("type", Utilities.capitalise(sessions[k].getType()));
                    LocalDate dueDate = sessions[k].getStartDate().toLocalDateTime().toLocalDate();
                    Integer userPriority = sessions[k].getPriority();
                    Double weighting = sessions[k].getWeighting();
                    jo.put("priority", String.valueOf(Priority.calculate(dueDate, userPriority, weighting)));
                    if(weighting == null){
                        jo.put("weighting", "null");
                    }else {
                        DecimalFormat decimal = new DecimalFormat("0.##");
                        jo.put("weighting", decimal.format(weighting));
                    }
                    jo.put("dueDate", sessions[k].getStartDate().toString());
                    ja.put(jo);
                }
            }
        }
    }
    out.print(ja);

%>