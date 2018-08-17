<%! boolean authRequired = false; %>
<%@ include file="./includes/session.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Monthly Mentor - Register</title>
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
        <script src="./js/register.js" type="text/javascript"></script>
    </head>
    <body class="stms_pale_container">
        <div id="stms_register_container">
            <div id="stms_register_top">
                <div id="stms_register_icon"></div>
                <div id="stms_register_title">Monthly Mentor</div>
            </div>
            <div id="stms_register_bottom">
                <div id="stms_register_desc">Fill in your details to create an account</div>
                <div id="stms_register_dhx_form"></div>
                <div id="stms_register_links">Already have an account? <a href="./login.jsp">Log in</a></div>
            </div>
        </div>
    </body>
</html>
