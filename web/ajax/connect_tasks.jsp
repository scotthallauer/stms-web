<%@ page import="com.stms.web.*" %>
<%@ page import="org.json.*" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.sql.Timestamp" %>
<%! boolean authRequired = true; %>
<%! boolean ajaxRequest = true; %>
<%@ include file="../includes/session.jsp" %>
<%

    String method = request.getMethod();
    User user = (User)session.getAttribute("user");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");

    // LOAD TASKS //
    if(method.equals("GET")){

        Task[] tasks = user.getTasks();
        JSONObject container = new JSONObject();
        JSONArray joa = new JSONArray();

        for(int i = 0 ; i < tasks.length ; i++) {
            if(!tasks[i].isComplete()) {
                JSONObject jo = new JSONObject();
                JSONArray jia = new JSONArray();
                jia.put("0");
                jia.put(tasks[i].getDescription());
                LocalDateTime dueDate = tasks[i].getDueDate().toLocalDateTime();
                jia.put(dueDate.format(formatter));
                jo.put("id", tasks[i].getTaskID());
                jo.put("data", jia);
                joa.put(jo);
            }
        }
        container.put("rows", joa);
        out.print(container);
        return;

    }

    // MODIFY TASKS //
    else{

        String action = request.getParameter("action");

        JSONObject jo = new JSONObject();

        Integer taskID;
        try{
            taskID = Integer.valueOf(request.getParameter("id"));
        }catch(Exception e){
            taskID = null;
        }
        String taskDescription = request.getParameter("description");
        if(taskDescription.length() == 0){
            jo.put("action", "deleted");
            out.print(jo);
            return;
        }
        Timestamp taskDueDate;
        try {
            taskDueDate = Timestamp.valueOf(LocalDateTime.parse(request.getParameter("due_date"), formatter));
        }catch(Exception e){
            taskDueDate = null;
        }
        Boolean taskComplete;
        try{
            if(Integer.valueOf(request.getParameter("complete")) == 1){
                taskComplete = true;
            }else{
                taskComplete = false;
            }
        }catch(Exception e){
            taskComplete = null;
        }

        if(action.equals("inserted")){

            Task task = new Task();
            task.setUserID(user.getUserID());
            task.setDescription(taskDescription);
            task.setDueDate(taskDueDate);
            task.setComplete(taskComplete);
            if(task.save()){
                jo.put("action", "inserted");
                jo.put("tid", String.valueOf(task.getTaskID()));
            }else{
                jo.put("action", "error");
            }

        }else if(action.equals("updated")){

            Task task = new Task(taskID);
            task.setDescription(taskDescription);
            task.setDueDate(taskDueDate);
            task.setComplete(taskComplete);
            if(task.save()){
                jo.put("action", "updated");
            }else{
                jo.put("action", "error");
            }

        }else if(action.equals("deleted")){

            Task task = new Task(taskID);
            if(task.delete()){
                jo.put("action", "deleted");
            }else{
                jo.put("action", "error");
            }

        }else{

            jo.put("action", "error");

        }

        out.print(jo);
        return;

    }

%>