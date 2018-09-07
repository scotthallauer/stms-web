<%@ page import="org.json.*" %>
<%!
    public boolean bool(Object obj){
        return ((Boolean)obj).booleanValue();
    }
%>
<%
    // Set up the session's "auth" attribute to initial value
    if(session.getAttribute("auth") == null){
        session.setAttribute("auth", false);
    }

    // If the user is accessing a restricted AJAX request
    if(authRequired && ajaxRequest && !bool(session.getAttribute("auth"))){
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();
        jo.put("action", "error");
        jo.put("auth", "false");
        ja.put(jo);
        out.print(ja);
        return;
    }

    // If the user is on a restricted page and they are not logged in, then redirect them to the login page
    if(authRequired && !bool(session.getAttribute("auth"))){
        response.sendRedirect("/login.jsp");
        return;
    }
%>