// Set-up the page layout when the full HTML document has been downloaded
dhtmlxEvent(window, 'load', function(){

    ////////////////////////////////
    // OVERALL APPLICATION LAYOUT //
    ////////////////////////////////
    var stms_app_layout = new dhtmlXLayoutObject({

        parent:         document.body,
        pattern:        "1C",
        skin:           "material",

        offsets: {
            top: 0,
            right: 0,
            bottom: 0,
            left: 0
        },

        cells: [

            {
                id:         "a"
            }
        ]

    });

    // Custom header
    stms_app_layout.attachHeader("stms_header");

    // Pop-up for when user clicks their name in the top right
    var stms_account_popup = new dhtmlXPopup();
    stms_account_popup.attachList("option", [
        {id: 1, option: "Account Settings"},
        {id: 2, option: "Log Out"}
    ]);
    stms_account_popup.attachEvent("onClick", function(id){
        if(id == 2){
            window.location = "/logout.jsp";
        }
    });
    $("#stms_account").click(function(){
        if(stms_account_popup.isVisible()){
            stms_account_popup.hide();
        }else {
            var stms_account_area = document.getElementById("stms_account_icon");
            var x = window.dhx.absLeft(stms_account_area);
            var y = window.dhx.absTop(stms_account_area)+3;
            var width = stms_account_area.offsetWidth;
            var height = stms_account_area.offsetHeight;
            stms_account_popup.show(x, y, width, height);
        }
    });

    // Sidebar for page navigation
    var stms_sidebar = stms_app_layout.cells("a").attachSidebar({

        parent:         document.body,
        skin:           "material",
        template:       "icons_text",
        icons_path:     "media/icons/",
        width:          80,
        header:         false,
        autohide:       false,

        items: [

            {
                id:         "p1_calendar",
                text:       "Calendar",
                icon:       "calendar_icon.png",
                selected:   true
            },

            {
                id:         "p2_tasks",
                text:       "Tasks",
                icon:       "task_icon.png"
            }

        ]

    });

    ///////////////////
    // CALENDAR PAGE //
    ///////////////////
    scheduler.config.xml_date = "%Y-%m-%d %H:%i:%s";
    scheduler.config.api_date = "%Y-%m-%d %H:%i:%s";
    scheduler.config.repeat_date = "%d/%m/%Y";
    scheduler.config.first_hour = 4;
    scheduler.config.limit_time_select = true;
    scheduler.config.scroll_hour = 5;
    scheduler.config.include_end_by = true;
    scheduler.config.edit_on_create = true;
    scheduler.config.details_on_create = true;
    scheduler.config.details_on_dblclick = true;
    scheduler.config.time_step = 15;
    scheduler.locale.labels.section_courses = "Course";
    scheduler.locale.labels.section_types = "Type";
    scheduler.locale.labels.section_grades = "Graded";

    scheduler.form_blocks["grading"] = {
        render: function(sns){
            var out = "<div id='stms_lightbox_grading' class='stms_lightbox_grading_hidden'>" +
                         "<table>" +
                            "<tr>" +
                                "<td><input type='checkbox' onclick='lightboxGradeCheckbox(this)'/></td>" +
                                "<td>Priority:</td>" +
                                "<td><select><option value='3'>High</option><option value='2'>Normal</option><option value='1'>Low</option></select></td>" +
                            "</tr>" +
                            "<tr>" +
                                "<td></td>" +
                                "<td>Weighting:</td>" +
                                "<td><input type='number' min='0' max='100' step='1'>%</td>" +
                            "</tr>" +
                        "</table>" +
                      "</div>";
            return out;
        },
        set_value: function(node, value, ev){
            var flag = true;
            try{
                var priority = parseInt(value.split(",")[0]);
                var weighting = parseFloat(value.split(",")[1]);
                if(priority >= 1 && priority <= 3 && weighting >= 0 && weighting <= 100) {
                    $("div#stms_lightbox_grading input[type=checkbox]").click();
                    $("div#stms_lightbox_grading select").val(priority);
                    $("div#stms_lightbox_grading input[type=number]").val(weighting);
                }else{
                    flag = false;
                }
            }catch(err){
                var flag = false;
            }
            if(!flag){
                $("div#stms_lightbox_grading select").val(2);
                $("div#stms_lightbox_grading input[type=number]").val(0);
            }
        },
        get_value: function(node, ev){
            if($("div#stms_lightbox_grading input[type=checkbox]")[0].checked){
                return $("div#stms_lightbox_grading select").val() + "," + $("div#stms_lightbox_grading input[type=number]").val();
            }else{
                return "0,0";
            }
        },
        focus: function(node){}
    };

    scheduler.config.lightbox.sections = [
        {name: "courses", height: 30, type: "combo", map_to: "course_id", filtering: false, script_path: "./ajax/connect_course_combo.jsp"},
        {name: "types", height: 30, type: "combo", map_to: "event_type", filtering: false, script_path: "./ajax/connect_type_combo.jsp"},
        {name: "grades", type: "grading", map_to: "event_grade"},
        {name: "recurring", height: 115, type: "recurring", map_to: "rec_type", button: "recurring"},
        {name: "time", height: 72, type: "time", map_to: "auto"}
    ];

    stms_sidebar.cells("p1_calendar").attachScheduler(new Date(), 'week');

    // Add a create button to the top right of the calendar
    $("div.dhx_cal_navline").append("<div class='dhx_cal_create_button' aria-label='Create' role='button'>Create</div>");
    $("div.dhx_cal_create_button").click(function () {
        scheduler.addEventNow({
            text: "New Session",
            color: getColourCode("blue"),
            start_date: moment().startOf("hour").toDate(),
            end_date: moment().startOf("hour").add(1, "hour").toDate()
        });
    });

    // Set event details to default values when creating an event by dragging on the calendar
    scheduler.attachEvent("onEventCreated", function(id, ev){
        scheduler.getEvent(id).text = "New Session";
        scheduler.getEvent(id).color = getColourCode("blue");
        return true;
    });

    // Only allow dragging events to change their time if they are not readonly
    scheduler.attachEvent("onBeforeDrag", function(id){
        var event = scheduler.getEvent(id);
        if(event != null && event.readonly){
            return false;
        }else{
            return true;
        }
    });

    scheduler.attachEvent("onLightbox", function(id){
        $("div.dhx_cal_light").css("top", "10px");
        $("div.dhx_cal_larea").css("max-height", ($(window).height()-150) + "px");
        $("div.dhx_cal_larea").on("DOMSubtreeModified", function(){
            lightboxResize();
        });
        lightboxResize();
    });

    // configure scheduler to only show "details" button when event is readonly, otherwise show "edit" and "delete" icon
    scheduler.attachEvent("onClick", function(id){
        var event = scheduler.getEvent(id);
        if (event.readonly) {
            scheduler.config.icons_select = ["icon_details"];
        }else {
            scheduler.config.icons_select = ["icon_edit", "icon_delete"];
        }
        return true;
    });

    scheduler._click.buttons.edit = function(id){
        scheduler.showLightbox(id);
    }

    // This method does two things:
    // (1) Updates the client-side version of the calendar event with the right details when the user saves it
    // (2) Enables custom session types - if the course session "type" is null, then the user has entered a custom value
    //     this method fetches that value and correctly assigns it as the combo box value so that it gets submitted with the form
    //     ...it's a bit of a workaround because DHTMLX does not handle this like it's supposed to :(
    scheduler.attachEvent("onEventSave", function(id, ev, is_new){
        var type = $("div.dhx_cal_light div.dhx_wrap_section:nth-of-type(2) input[name=types]").val().toLowerCase();
            type = type.charAt(0).toUpperCase() + type.substr(1);
        var course = $("div.dhx_cal_light div.dhx_wrap_section:nth-of-type(1) input[type=text]").val();
        if(ev.event_grade == "0,0") {
            $("div.dhxcombo_option_selected div.dhxcombo_option_text").each(function (index, element) {
                if ($(element).text().replace(/\s/g, "") == course.replace(/\s/g, "")) {
                    var url = $(element).css("background-image");
                    var colour = url.substring(url.indexOf("colours/") + 8, url.indexOf(".png"));
                    ev.color = getColourCode(colour);
                }
            });
        }else{
            ev.color = "red";
        }
        var course_name = null;
        var course_code = null;
        if(course.indexOf("-") == -1){
            course_name = course;
        }else{
            course_name = course.substr(course.indexOf("-")+2);
            course_code = course.substring(0, course.indexOf("-")-1);
        }
        if(course_code != null) {
            ev.text = course_code + " - " + type;
        }else{
            ev.text = course_name + " - " + type;
        }
        if(ev.event_type == null) {
            ev.event_type = type.toLowerCase();
        }
        return true;
    });

    scheduler.load("./ajax/connect_scheduler.jsp", "json");
    dp = new dataProcessor("./ajax/connect_scheduler.jsp");
    dp.init(scheduler);
    dp.setTransactionMode("POST", false);
    dp.attachEvent("onAfterUpdate", function(id, action, tid, response){
        if(action == "inserted"){
            scheduler.changeEventId(id, tid);
        }
        return true;
    });

    ////////////////
    // TASKS PAGE //
    ////////////////
    var stms_task_layout = stms_sidebar.cells("p2_tasks").attachLayout({

        pattern:        "2U",
        skin:           "material",

        offsets: {
            top: 0,
            right: 0,
            bottom: 0,
            left: 0
        },

        cells: [

            {
                id:         "a",
                text:       "To-Do List",
                fix_size:   [true, true]
            },
            {
                id:         "b",
                fix_size:   [true, true]
            }
        ]

    });

    stms_task_layout.setSeparatorSize(0, 5);

    // when window resizes, set the two columns to 50% width and update task grid dimensions
    stms_app_layout.attachEvent("onResizeFinish", function(){
        var totalWidth = 0;
        totalWidth = stms_task_layout.cells("a").getWidth();
        totalWidth += stms_task_layout.cells("b").getWidth();
        stms_task_layout.cells("a").setWidth(totalWidth/2);
        stms_task_layout.cells("b").setWidth(totalWidth/2);
        var taskListWidth = (totalWidth/2)-22;
        var taskListHeight = stms_task_layout.cells("a").getHeight()-$("table.stms_task_suggestion_list").height()-203;
        taskListResize(taskListWidth, taskListHeight);
    });

    stms_task_layout.cells("a").attachObject("stms_tasks");

    var stms_task_grid = new dhtmlXGridObject("stms_tasks_grid"); // global variable because we need to access it from the taskListResize() function
    stms_task_grid.setImagePath("../js/libraries/dhtmlxSuite/imgs/");
    stms_task_grid.setHeader("Done,Task,Due");
    stms_task_grid.setNoHeader(true);
    stms_task_grid.setInitWidths("40,,100");
    stms_task_grid.setColAlign("center,left,right");
    stms_task_grid.setColTypes("ch,ed,dhxCalendar");
    stms_task_grid.setColSorting("int,str,date");
    stms_task_grid.setDateFormat("%d/%m/%Y");
    stms_task_grid.enableAutoHeight(true);
    stms_task_grid.enableAutoWidth(true);
    stms_task_grid.init();
    var initialWidth = stms_task_layout.cells("a").getWidth()-22;
    var initialHeight = stms_task_layout.cells("a").getHeight()-$("table.stms_task_suggestion_list").height()-203;
    taskListResize(initialWidth, initialHeight);

    // enable create button at the top of the task list
    $("div.dhx_task_create_button").click(function () {
        var newID = (new Date()).valueOf();
        stms_task_grid.addRow(newID, "", 0);
    });

    var data = {
        rows:[
            { id:1, data: ["0", "Hand in plagiarism form", "27/08/2018"]},
            { id:2, data: ["0", "Pay for lab coat", "29/08/2018"]},
            { id:3, data: ["0", "Set up study timetable", "02/09/2018"]}
        ]
    };
    stms_task_grid.parse(data,"json");

    var stms_assignment_tabbar = stms_task_layout.cells("b").attachTabbar({

        tabs: [
            {
                id:         "b1",
                text:       "Upcoming Assignments",
                active:     true
            },
            {
                id:         "b2",
                text:       "Completed Assignments"
            }
        ]

    });

    var stms_assignment_list_1 = stms_assignment_tabbar.tabs("b1").attachList({
        type:{
            template:"<span style='font-weight: bold'>#course#: #name#</span><br/>Priority: #priority#"
        }
    });
    stms_assignment_list_1.add({
        course: "CSC3002S",
        name: "Progress Report",
        priority: "Medium"
    });

    // hide loading screen after app has finished loading
    $("div#stms_loader").hide();
    $("body").loadingModal("hide");

});

