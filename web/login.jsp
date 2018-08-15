<%! boolean authRequired = false; %>
<%@ include file="./includes/session.jsp" %>
<%
    // Redirect to application if user is already logged in
    if(bool(session.getAttribute("auth"))){
        response.sendRedirect("/");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Monthly Mentor - Log In</title>
        <link rel="shortcut icon" href="./media/icons/favicon.ico"/>
        <link rel="stylesheet" type="text/css" href="./css/normalize.css">
        <link rel="stylesheet" type="text/css" href="./js/libraries/dhtmlxSuite/dhtmlx.css">
        <link rel="stylesheet" type="text/css" href="./js/libraries/dhtmlxScheduler/dhtmlxscheduler_material.css">
        <link rel="stylesheet" type="text/css" href="./js/libraries/loadingModal/jquery.loadingModal.min.css">
        <link rel="stylesheet" type="text/css" href="./css/stms.css">
        <script src="./js/libraries/jquery/jquery-3.3.1.min.js" type="text/javascript"></script>
        <script src="./js/libraries/dhtmlxSuite/dhtmlx.js" type="text/javascript"></script>
        <script src="./js/libraries/dhtmlxScheduler/dhtmlxscheduler.js" type="text/javascript"></script>
        <script src="./js/libraries/sweetalert/sweetalert.min.js" type="text/javascript"></script>
        <script src="./js/libraries/loadingModal/jquery.loadingModal.min.js" type="text/javascript"></script>
        <script src="./js/login.js" type="text/javascript"></script>
    </head>
    <body class="stms_pale_container">
        <div id="stms_login_container">
            <div id="stms_login_top">
                <div id="stms_login_icon"></div>
                <div id="stms_login_title">Monthly Mentor</div>
            </div>
            <div id="stms_login_bottom">
                <div id="stms_login_desc">Log in to continue to your account</div>
                <div id="stms_login_dhx_form"></div>
                <div id="stms_login_links">Not registered? <a href="./register.jsp">Create an account</a></div>
            </div>
        </div>
    </body>
</html>
