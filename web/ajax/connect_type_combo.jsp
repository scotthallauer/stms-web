<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.sql.Types" %>
<%@ page import="java.sql.ResultSet" %>
<%! boolean authRequired = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    if(Database.isConnected()){

        User user = (User)session.getAttribute("user");
        String sql;
        Object[] params;
        int[] types;
        String id = request.getParameter("id");
        if(id != null) {
            sql = "SELECT T.type FROM ( " +
                "(SELECT sessionType AS type " +
                "FROM courseSession " +
                "INNER JOIN " +
                "(" +
                    "SELECT courseID " +
                    "FROM course " +
                    "INNER JOIN semester " +
                    "ON course.semesterID1 = semester.semesterID " +
                    "WHERE semester.userID = ? " +
	            ") AS C " +
                "ON courseSession.courseID = C.courseID " +
                "GROUP BY courseSession.sessionType " +
                "ORDER BY courseSession.sessionType ASC) " +
                "UNION (SELECT 'lecture' AS type) " +
                "UNION (SELECT 'tutorial' AS type) " +
                "UNION (SELECT 'practical' AS type) " +
                "UNION (SELECT 'test' AS type) " +
                "UNION (SELECT 'exam' AS type) " +
                ") AS T " +
                "WHERE T.type = ? " +
                "ORDER BY T.type ASC";
            params = new Object[2];
            types = new int[2];
            params[0] = user.getUserID();
            types[0] = Types.INTEGER;
            params[1] = id;
            types[1] = Types.VARCHAR;
        }else{
            String filter = "";
            if (request.getParameter("mask") == null) {
                filter = "%";
            } else {
                filter = request.getParameter("mask") + "%";
            }
            sql = "SELECT T.type FROM ( " +
                    "(SELECT sessionType AS type " +
                    "FROM courseSession " +
                    "INNER JOIN " +
                    "(" +
                    "SELECT courseID " +
                    "FROM course " +
                    "INNER JOIN semester " +
                    "ON course.semesterID1 = semester.semesterID " +
                    "WHERE semester.userID = ? " +
                    ") AS C " +
                    "ON courseSession.courseID = C.courseID " +
                    "GROUP BY courseSession.sessionType " +
                    "ORDER BY courseSession.sessionType ASC) " +
                    "UNION (SELECT 'lecture' AS type) " +
                    "UNION (SELECT 'tutorial' AS type) " +
                    "UNION (SELECT 'practical' AS type) " +
                    "UNION (SELECT 'test' AS type) " +
                    "UNION (SELECT 'exam' AS type) " +
                    ") AS T " +
                    "WHERE T.type LIKE ? " +
                    "ORDER BY T.type ASC";
            params = new Object[2];
            types = new int[2];
            params[0] = user.getUserID();
            types[0] = Types.INTEGER;
            params[1] = filter;
            types[1] = Types.VARCHAR;
        }
        ResultSet rs = Database.query(sql, params, types);
        JSONArray ja = new JSONArray();
        if(rs.first()){
            do{
                JSONObject jo = new JSONObject();
                jo.put("value", rs.getString("type"));
                jo.put("text", Utilities.capitalise(rs.getString("type")));
                ja.put(jo);
            }while(rs.next());
            JSONObject jo = new JSONObject();
            jo.put("options", ja);
            out.print(jo);
        }
    }

%>