// utility functions
function getColourCode(colourName){
    colourName = colourName.toLowerCase();
    if(colourName == "red"){
        return "#C3272B";
    }else if(colourName == "orange"){
        return "#F9690E";
    }else if(colourName == "yellow"){
        return "#FFA400";
    }else if(colourName == "green"){
        return "#26A65B";
    }else if(colourName == "blue"){
        return "#22A7F0";
    }else if(colourName == "purple"){
        return "#875F9A";
    }else{
        return "#95A5A6";
    }
}

function lightboxResize(){
    if($("div.dhx_cal_larea").outerHeight() < $("div.dhx_cal_larea").prop("scrollHeight")){
        $("div.dhx_cal_larea").css("box-shadow", "inset 0px 11px 8px -10px #CCC, inset 0px -11px 8px -10px #CCC");
        $("div.dhx_cal_larea").css("margin-bottom", "20px");
    }else{
        $("div.dhx_cal_larea").css("box-shadow", "");
        $("div.dhx_cal_larea").css("margin-bottom", "0");
    }
}

function taskListResize(newWidth, newHeight){
    if(newHeight > 0) {
        $("div#stms_tasks_grid").css("max-height", newHeight);
    }
    if(newWidth > 0) {
        $("div#stms_tasks_grid").width(newWidth);
    }
}

function lightboxGradeCheckbox(checkbox){
    if(checkbox.checked){
        $("#stms_lightbox_grading").removeClass("stms_lightbox_grading_hidden");
    }else{
        $("#stms_lightbox_grading").addClass("stms_lightbox_grading_hidden");
    }
    $("div.dhx_cal_larea").trigger('DOMSubtreeModified');
}