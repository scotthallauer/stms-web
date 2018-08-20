<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.Types" %>
<%@ page import="java.sql.Timestamp" %>
<%! boolean authRequired = true; %>
<%@ include file="../includes/session.jsp" %>
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
                    jo.put("event_pid", String.valueOf(sessions[k].getSessionPID()));
                    jo.put("text", sessions[k].getName());
                    jo.put("start_date", sessions[k].getStartDate().toString());
                    jo.put("end_date", sessions[k].getEndDate().toString());
                    jo.put("event_length", String.valueOf(sessions[k].getLength()));
                    jo.put("rec_type", sessions[k].getRecType());
                    jo.put("color", courses[j].getColour());
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
        Integer eventPID;
        try{
            eventPID = Integer.valueOf(request.getParameter("event_pid"));
        }catch(Exception e){
            eventPID = null;
        }
        Integer eventCourseID = 3;
        String eventText = request.getParameter("text");
        String eventType = "lecture";
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
            cs.setName(eventText);
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
            cs.setName(eventText);
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
            cs.setName(eventText);
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