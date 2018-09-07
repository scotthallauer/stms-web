<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.text.DecimalFormat" %>
<%! boolean authRequired = true; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    String method = request.getMethod();
    User user = (User)session.getAttribute("user");

    // LOAD ASSIGNMENTS //
    if(method.equals("GET")){

        Semester[] semesters = user.getSemesters();

        JSONArray ja = new JSONArray(); // JSON semester array
        for(int i = 0 ; i < semesters.length ; i++){
            Course[] courses = semesters[i].getCourses();
            for(int j = 0 ; j < courses.length ; j++) {
                CourseAssignment[] assignments = courses[j].getAssignments();
                for(int k = 0 ; k < assignments.length ; k++) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", "a" + assignments[k].getAssignmentID());
                    jo.put("course_name", courses[j].getName());
                    jo.put("course_code", courses[j].getCode());
                    jo.put("colour", courses[j].getColour());
                    jo.put("description", assignments[k].getDescription());
                    jo.put("due_date", assignments[k].getDueDate());
                    jo.put("priority", assignments[k].getPriority());
                    DecimalFormat decimal = new DecimalFormat("0.##");
                    jo.put("weighting", decimal.format(assignments[k].getWeighting()));
                    jo.put("study_hours", assignments[k].getStudyHours());
                    jo.put("complete", assignments[k].isComplete());
                    ja.put(jo);
                }
            }
        }

        out.print(ja);
        return;

    }

    // MODIFY ASSIGNNMENTS //
    else{



    }

%>