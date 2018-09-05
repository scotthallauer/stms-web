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
        }else if(name.equals("purple")) {
            return "#875F9A";
        }else if(name.equals("pink")){
            return "#F47983";
        }else{
            return "#95A5A6"; // grey
        }
    }

%>
<%

    String method = request.getMethod();
    User user = (User)session.getAttribute("user");

    // LOAD CALENDAR //
    if(method.equals("GET")){

        Semester[] semesters = user.getSemesters();

        JSONArray ja = new JSONArray();
        for(int i = 0 ; i < semesters.length ; i++) {
            // load course sessions
            Course[] courses = semesters[i].getCourses();
            for (int j = 0; j < courses.length; j++) {
                CourseSession[] courseSessions = courses[j].getSessions();
                for (int k = 0; k < courseSessions.length; k++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", "c" + String.valueOf(courseSessions[k].getSessionID()));
                    jo.put("course_id", courses[j].getCourseID());
                    if(courseSessions[k].getSessionPID() != 0) {
                        jo.put("event_pid", "c" + String.valueOf(courseSessions[k].getSessionPID()));
                    }else{
                        jo.put("event_pid", 0);
                    }
                    jo.put("event_type", String.valueOf(courseSessions[k].getType()));
                    if(courses[j].getCode() != null){
                        jo.put("text", courses[j].getCode() + " - " + Utilities.capitalise(courseSessions[k].getType()));
                    }else {
                        jo.put("text", courses[j].getName() + " - " + Utilities.capitalise(courseSessions[k].getType()));
                    }
                    if(courseSessions[k].getPriority() != null){
                        jo.put("event_grade", String.valueOf(courseSessions[k].getPriority()) + "," + String.valueOf(courseSessions[k].getWeighting()) + "," + String.valueOf(courseSessions[k].getStudyHours()));
                        jo.put("color", "red");
                    }else{
                        jo.put("event_grade", "0,0,0");
                        jo.put("color", getColourCode(courses[j].getColour()));
                    }
                    jo.put("start_date", courseSessions[k].getStartDate().toString());
                    jo.put("end_date", courseSessions[k].getEndDate().toString());
                    jo.put("event_length", String.valueOf(courseSessions[k].getLength()));
                    jo.put("rec_type", courseSessions[k].getRecType());
                    jo.put("textColor", "#FFFFFF");
                    ja.put(jo);
                }
            }
            // load study sessions
            StudySession[] studySessions = semesters[i].getStudySessions();
            for(int j = 0 ; j < studySessions.length ; j++){
                JSONObject jo = new JSONObject();
                jo.put("id", "s" + String.valueOf(studySessions[j].getStudySessionID()));
                jo.put("text", "Study Session");
                jo.put("start_date", studySessions[j].getStartTime().toString());
                jo.put("end_date", studySessions[j].getEndTime().toString());
                jo.put("textColor", "#FFFFFF");
                jo.put("color", getColourCode("grey"));
                ja.put(jo);
            }
        }
        out.print(ja);
    }

    // MODIFY CALENDAR //
    else{
        String action = request.getParameter("!nativeeditor_status");

        // Get parameters
        boolean eventIsCourseSession = (request.getParameter("id").charAt(0) == 'c' || request.getParameter("id").charAt(0) != 's');
        boolean eventIsStudySession = (request.getParameter("id").charAt(0) == 's');
        Integer eventID;
        try {
            if(request.getParameter("id").charAt(0) == 'c' || request.getParameter("id").charAt(0) == 's') {
                eventID = Integer.valueOf(request.getParameter("id").substring(1));
            }else{
                eventID = Integer.valueOf(request.getParameter("id"));
            }
        }catch(Exception e){
            eventID = null;
        }
        Integer eventCourseID;
        try {
            eventCourseID = Integer.valueOf(request.getParameter("course_id"));
        }catch(Exception e){
            eventCourseID = null;
        }
        Integer eventPID = null;
        try{
            if(request.getParameter("event_pid").charAt(0) == 'c') {
                eventPID = Integer.valueOf(request.getParameter("event_pid").substring(1));
            }
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
        Integer eventPriority;
        try{
            eventPriority = Integer.valueOf(request.getParameter("event_grade").split(",")[0]);
            if(eventPriority == 0){
                eventPriority = null;
            }
        }catch(Exception e){
            eventPriority = null;
        }
        Double eventWeighting = null;
        if(eventPriority != null){
            try{
                eventWeighting = Double.valueOf(request.getParameter("event_grade").split(",")[1]);
                if(eventWeighting == 0){
                    eventWeighting = null;
                }
            }catch(Exception e){
                eventWeighting = null;
            }
        }
        Integer eventStudyHours = null;
        if(eventPriority != null){
            try{
                eventStudyHours = Integer.valueOf(request.getParameter("event_grade").split(",")[2]);
                if(eventStudyHours == 0){
                    eventStudyHours = null;
                }
            }catch(Exception e){
                eventStudyHours = null;
            }
        }
        Timestamp eventStartDate = Timestamp.valueOf(request.getParameter("start_date"));
        Timestamp eventEndDate = Timestamp.valueOf(request.getParameter("end_date"));
        Long eventLength;
        try{
            eventLength = Long.valueOf(request.getParameter("event_length"));
        }catch(Exception e){
            eventLength = null;
        }
        String eventRecType = request.getParameter("rec_type");
        if(eventRecType != null && eventRecType.length() < 1){
            eventRecType = null;
        }

        JSONObject jo = new JSONObject();
        if(action.equals("inserted")){

            // insert new course session
            if(eventIsCourseSession) {

                CourseSession cs = new CourseSession();
                cs.setSessionPID(eventPID);
                cs.setCourseID(eventCourseID);
                cs.setType(eventType);
                cs.setPriority(eventPriority);
                cs.setWeighting(eventWeighting);
                cs.setStudyHours(eventStudyHours);
                cs.setStartDate(eventStartDate);
                cs.setEndDate(eventEndDate);
                cs.setLength(eventLength);
                cs.setRecType(eventRecType);
                if (cs.save()) {

                    if (eventRecType != null && eventRecType.equals("none")) {
                        jo.put("action", "deleted");
                    } else {
                        if (cs.isGraded()) {
                            cs.scheduleStudySessions();
                            jo.put("refresh", true);
                        }
                        jo.put("action", "inserted");
                        jo.put("tid", cs.getSessionID());
                    }
                } else {
                    jo.put("action", "error");
                }

            // user can't insert new study session (only delete and edit existing ones)
            }else{
                jo.put("action", "error");
            }

        }else if(action.equals("updated")){

            // update existing course session
            if(eventIsCourseSession) {

                CourseSession cs = new CourseSession(eventID);
                boolean oldIsGraded = cs.isGraded();
                Integer oldStudyHours = null;
                Timestamp oldStartDate = null;
                if (cs.isGraded()) {
                    oldStudyHours = cs.getStudyHours();
                    oldStartDate = cs.getStartDate();
                    jo.put("refresh", true);
                }
                cs.setSessionPID(eventPID);
                cs.setCourseID(eventCourseID);
                cs.setType(eventType);
                cs.setPriority(eventPriority);
                cs.setWeighting(eventWeighting);
                cs.setStudyHours(eventStudyHours);
                cs.setStartDate(eventStartDate);
                cs.setEndDate(eventEndDate);
                cs.setLength(eventLength);
                cs.setRecType(eventRecType);
                if (cs.save()) {
                    if (eventRecType != null && eventRecType.length() > 0 && !eventRecType.equals("none")) {
                        String sql = "DELETE FROM courseSession WHERE sessionPID = ?";
                        Object[] params = new Object[1];
                        int[] types = new int[1];
                        params[0] = eventID;
                        types[0] = Types.INTEGER;
                        Database.update(sql, params, types);
                    }
                    if((cs.isGraded() && (oldStudyHours != eventStudyHours || !oldStartDate.equals(eventStartDate))) ||
                       (oldIsGraded && !cs.isGraded())){
                        cs.scheduleStudySessions();
                    }
                    jo.put("action", "updated");
                } else {
                    jo.put("action", "error");
                }

            // update existing study session
            }else if(eventIsStudySession){

                StudySession ss = new StudySession(eventID);
                ss.setStartTime(eventStartDate);
                ss.setEndTime(eventEndDate);
                if (ss.save()) {
                    jo.put("action", "updated");
                } else {
                    jo.put("action", "error");
                }

            }else{
                jo.put("action", "error");
            }

        }else if(action.equals("deleted") && eventPID != null && eventPID != 0) {

            if(eventIsCourseSession) {

                eventRecType = "none";
                CourseSession cs = new CourseSession(eventID);
                if (cs.isGraded()) {
                    jo.put("refresh", true);
                }
                cs.setSessionPID(eventPID);
                cs.setCourseID(eventCourseID);
                cs.setType(eventType);
                cs.setPriority(eventPriority);
                cs.setWeighting(eventWeighting);
                cs.setStudyHours(eventStudyHours);
                cs.setStartDate(eventStartDate);
                cs.setEndDate(eventEndDate);
                cs.setLength(eventLength);
                cs.setRecType(eventRecType);
                if (cs.save()) {
                    jo.put("action", "deleted");
                } else {
                    jo.put("action", "error");
                }

            } else {
                jo.put("action", "error");
            }

        }else if(action.equals("deleted")){

            // delete existing course session
            if(eventIsCourseSession) {

                CourseSession cs = new CourseSession(eventID);
                if (cs.isGraded()) {
                    jo.put("refresh", true);
                }
                if(cs.delete()){
                    jo.put("action", "deleted");
                }else{
                    jo.put("action", "error");
                }

            // delete exisitng study session
            }else if(eventIsStudySession){

                StudySession ss = new StudySession(eventID);
                if(ss.delete()){
                    jo.put("action", "deleted");
                }else{
                    jo.put("action", "error");
                }

            } else {
                jo.put("action", "error");
            }

        }else{
            jo.put("action", "error");
        }
        out.print(jo);
    }
%>