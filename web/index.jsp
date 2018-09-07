<%@ page import="com.stms.web.*" %>
<%! boolean authRequired = true; %>
<%! boolean ajaxRequest = false; %>
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
				<h3></h3>
				<div id="stms_task_suggestion_wrapper">
					<h4>Suggestions</h4>
					<table id="stms_task_suggestion_list"><tbody></tbody></table>
				</div>
				<div id="stms_task_user_wrapper">
					<h4>Your Tasks<div class='dhx_task_create_button' aria-label='Create' role='button'>Create</div></h4>
					<div id="stms_tasks_grid"></div>
					<div id="stms_tasks_none">No Tasks</div>
				</div>
			</div>
		</div>
		<div id="stms_assignments_upcoming">
			<h4>Your Assignments<div class='dhx_assignment_create_button' aria-label='Create' role='button'>Create</div></h4>
			<table id="stms_assignments_upcoming_list" class="stms_assignments_list_table">
				<tbody>
					<tr class="stms_assignments_duedate_row">
						<td>Due Yesterday</td>
					</tr>
					<tr class="stms_assignments_task_row">
						<td>
							<table class="stms_assignment_block colour_blue overdue">
								<tbody>
								<tr class="stms_assignment_header_row">
									<td><div class="coloured_checkbox colour_blue"><input type="checkbox"></div>CSC3003S - Computer Science</td>
									<td>09:00</td>
								</tr>
								<tr class="stms_assignment_cont_row stms_assignment_course_row">
									<td colspan="2">Capstone Project</td>
								</tr>
								<tr class="stms_assignment_cont_row">
									<td colspan="2">Priority: High</td>
								</tr>
								<tr class="stms_assignment_cont_row stms_assignment_weighting_row">
									<td colspan="2">Weighting: 10%</td>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr class="stms_assignments_duedate_row">
						<td>Due Tomorrow</td>
					</tr>
					<tr class="stms_assignments_task_row">
						<td>
							<table class="stms_assignment_block colour_green">
								<tbody>
								<tr class="stms_assignment_header_row">
									<td><div class="coloured_checkbox colour_green"><input type="checkbox"></div>MCB3023S - Genetics</td>
									<td>17:00</td>
								</tr>
								<tr class="stms_assignment_cont_row stms_assignment_course_row">
									<td colspan="2">Mid-term Report</td>
								</tr>
								<tr class="stms_assignment_cont_row">
									<td colspan="2">Priority: High</td>
								</tr>
								<tr class="stms_assignment_cont_row stms_assignment_weighting_row">
									<td colspan="2">Weighting: 10%</td>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr class="stms_assignments_duedate_row">
						<td>Due 12 September</td>
					</tr>
					<tr class="stms_assignments_task_row">
						<td>
							<table class="stms_assignment_block colour_blue">
								<tbody>
								<tr class="stms_assignment_header_row">
									<td><div class="coloured_checkbox colour_blue"><input type="checkbox"></div>CSC3003S - Computer Science</td>
									<td>09:00</td>
								</tr>
								<tr class="stms_assignment_cont_row stms_assignment_course_row">
									<td colspan="2">Capstone Project</td>
								</tr>
								<tr class="stms_assignment_cont_row">
									<td colspan="2">Priority: High</td>
								</tr>
								<tr class="stms_assignment_cont_row stms_assignment_weighting_row">
									<td colspan="2">Weighting: 10%</td>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr class="stms_assignments_task_row">
						<td>
							<table class="stms_assignment_block colour_green">
								<tbody>
								<tr class="stms_assignment_header_row">
									<td><div class="coloured_checkbox colour_green"><input type="checkbox"></div>MCB3023S - Genetics</td>
									<td>17:00</td>
								</tr>
								<tr class="stms_assignment_cont_row stms_assignment_course_row">
									<td colspan="2">Mid-term Report</td>
								</tr>
								<tr class="stms_assignment_cont_row">
									<td colspan="2">Priority: High</td>
								</tr>
								<tr class="stms_assignment_cont_row stms_assignment_weighting_row">
									<td colspan="2">Weighting: 10%</td>
								</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="stms_assignments_none">No Upcoming Assignments</div>
		</div>
		<div id="stms_assignments_completed">
			<table id="stms_assignments_completed_list" class="stms_assignments_list_table">
				<tbody>

				</tbody>
			</table>
			<div class="stms_assignments_none">No Completed Assignments</div>
		</div>
        <div id="stms_semesters_left">
            <div id="stms_semesters_button_bar"><h2>Semesters</h2><div id='stms_semester_create_button' class='dhx_semester_create_button' aria-label='Create' role='button'>Create</div></div>
            <div id="stms_semesters_list_wrapper">
                <table id="stms_semesters_list"><tbody></tbody></table>
				<div id="stms_semesters_none">No Semesters</div>
            </div>
        </div>
        <div id="stms_semesters_right">
			<div id="stms_semester_form_wrapper">
				<h2>New Semester</h2>
				<div id="stms_semester_dhx_form"></div>
			</div>
			<div id="stms_course_form_wrapper" style="display: none">
				<h2>New Course</h2>
				<div id="stms_course_dhx_form"></div>
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
	<script src="./js/libraries/ajax-interceptor/interceptor.js" type="text/javascript"></script>
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