<!DOCTYPE html>
<html>
	<head>
		<title>Scheduler</title>
		<link rel="stylesheet" type="text/css" href="./css/normalize.css">
		<link rel="stylesheet" type="text/css" href="./libraries/dhtmlxSuite/dhtmlx.css">
		<link rel="stylesheet" type="text/css" href="./libraries/dhtmlxScheduler/dhtmlxscheduler_material.css">
		<style type="text/css" media="screen">
			html, body{
				margin:0px;
				padding:0px;
				height:100%;
				overflow:hidden;
			}   
		</style>
		<script src="./libraries/dhtmlxSuite/dhtmlx.js" type="text/javascript"></script> 
		<script src="./libraries/dhtmlxScheduler/dhtmlxscheduler.js" type="text/javascript"></script> 
		<script type="text/javascript">
			dhtmlxEvent(window, 'load', function(){ 
				scheduler.init('scheduler_here', new Date(), 'month');
			});
		</script>
	</head>
	<body>
		<div id="scheduler_here" class="dhx_cal_container" style='width:100%; height:100%;'>
			<div class="dhx_cal_navline">
				<div class="dhx_cal_prev_button">&nbsp;</div>
				<div class="dhx_cal_next_button">&nbsp;</div>
				<div class="dhx_cal_today_button"></div>
				<div class="dhx_cal_date"></div>
				<div class="dhx_cal_tab" name="day_tab" style="right:204px;"></div>
				<div class="dhx_cal_tab" name="week_tab" style="right:140px;"></div>
				<div class="dhx_cal_tab" name="month_tab" style="right:76px;"></div>
			</div>
			<div class="dhx_cal_header"></div>
			<div class="dhx_cal_data"></div>       
		</div>
	</body>
</html>