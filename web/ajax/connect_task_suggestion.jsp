<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Comparator" %>
<%! boolean authRequired = true; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    User user = (User)session.getAttribute("user");
    Semester[] semesters = user.getSemesters();
    Timestamp now = new Timestamp(System.currentTimeMillis());

    ArrayList<JSONObject> oa = new ArrayList<JSONObject>();

    // add upcoming graded sessions to the array
    for(int i = 0 ; i < semesters.length ; i++) {
        Course[] courses = semesters[i].getCourses();
        for (int j = 0; j < courses.length; j++) {
            CourseSession[] sessions = courses[j].getGradedSessions(); // only interested in graded sessions
            for (int k = 0; k < sessions.length; k++) {
                if(now.before(sessions[k].getStartDate())) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", sessions[k].getSessionID());
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
                    jo.put("priority", Priority.calculate(dueDate, userPriority, weighting));
                    if(weighting == null){
                        jo.put("weighting", "null");
                    }else {
                        DecimalFormat decimal = new DecimalFormat("0.##");
                        jo.put("weighting", decimal.format(weighting));
                    }
                    jo.put("dueDate", sessions[k].getStartDate().toString());
                    oa.add(jo);
                }
            }
        }
    }

    // add upcoming assignments to the array (still to implement)


    // prepare JSON array for output (e.g. sort and trim to a maximum length)
    JSONArray ja = new JSONArray();
    oa.sort(new Comparator<JSONObject>() {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            double p1, p2;
            try{
                p1 = o1.getDouble("priority");
                p2 = o2.getDouble("priority");
            }catch(Exception e){
                p1 = 0;
                p2 = 0;
            }
            if(p1 > p2) {
                return -1;
            }else if(p1 < p2){
                return 1;
            }else{
                return 0;
            }
        }
    });
    for(int i = 0 ; i < oa.size() && i < 3 ; i++){
        ja.put(oa.get(i));
    }

    // output the array
    out.print(ja);

%>