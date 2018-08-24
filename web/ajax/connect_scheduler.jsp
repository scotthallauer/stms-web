<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.Types" %>
<%@ page import="java.sql.Timestamp" %>
<%! boolean authRequired = true; %>
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

    // LOAD CALENDAR //
    if(method.equals("GET")){

        User user = (User)session.getAttribute("user");
        Semester[] semesters = user.getSemesters();

        JSONArray ja = new JSONArray();
        for(int i = 0 ; i < semesters.length ; i++) {
            Course[] courses = semesters[i].getCourses();
            for (int j = 0; j < courses.length; j++) {
                CourseSession[] sessions = courses[j].getSessions();
                for (int k = 0; k < sessions.length; k++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", String.valueOf(sessions[k].getSessionID()));
                    jo.put("course_id", courses[j].getCourseID());
                    jo.put("event_pid", String.valueOf(sessions[k].getSessionPID()));
                    jo.put("event_type", String.valueOf(sessions[k].getType()));
                    jo.put("text", courses[j].getName() + " - " + Utilities.capitalise(sessions[k].getType()));
                    jo.put("start_date", sessions[k].getStartDate().toString());
                    jo.put("end_date", sessions[k].getEndDate().toString());
                    jo.put("event_length", String.valueOf(sessions[k].getLength()));
                    jo.put("rec_type", sessions[k].getRecType());
                    jo.put("color", getColourCode(courses[j].getColour()));
                    jo.put("textColor", "#FFFFFF");
                    ja.put(jo);
                }
            }
        }
        out.print(ja);
    }

    // MODIFY CALENDAR //
    else{
        String action = request.getParameter("!nativeeditor_status");

        // Get parameters
        Integer eventID;
        try {
            eventID = Integer.valueOf(request.getParameter("id"));
        }catch(Exception e){
            eventID = null;
        }
        Integer eventCourseID;
        try {
            eventCourseID = Integer.valueOf(request.getParameter("course_id"));
        }catch(Exception e){
            eventCourseID = null;
        }
        Integer eventPID;
        try{
            eventPID = Integer.valueOf(request.getParameter("event_pid"));
        }catch(Exception e){
            eventPID = null;
        }
        String eventType;
        try {
            eventType = String.valueOf(request.getParameter("event_type")).toLowerCase();
            if(eventType.length() < 1){
                eventType = null;
            }
        }catch(Exception e){
            eventType = null;
        }
        Timestamp eventStartDate = Timestamp.valueOf(request.getParameter("start_date"));
        Timestamp eventEndDate = Timestamp.valueOf(request.getParameter("end_date"));
        Integer eventLength;
        try{
            eventLength = Integer.valueOf(request.getParameter("event_length"));
        }catch(Exception e){
            eventLength = null;
        }
        String eventRecType = request.getParameter("rec_type");

        JSONObject jo = new JSONObject();
        if(action.equals("inserted")){

            CourseSession cs = new CourseSession();
            cs.setSessionPID(eventPID);
            cs.setCourseID(eventCourseID);
            cs.setType(eventType);
            cs.setStartDate(eventStartDate);
            cs.setEndDate(eventEndDate);
            cs.setLength(eventLength);
            cs.setRecType(eventRecType);
            if(cs.save()){
                if(eventRecType.equals("none")){
                    jo.put("action", "deleted");
                }else{
                    jo.put("action", "inserted");
                    jo.put("tid", cs.getSessionID());
                }
            }else{
                jo.put("action", "error");
            }

        }else if(action.equals("updated")){

            CourseSession cs = new CourseSession(eventID);
            cs.setSessionPID(eventPID);
            cs.setCourseID(eventCourseID);
            cs.setType(eventType);
            cs.setStartDate(eventStartDate);
            cs.setEndDate(eventEndDate);
            cs.setLength(eventLength);
            cs.setRecType(eventRecType);
            if(cs.save()){
                if(eventRecType.length() > 0 && !eventRecType.equals("none")){
                    String sql = "DELETE FROM courseSession WHERE sessionPID = ?";
                    Object[] params = new Object[1];
                    int[] types = new int[1];
                    params[0] = eventID;
                    types[0] = Types.INTEGER;
                    Database.update(sql, params, types);
                }
                jo.put("action", "updated");
            }else{
                jo.put("action", "error");
            }

        }else if(action.equals("deleted") && eventPID != null && eventPID != 0) {

            eventRecType = "none";
            CourseSession cs = new CourseSession(eventID);
            cs.setSessionPID(eventPID);
            cs.setCourseID(eventCourseID);
            cs.setType(eventType);
            cs.setStartDate(eventStartDate);
            cs.setEndDate(eventEndDate);
            cs.setLength(eventLength);
            cs.setRecType(eventRecType);
            if(cs.save()){
                jo.put("action", "deleted");
            }else{
                jo.put("action", "error");
            }

        }else if(action.equals("deleted")){

            String sql = "DELETE FROM courseSession WHERE sessionID = ?";
            Object[] params = new Object[1];
            int[] types = new int[1];
            params[0] = eventID;
            types[0] = Types.INTEGER;
            if(Database.update(sql, params, types)){
                if(eventRecType != null && eventRecType != "none"){
                    sql = "DELETE FROM courseSession WHERE sessionPID = ?";
                    params = new Object[1];
                    types = new int[1];
                    params[0] = eventID;
                    types[0] = Types.INTEGER;
                    Database.update(sql, params, types);
                }
                jo.put("action", "deleted");
            }else{
                jo.put("action", "error");
            }

        }else{
            jo.put("action", "error");
        }
        out.print(jo);
    }
%>