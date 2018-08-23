<%@ page import="com.stms.web.*" %>
<%! boolean authRequired = true; %>
<%@ include file="./includes/session.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Monthly Mentor</title>
		<link rel="shortcut icon" href="./media/icons/favicon.ico"/>
		<link rel="stylesheet" type="text/css" href="./css/normalize.css">
		<link rel="stylesheet" type="text/css" href="./js/libraries/loadingModal/jquery.loadingModal.min.css">
		<link rel="stylesheet" type="text/css" href="./css/stms.css">
		<script src="./js/libraries/jquery/jquery-3.3.1.min.js" type="text/javascript"></script>
		<script src="./js/libraries/loadingModal/jquery.loadingModal.min.js" type="text/javascript"></script>
	</head>
	<body>
	<div id="stms_loader"></div>
		<div id="stms_header">
			<div id="stms_title">
				<div id="stms_title_icon"></div>
				<div id="stms_title_text">Monthly Mentor</div>
			</div>
			<div id="stms_account">
				<div id="stms_account_icon"><%= ((User)session.getAttribute("user")).getInitials() %></div>
				<div id="stms_account_text"><%= ((User)session.getAttribute("user")).getFirstName() %></div>
			</div>
		</div>
		<div id="stms_tasks">
			<div id="stms_tasks_header">
				<h2>To-Do List</h2>
			</div>
			<div id="stms_tasks_cont">
				<h3>Thursday, 23 August</h3>
				<h4>Suggestions</h4>
				<ul style="list-style-type: none">
					<li><div class="stms_priority_icon stms_priority_icon_high">!!!</div></li>
					<li><div class="stms_priority_icon stms_priority_icon_medium">!!</div></li>
					<li><div class="stms_priority_icon stms_priority_icon_low">!</div></li>
				</ul>
				<h4>Your Tasks</h4>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		// show loading screen while app loads
        $("body").loadingModal({
            position: "auto",
            color: "#000",
            opacity: "1.0",
            backgroundColor: "rgb(255,255,255)",
            animation: "doubleBounce"
        });
        $("body").loadingModal("show");
	</script>
	<!-- load application scripts after we have started the loading screen -->
	<script src="./js/libraries/dhtmlxSuite/dhtmlx.js" type="text/javascript"></script>
	<script src="./js/libraries/dhtmlxScheduler/dhtmlxscheduler.js" type="text/javascript"></script>
	<script src="./js/libraries/dhtmlxScheduler/ext/dhtmlxscheduler_recurring.js" type="text/javascript"></script>
	<script src="./js/libraries/dhtmlxScheduler/ext/dhtmlxscheduler_readonly.js" type="text/javascript"></script>
	<script src="./js/libraries/dhtmlxScheduler/sources/ext/dhtmlxscheduler_editors.js" type="text/javascript"></script>
	<script src="./js/libraries/sweetalert/sweetalert.min.js" type="text/javascript"></script>
	<script src="./js/libraries/moment.js/moment.js" type="text/javascript"></script>
	<script src="./js/app.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="./js/libraries/dhtmlxSuite/dhtmlx.css">
	<link rel="stylesheet" type="text/css" href="./js/libraries/dhtmlxScheduler/dhtmlxscheduler_material.css">
</html>