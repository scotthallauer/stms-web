<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.Types" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="javax.xml.crypto.Data" %>
<%@ page import="java.sql.ResultSet" %>
<%! boolean authRequired = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    if(Database.isConnected()){

        User user = (User)session.getAttribute("user");
        String sql;
        Object[] params;
        int[] types;
        Integer id;
        try{
            id = Integer.valueOf(request.getParameter("id"));
        }catch(Exception e){
            id = null;
        }
        if(id != null) {
            sql = "SELECT * FROM course " +
                    "INNER JOIN semester " +
                    "ON course.semesterID1 = semester.semesterID " +
                    "WHERE semester.userID = ? AND " +
                    "course.courseID = ? " +
                    "ORDER BY course.courseName ASC, course.courseCode ASC";
            params = new Object[2];
            types = new int[2];
            params[0] = user.getUserID();
            types[0] = Types.INTEGER;
            params[1] = id;
            types[1] = Types.INTEGER;
        }else{
            String filter = "";
            if (request.getParameter("mask") == null) {
                filter = "%";
            } else {
                filter = "%" + request.getParameter("mask") + "%";
            }
            sql = "SELECT * FROM course " +
                    "INNER JOIN semester " +
                    "ON course.semesterID1 = semester.semesterID " +
                    "WHERE semester.userID = ? AND " +
                    "(course.courseName LIKE ? OR course.courseCode LIKE ? OR CONCAT(course.courseCode, \" - \", course.courseName) LIKE ?) " +
                    "ORDER BY course.courseName ASC, course.courseCode ASC";
            params = new Object[4];
            types = new int[4];
            params[0] = user.getUserID();
            types[0] = Types.INTEGER;
            params[1] = filter;
            types[1] = Types.VARCHAR;
            params[2] = filter;
            types[2] = Types.VARCHAR;
            params[3] = filter;
            types[3] = Types.VARCHAR;
        }
        ResultSet rs = Database.query(sql, params, types);
        JSONArray ja = new JSONArray();
        if(rs.first()){
            do{
                JSONObject jo = new JSONObject();
                jo.put("value", rs.getInt("courseID"));
                rs.getString("courseCode");
                if(rs.wasNull()){
                    jo.put("text", rs.getString("courseName"));
                }else{
                    jo.put("text", rs.getString("courseCode") + " - " + rs.getString("courseName"));
                }
                jo.put("css", "background-image: url(/media/colours/" + rs.getString("colour") + ".png); margin-left: 5px; padding-left: 23px; background-repeat: no-repeat; background-position: left center;");
                ja.put(jo);
            }while(rs.next());
            JSONObject jo = new JSONObject();
            jo.put("options", ja);
            out.print(jo);
        }
    }

%>