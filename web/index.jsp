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
		<style type="text/css">
			/* Cover to hide the page until the app has finished loading */
			div#stms_loader{
				position: absolute;
				width: 100%;
				height: 100%;
				background-color: #FFFFFF;
				z-index: 10;
			}
		</style>
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
				<h3>Friday, 24 August</h3>
				<h4>Suggestions</h4>
				<table class="stms_task_suggestion_list">
					<tbody>
						<tr>
							<td rowspan="2" class="stms_task_suggestion_priority"><div class="stms_priority_icon stms_priority_icon_high">!!!</div></td>
							<td style="border-bottom: 0" class="stms_task_suggestion_text"><span class="stms_priority_text stms_priority_text_high">Study for Computer Science Test</span></td>
							<td style="border-bottom: 0" class="stms_task_suggestion_date">tomorrow</td>
						</tr>
						<tr>
							<td style="color: red; font-size: 13px">Weighting: 10%</td>
						</tr>
						<tr>
							<td class="stms_task_suggestion_priority"><div class="stms_priority_icon stms_priority_icon_medium">!!</div></td>
							<td class="stms_task_suggestion_text"><span class="stms_priority_text stms_priority_text_medium">Work on Computer Science Assignment</span></td>
							<td class="stms_task_suggestion_date">in 3 days</td>
						</tr>
						<tr>
							<td class="stms_task_suggestion_priority"><div class="stms_priority_icon stms_priority_icon_low">!</div></td>
							<td class="stms_task_suggestion_text"><span class="stms_priority_text stms_priority_text_low">Work on Philosophy Essay</span></td>
							<td class="stms_task_suggestion_date">in 6 days</td>
						</tr>
					</tbody>
				</table>
				<h4>Your Tasks<div class='dhx_task_create_button' aria-label='Create' role='button'>Create</div></h4>
				<div id="stms_tasks_grid"></div>
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
	<link rel="stylesheet" type="text/css" href="./css/stms.css">
</html>