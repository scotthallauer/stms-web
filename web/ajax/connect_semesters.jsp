<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.Types" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%! boolean authRequired = true; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    String method = request.getMethod();
    User user = (User)session.getAttribute("user");

    // LOAD SEMESTERS/COURSES //
    if(method.equals("GET")){

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
        return;

    }

    // MODIFY SEMESTERS/COURSES //
    else{

        String action = request.getParameter("action");
        if(action == null || (!action.equals("inserted") && !action.equals("updated") && !action.equals("deleted"))){
            out.print(1);
            return;
        }

        // MODIFY SEMESTER //
        if(request.getParameter("semester_id") != null){

            Integer semesterID;
            try{
                semesterID = Integer.valueOf(request.getParameter("semester_id"));
            }catch(Exception e){
                semesterID = null;
            }
            String semesterName;
            try{
                semesterName = request.getParameter("semester_name");
            }catch(Exception e){
                semesterName = null;
            }
            LocalDate semesterStart;
            try{
                semesterStart = LocalDate.parse(request.getParameter("semester_start"));
            }catch(Exception e){
                semesterStart = null;
            }
            LocalDate semesterEnd;
            try{
                semesterEnd = LocalDate.parse(request.getParameter("semester_end"));
            }catch(Exception e){
                semesterEnd = null;
            }

            JSONObject jo = new JSONObject();
            if(action.equals("inserted")){
                Semester semester = new Semester();
                semester.setUserID(user.getUserID());
                semester.setName(semesterName);
                semester.setStartDate(semesterStart);
                semester.setEndDate(semesterEnd);
                if(semester.save()){
                    jo.put("action", "inserted");
                    jo.put("tid", semester.getSemesterID());
                }else{
                    jo.put("action", "error");
                }
            }else if(action.equals("updated")){
                Semester semester = new Semester(semesterID);
                if(semester.getUserID() == user.getUserID()) { // only allow user to edit their own semesters (security measure)
                    semester.setName(semesterName);
                    semester.setStartDate(semesterStart);
                    semester.setEndDate(semesterEnd);
                    if(semester.save()){
                        jo.put("action", "updated");
                        jo.put("tid", semester.getSemesterID());
                    }else{
                        jo.put("action", "error");
                    }
                }else{
                    jo.put("action", "error");
                }
            }else if(action.equals("deleted")){
                Semester semester = new Semester(semesterID);
                if(semester.getUserID() == user.getUserID()) { // only allow user to delete their own semesters (security measure)
                    if(semester.delete()){
                        jo.put("action", "deleted");
                    }else{
                        jo.put("action", "error");
                    }
                }else{
                    jo.put("action", "error");
                }
            }
            out.print(jo);
            return;

        // MODIFY COURSE //
        }else if(request.getParameter("course_id") != null){

            Integer courseID;
            try{
                courseID = Integer.valueOf(request.getParameter("course_id"));
            }catch(Exception e){
                courseID = null;
            }
            String courseName;
            try{
                courseName = request.getParameter("course_name");
            }catch(Exception e){
                courseName = null;
            }
            String courseCode;
            try{
                courseCode = request.getParameter("course_code");
            }catch(Exception e){
                courseCode = null;
            }
            Integer courseSemester1;
            try{
                courseSemester1 = Integer.valueOf(request.getParameter("course_semester_1"));
            }catch(Exception e){
                courseSemester1 = null;
            }
            String courseColour;
            try{
                courseColour = request.getParameter("course_colour");
            }catch(Exception e){
                courseColour = null;
            }

            JSONObject jo = new JSONObject();
            if(action.equals("inserted")){
                Course course = new Course();
                course.setName(courseName);
                course.setCode(courseCode);
                course.setSemesterID1(courseSemester1);
                course.setColour(courseColour);
                if(course.save()){
                    jo.put("action", "inserted");
                    jo.put("tid", course.getCourseID());
                }else{
                    jo.put("action", "error");
                }
            }else if(action.equals("updated")){
                Course course = new Course(courseID);
                if(new Semester(course.getSemesterID1()).getUserID() == user.getUserID()) { // only allow user to edit their own courses (security measure)
                    course.setName(courseName);
                    course.setCode(courseCode);
                    course.setSemesterID1(courseSemester1);
                    course.setColour(courseColour);
                    if(course.save()){
                        jo.put("action", "updated");
                        jo.put("tid", course.getCourseID());
                    }else{
                        jo.put("action", "error");
                    }
                }else{
                    jo.put("action", "error");
                }
            }else if(action.equals("deleted")){
                Course course = new Course(courseID);
                if(new Semester(course.getSemesterID1()).getUserID() == user.getUserID()) { // only allow user to delete their own courses (security measure)
                    if(course.delete()){
                        jo.put("action", "deleted");
                    }else{
                        jo.put("action", "error");
                    }
                }else{
                    jo.put("action", "error");
                }
            }
            out.print(jo);
            return;

        }else {

            JSONObject jo = new JSONObject();
            jo.put("action", "error");
            out.print(jo);
            return;

        }

    }

%>