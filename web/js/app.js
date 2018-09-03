// global variables used as flags in app (they can be updated after AJAX calls and referred to from anywhere)
SEMESTER_COUNT = 0;
COURSE_COUNT = 0;

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
        {id: 2, option: "User Manual"},
        stms_account_popup.separator,
        {id: 3, option: "Log Out"}
    ]);
    stms_account_popup.attachEvent("onClick", function(id){
        if(id == 2){
            window.open("/media/documents/user_manual.pdf");
        }else if(id == 3){
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
            },

            {
                id:         "p3_semesters",
                text:       "Semesters",
                icon:       "semester_icon.png"
            }

        ]

    });

    // prepare pages when user switches between them
    stms_sidebar.attachEvent("onSelect", function(id, lastId){
        if(id == "p1_calendar"){
            loadEvents();
        }else if(id == "p2_tasks"){
            taskListResize();
            loadSuggestions(); // suggestion list table
        }else if(id == "p3_semesters"){
            loadSemesters();
        }
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

    // custom lightbox form section for graded sessions
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
        if(COURSE_COUNT == 0){
            swal({
                icon: "warning",
                title: "No Courses",
                text: "You haven't created any courses yet! Go to the \"Semesters\" page to create some semesters and courses."
            });
        }else{
            scheduler.addEventNow({
                text: "New Session",
                color: getColourCode("blue"),
                start_date: moment().startOf("hour").toDate(),
                end_date: moment().startOf("hour").add(1, "hour").toDate()
            });
        }
    });

    // Set event details to default values when creating an event by dragging on the calendar
    scheduler.attachEvent("onEventCreated", function(id, ev){
        scheduler.getEvent(id).text = "New Session";
        scheduler.getEvent(id).color = getColourCode("blue");
        scheduler.getEvent(id).textColor = "#FFFFFF";
        return true;
    });

    // Only allow dragging events to change their time if they are not readonly
    scheduler.attachEvent("onBeforeDrag", function(id){
        if(COURSE_COUNT == 0){
            swal({
                icon: "warning",
                title: "No Courses",
                text: "You haven't created any courses yet! Go to the \"Semesters\" page to create some semesters and courses."
            });
            return false;
        }else {
            var event = scheduler.getEvent(id);
            if (event != null && event.readonly) {
                return false;
            } else {
                return true;
            }
        }
    });

    scheduler.attachEvent("onLightbox", function(id){
        $("div.dhx_cal_light").css("top", "10px");
        $("div.dhx_cal_larea").css("max-height", ($(window).height()-150) + "px");
        $("div.dhx_cal_larea").off("DOMSubtreeModified");
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

    // preload events (will reload whenever user switches to this tab - handled in onSelect event for sidebar)
    loadEvents();

    // dataProcessor to communicate updates to events with the server
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

    // PAGE LAYOUT //
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
        taskListResize();
    });

    // TO-DO LIST COLUMN //
    stms_task_layout.cells("a").attachObject("stms_tasks");

    // set title to current date
    $("div#stms_tasks_cont h3").text(moment().format("dddd, Do MMMM"));

    // preload suggestion list table (will reload whenever user switches to this tab - handled in onSelect event for sidebar)
    loadSuggestions();

    // task list grid
    var stms_task_grid = new dhtmlXGridObject("stms_tasks_grid");
    stms_task_grid.setImagePath("../js/libraries/dhtmlxSuite/imgs/");
    stms_task_grid.setHeader("Done,Task,Due");
    stms_task_grid.setNoHeader(true);
    stms_task_grid.setInitWidths("40,,150");
    stms_task_grid.setColAlign("center,left,right");
    stms_task_grid.setColTypes("ch,ed,dhxCalendar");
    stms_task_grid.setColSorting("int,str,date");
    stms_task_grid.setDateFormat("%d/%m/%Y at %H:%i");
    stms_task_grid.enableAutoHeight(true);
    stms_task_grid.enableAutoWidth(true);
    stms_task_grid.enableEditEvents(true,true,true);
    stms_task_grid.init();

    // enable create button at the top of the task list
    $("div.dhx_task_create_button").click(function () {
        var newID = (new Date()).valueOf(); // generate unique id based on datetime
        var newDate = moment().add(1, 'days').format("DD/MM/YYYY [at 12:00]");
        stms_task_grid.addRow(newID, "0,," + newDate, 0); // insert row in grid with unchecked checkbox, empty task name and tomorrow noon as default due date
    });

    stms_task_grid.attachEvent("onRowAdded", function(rID){
        $("div#stms_tasks_grid").removeClass("gridbox_empty"); // remove the "No Tasks" placeholder
        $("div#stms_tasks_none").removeClass("gridbox_empty");
        stms_task_grid.selectCell(0, 1);
        stms_task_grid.editCell();
        setTimeout( // weird workaround to focus cell (honestly have no clue why it doesn't work without the timeout)
            function() {
                $("div#stms_tasks_grid tr.rowselected td.cellselected").trigger("click");
            },
            0
        );
    });

    stms_task_grid.attachEvent("onCheck", function(rID, cInd, state){
        stms_task_grid.selectRowById(rID);
        if(state){
            stms_task_grid.cells(rID, 1).setDisabled(true);
            stms_task_grid.cells(rID, 2).setDisabled(true);
            $("div#stms_tasks_grid tr.rowselected").addClass("stms_task_disabled");
            setTimeout(function(){ // give user 2 seconds to uncheck task before it disappears
                    if(stms_task_grid.cells(rID, 0).isChecked()){
                        stms_task_grid.deleteRow(rID);
                    }
                },
                2000
            )
        }else{
            stms_task_grid.cells(rID, 1).setDisabled(false);
            stms_task_grid.cells(rID, 2).setDisabled(false);
            $("div#stms_tasks_grid tr.rowselected").removeClass("stms_task_disabled");
        }
    });

    stms_task_grid.attachEvent("onAfterRowDeleted", function(id, pid){
       if(stms_task_grid.getRowsNum() <= 0){
           $("div#stms_tasks_grid").addClass("gridbox_empty");
           $("div#stms_tasks_none").addClass("gridbox_empty");
       }
    });

    var data = {
        rows:[
            { id:1, data: ["0", "Hand in plagiarism form", "27/08/2018 at 12:00"]},
            { id:2, data: ["0", "Pay for lab coat", "29/08/2018 at 12:00"]},
            { id:3, data: ["0", "Set up study timetable", "02/09/2018 at 12:00"]}
        ]
    };
    stms_task_grid.parse(data,"json"); // populate task list

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

    ////////////////////
    // SEMESTERS PAGE //
    ////////////////////
    // PAGE LAYOUT //
    var stms_semester_layout = stms_sidebar.cells("p3_semesters").attachLayout({

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
                width:      350
            },
            {
                id:         "b"
            }
        ]

    });

    stms_semester_layout.cells("a").setMinWidth(350);
    stms_semester_layout.cells("a").attachObject("stms_semesters_left");

    // Pop-up for when user clicks create button on the semesters page (choose new semester or new course)
    stms_semester_popup = new dhtmlXPopup();
    stms_semester_popup.attachList("option", [
        {id: 1, option: "New Semester"},
        {id: 2, option: "New Course"}
    ]);
    stms_semester_popup.attachEvent("onClick", function(id){
        $("table#stms_semesters_list tr").removeClass("selected");
        if(id == 1){
            prepareSemesterForm(null);
        }else{
            prepareCourseForm(null);
        }
    });
    $("#stms_semester_create_button").click(function(){
        if(stms_semester_popup.isVisible()){
            stms_semester_popup.hide();
        }else {
            var stms_semester_area = document.getElementById("stms_semester_create_button");
            var x = window.dhx.absLeft(stms_semester_area);
            var y = window.dhx.absTop(stms_semester_area)-6;
            var width = stms_semester_area.offsetWidth;
            var height = stms_semester_area.offsetHeight;
            stms_semester_popup.show(x, y, width, height);
        }
    });

    stms_semester_layout.cells("b").setMinWidth(550);
    stms_semester_layout.cells("b").attachObject("stms_semesters_right");

    dhtmlx.message.position="bottom"; // settings for messages indicating user's form submission was saved

    stms_semester_form = new dhtmlXForm("stms_semester_dhx_form", [
        {type: "settings", position: "label-left", labelWidth: "100", inputWidth: "300"},
        {type: "hidden", name: "semester_id"},
        {type: "block", width: 403, offsetTop: 15, blockOffset:0, list: [
            {type: "input", name: "semester_name", label: "Name:", required: true},
            {type: "calendar", name: "semester_start", label: "Start Date:", required: true},
            {type: "calendar", name: "semester_end", label: "End Date:", required: true}
        ]},
        {type: "block", width: 403, offsetTop: 15, blockOffset: 0, list: [
            {type: "button", name: "submit", value: "Save", className: "stms_save_semester_button", width: 197},
            {type: "newcolumn"},
            {type: "button", name: "cancel", value: "Cancel", className: "stms_cancel_semester_button", width: 197},
            {type: "button", name: "delete", value: "Delete", className: "stms_delete_semester_button", width: 197}
        ]}
    ]);

    stms_semester_form.attachEvent("onAfterValidate", function(status){
        // if the form passes validation, display a loading screen while we wait for the AJAX response
        if(status){
            $("body").loadingModal("destroy");
            $("body").loadingModal({
                position: "auto",
                color: "#fff",
                opacity: "0.7",
                backgroundColor: "rgb(0,0,0)",
                animation: "doubleBounce"
            });
            $("body").loadingModal("show");
        }
    });

    stms_semester_form.attachEvent("onButtonClick", function(name){
        if(name == "submit"){
            submitSemesterForm(false);
        }else if(name == "cancel"){
            var first_course = $("table#stms_semesters_list tr.stms_course_row:first");
            if(first_course.length) {
                first_course.click(); // if there is at least one course, select it
            }else{
                $("table#stms_semesters_list tr.stms_semester_row:first").click(); // otherwise if there is at least one semester, select it
            }
        }else if(name == "delete"){
            swal({
                title: "Are you sure?",
                text: "Deleting this semester will result in all of the associated courses being deleted too.\n\nYou cannot undo this action.",
                icon: "warning",
                buttons: ["Cancel", "Delete"],
                dangerMode: true,
            })
            .then(function(willDelete){
                if(willDelete){
                    submitSemesterForm(true);
                }
            });
        }
    });

    stms_course_form = new dhtmlXForm("stms_course_dhx_form", [
        {type: "settings", position: "label-left", labelWidth: "100", inputWidth: "300"},
        {type: "hidden", name: "course_id"},
        {type: "block", width: 403, offsetTop: 15, blockOffset:0, list: [
            {type: "input", name: "course_name", label: "Name:", required: true},
            {type: "input", name: "course_code", label: "Code:", maxLength: 10, required: false},
            {type: "combo", name: "course_semester_1", label: "Semester:", readonly: true, required: true},
            {type: "combo", name: "course_colour", label: "Colour:", comboType: "image", readonly: true, required: true, options: [
                {value: "red", text: "Red", img_src: "../media/colours/red.png"},
                {value: "orange", text: "Orange", img_src: "../media/colours/orange.png"},
                {value: "yellow", text: "Yellow", img_src: "../media/colours/yellow.png"},
                {value: "green", text: "Green", img_src: "../media/colours/green.png"},
                {value: "blue", text: "Blue", img_src: "../media/colours/blue.png"},
                {value: "purple", text: "Purple", img_src: "../media/colours/purple.png"}
            ]}
        ]},
        {type: "block", width: 403, offsetTop: 15, blockOffset: 0, list: [
            {type: "button", name: "submit", value: "Save", className: "stms_save_course_button", width: 197},
            {type: "newcolumn"},
            {type: "button", name: "cancel", value: "Cancel", className: "stms_cancel_course_button", width: 197},
            {type: "button", name: "delete", value: "Delete", className: "stms_delete_course_button", width: 197}
        ]}
    ]);

    stms_course_form.attachEvent("onAfterValidate", function(status){
        // if the form passes validation, display a loading screen while we wait for the AJAX response
        if(status){
            $("body").loadingModal("destroy");
            $("body").loadingModal({
                position: "auto",
                color: "#fff",
                opacity: "0.7",
                backgroundColor: "rgb(0,0,0)",
                animation: "doubleBounce"
            });
            $("body").loadingModal("show");
        }
    });

    stms_course_form.attachEvent("onButtonClick", function(name){
        if(name == "submit"){
            submitCourseForm(false);
        }else if(name == "cancel"){
            var first_course = $("table#stms_semesters_list tr.stms_course_row:first");
            if(first_course.length) {
                first_course.click(); // if there is at least one course, select it
            }else{
                $("table#stms_semesters_list tr.stms_semester_row:first").click(); // otherwise if there is at least one semester, select it
            }
        }else if(name == "delete"){
            swal({
                title: "Are you sure?",
                text: "Deleting this course will result in all of the associated sessions and assignments being deleted too.\n\nYou cannot undo this action.",
                icon: "warning",
                buttons: ["Cancel", "Delete"],
                dangerMode: true,
            })
            .then(function(willDelete){
                if(willDelete){
                    submitCourseForm(true);
                }
            });
        }
    });

    // preload semester list table (will reload whenever user switches to this tab - handled in onSelect event for sidebar)
    //prepareSemesterForm(null);
    loadSemesters(function(){
        if(COURSE_COUNT == 0){
            stms_sidebar.cells("p3_semesters").setActive();
        }
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

function loadEvents(){
    $.ajax({
        url: "./ajax/connect_scheduler.jsp"
    }).done(function(data) {
        scheduler.clearAll();
        scheduler.parse(JSON.parse(data), "json");
    });
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

function taskListResize(){
    var newWidth = $("div#stms_tasks").width()-22;
    var newHeight = $("div#stms_tasks").height()-$("div#stms_task_suggestion_wrapper").height()-180;
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

function compareSuggestion(a,b) {
    var aP = parseFloat(a.priority);
    var bP = parseFloat(b.priority);
    if (aP < bP)
        return 1;
    if (aP > bP)
        return -1;
    return 0;
}

function loadSuggestions(){
    $.ajax({
        url: "./ajax/connect_task_suggestion.jsp"
    }).done(function(data){
        $("table#stms_task_suggestion_list tbody").empty(); // clear existing items in suggestion list (need to refresh)
        var suggestions = JSON.parse(data);
        suggestions.sort(compareSuggestion); // sort based on priority (high to low)
        if(suggestions.length < 1){
            $("div#stms_task_suggestion_wrapper").hide(); // if there are no suggestion, then hide the section
        }else {
            $("div#stms_task_suggestion_wrapper").show();
        }
        for(var i = 0 ; i < suggestions.length ; i++){
            var priority = suggestions[i].priority;
            var relativeDate = moment(suggestions[i].dueDate).fromNow();
            var html = "";
            if(priority >= 0.65){ // high priority
                html = "<tr><td ";
                if(suggestions[i].weighting != null){
                    html += "rowspan='2' ";
                }
                html += "class='stms_task_suggestion_priority'><div class='stms_priority_icon stms_priority_icon_high'>!!!</div></td><td ";
                if(suggestions[i].weighting != null){
                    html += "style='border-bottom: 0' ";
                }
                html += "class='stms_task_suggestion_text'><span class='stms_priority_text stms_priority_text_high'>" + suggestions[i].action + " " + suggestions[i].courseName + " " + suggestions[i].type + "</span></td><td ";
                if(suggestions[i].weighting != null){
                    html += "style='border-bottom: 0' ";
                }
                html += "class='stms_task_suggestion_date'>" + relativeDate + "</td></tr>";
                if(suggestions[i].weighting != null){
                    html += "<tr><td style='color: red; font-size: 13px'>Weighting: " + suggestions[i].weighting + "%</td></tr>";
                }
            }else if(priority >= 0.45){ // medium priority
                html =
                    "<tr>" +
                    "<td class='stms_task_suggestion_priority'><div class='stms_priority_icon stms_priority_icon_medium'>!!</div></td>" +
                    "<td class='stms_task_suggestion_text'><span class='stms_priority_text stms_priority_text_medium'>" + suggestions[i].action + " " + suggestions[i].courseName + " " + suggestions[i].type + "</span></td>" +
                    "<td class='stms_task_suggestion_date'>" + relativeDate + "</td>";
                "</tr>";
            }else{ // low priority
                html =
                    "<tr>" +
                    "<td class='stms_task_suggestion_priority'><div class='stms_priority_icon stms_priority_icon_low'>!</div></td>" +
                    "<td class='stms_task_suggestion_text'><span class='stms_priority_text stms_priority_text_low'>" + suggestions[i].action + " " + suggestions[i].courseName + " " + suggestions[i].type + "</span></td>" +
                    "<td class='stms_task_suggestion_date'>" + relativeDate + "</td>";
                "</tr>";
            }
            $("table#stms_task_suggestion_list tbody").append(html);
            taskListResize();
        }
    });
}

function compareSemester(a,b) {
    var aD = a.end;
    var bD = b.end;
    if (moment(aD).isBefore(bD))
        return 1;
    if (moment(aD).isAfter(bD))
        return -1;
    return 0;
}

function compareCourse(a,b) {
    var aD;
    if(a.code == null){
        aD = a.name;
    }else{
        aD = a.code + " - " + a.name;
    }
    var bD;
    if(b.code == null){
        bD = b.name;
    }else{
        bD = b.code + " - " + b.name;
    }
    if (aD > bD)
        return 1;
    if (aD < bD)
        return -1;
    return 0;
}

function loadSemesters(callback){
    $.ajax({
        url: "./ajax/connect_semesters.jsp"
    }).done(function(data){
        $("table#stms_semesters_list tbody").empty(); // clear existing items in semester list (need to refresh)
        var semesters = JSON.parse(data);
        semesters.sort(compareSemester); // sort based on priority (high to low)
        SEMESTER_COUNT = semesters.length;
        if(semesters.length == 0){
            $("table#stms_semesters_list").hide(); // if there are no semesters, then hide the table
            $("div#stms_semesters_none").show();
        }else{
            $("table#stms_semesters_list").show();
            $("div#stms_semesters_none").hide();
        }
        var semester_combo = stms_course_form.getCombo("course_semester_1");
        semester_combo.clearAll();
        var total_courses = 0;
        for(var i = 0 ; i < semesters.length ; i++){
            var semester_id = semesters[i].id;
            var semester_name = semesters[i].name;
            semester_combo.addOption(semester_id, semester_name + " (" + moment(semesters[i].end).format("YYYY") + ")");
            var date_range = moment(semesters[i].start).format("D MMM YYYY") + " - " + moment(semesters[i].end).format("D MMM YYYY");
            var html = "";
            html += "<tr class='stms_semester_row' data-semester-id='" + semester_id + "' data-semester-name='" + semester_name + "' data-semester-start='" + moment(semesters[i].start).format("YYYY-MM-DD") + "' data-semester-end='" + moment(semesters[i].end).format("YYYY-MM-DD") + "'>" +
                        "<td><span class='stms_semester_name'>" + semester_name + "</span><span class='stms_semester_date'>" + date_range + "</span></td>" +
                    "</tr>";
            var courses = semesters[i].courses;
            courses.sort(compareCourse);
            for(var j = 0 ; j < courses.length ; j++){
                var course_id = courses[j].id;
                var course_code = (courses[j].code == null ? "" : courses[j].code);
                var course_desc;
                if(courses[j].code == null){
                    course_desc = courses[j].name;
                }else{
                    course_desc = courses[j].code + " - " + courses[j].name;
                }
                html += "<tr class='stms_course_row' data-course-id='" + course_id + "' data-course-semester-1='" + courses[j].semester_id_1 + "' data-course-name='" + courses[j].name + "' data-course-code='" + course_code + "' data-course-colour='" + courses[j].colour + "'>" +
                            "<td><span class='colour_"+ courses[j].colour + "'>" + course_desc + "</span></td>" +
                        "</tr>";
                total_courses++;
            }
            $("table#stms_semesters_list tbody").append(html);
        }
        COURSE_COUNT = total_courses;
        semester_combo.selectOption(0);
        // add click event handlers to prepare the right-hand form when a row is clicked
        $("table#stms_semesters_list tr.stms_semester_row").off("click");
        $("table#stms_semesters_list tr.stms_semester_row").on("click", function(event){
            var target = $(this);
            $("table#stms_semesters_list tr").removeClass("selected");
            target.addClass("selected");
            prepareSemesterForm(target.attr("data-semester-id"));
        });
        $("table#stms_semesters_list tr.stms_course_row").off("click");
        $("table#stms_semesters_list tr.stms_course_row").on("click", function(event){
            var target = $(this);
            $("table#stms_semesters_list tr").removeClass("selected");
            target.addClass("selected");
            prepareCourseForm(target.attr("data-course-id"));
        });
        if(semesters.length > 0 && total_courses > 0) {
            $("table#stms_semesters_list tr.stms_course_row:first").click();
            stms_semester_popup.clear();
            stms_semester_popup.attachList("option", [
                {id: 1, option: "New Semester"},
                {id: 2, option: "New Course"}
            ]);
        }else if(semesters.length > 0){
            prepareCourseForm(null);
            stms_semester_popup.clear();
            stms_semester_popup.attachList("option", [
                {id: 1, option: "New Semester"},
                {id: 2, option: "New Course"}
            ]);
        }else{
            prepareSemesterForm(null);
            stms_semester_popup.clear();
            stms_semester_popup.attachList("option", [
                {id: 1, option: "New Semester"}
            ]);
        }
        if(callback && typeof callback === "function"){
            callback();
        }
    });
}

function prepareSemesterForm(semester_id){
    $("div#stms_course_form_wrapper").hide();
    $("div#stms_semester_form_wrapper").show();
    stms_semester_form.setItemValue("semester_id", semester_id);
    if(semester_id == null){
        $("div#stms_semester_form_wrapper h2").text("New Semester");
        $("div#stms_semester_form_wrapper div.stms_cancel_semester_button").show();
        $("div#stms_semester_form_wrapper div.stms_delete_semester_button").hide();
        stms_semester_form.clear();
    }else{
        $("div#stms_semester_form_wrapper h2").text("Edit Semester");
        $("div#stms_semester_form_wrapper div.stms_cancel_semester_button").hide();
        $("div#stms_semester_form_wrapper div.stms_delete_semester_button").show();
        var semester_row = $("table#stms_semesters_list tr.stms_semester_row[data-semester-id=" + semester_id + "]");
        stms_semester_form.setItemValue("semester_name", semester_row.attr("data-semester-name"));
        stms_semester_form.setItemValue("semester_start", semester_row.attr("data-semester-start"));
        stms_semester_form.setItemValue("semester_end", semester_row.attr("data-semester-end"));
    }
}

function submitSemesterForm(deleted){
    var action = "updated";
    if(stms_semester_form.getItemValue("semester_id") == null || stms_semester_form.getItemValue("semester_id").length == 0){
        action = "inserted";
    }
    if(deleted){
        action = "deleted";
    }
    stms_semester_form.send("./ajax/connect_semesters.jsp?action=" + action, "post", function(loader, response){
        // display error message if request failed, otherwise refresh semester page
        var data = JSON.parse(response);
        if(data.action == "inserted" || data.action == "updated"){
            loadSemesters(function(){
                $("body").loadingModal("hide");
                dhtmlx.message({
                    type: "save-message",
                    text: "Your semester has been successfully saved!"
                });
                selectSemester(data.tid);
            });
        }else if(data.action == "deleted"){
            loadSemesters(function(){
                $("body").loadingModal("hide");
                dhtmlx.message({
                    type: "delete-message",
                    text: "Your semester has been successfully deleted!"
                });
            });
        }else{
            $("body").loadingModal("hide");
            swal({
                icon: "error",
                title: "Server Error",
                text: "Please try again later."
            });
        }
    });
}

function prepareCourseForm(course_id){
    $("div#stms_course_form_wrapper").show();
    $("div#stms_semester_form_wrapper").hide();
    stms_course_form.setItemValue("course_id", course_id);
    if(course_id == null){
        $("div#stms_course_form_wrapper h2").text("New Course");
        $("div#stms_course_form_wrapper div.stms_cancel_course_button").show();
        $("div#stms_course_form_wrapper div.stms_delete_course_button").hide();
        stms_course_form.clear();
    }else{
        $("div#stms_course_form_wrapper h2").text("Edit Course");
        $("div#stms_course_form_wrapper div.stms_cancel_course_button").hide();
        $("div#stms_course_form_wrapper div.stms_delete_course_button").show();
        var course_row = $("table#stms_semesters_list tr.stms_course_row[data-course-id=" + course_id + "]");
        stms_course_form.setItemValue("course_semester_1", course_row.attr("data-course-semester-1"));
        stms_course_form.setItemValue("course_name", course_row.attr("data-course-name"));
        stms_course_form.setItemValue("course_code", course_row.attr("data-course-code"));
        stms_course_form.setItemValue("course_colour", course_row.attr("data-course-colour"));
    }
    // fix for a bug that suddenly appeared for no reason (thanks DHTMLX O_O)
    var semester_combo = $(stms_course_form.getCombo("course_semester_1").DOMelem);
    var colour_combo = $(stms_course_form.getCombo("course_colour").DOMelem);
    semester_combo.width(300);
    semester_combo.find("input").width(300);
    stms_course_form.getCombo("course_semester_1").setOptionWidth(298);
    colour_combo.width(300);
    colour_combo.find("input").width(300);
    stms_course_form.getCombo("course_colour").setOptionWidth(298);
}

function submitCourseForm(deleted){
    var action = "updated";
    var course_id = stms_course_form.getItemValue("course_id");
    if(course_id == null || course_id.length == 0){
        action = "inserted";
    }
    if(deleted){
        action = "deleted";
    }
    stms_course_form.send("./ajax/connect_semesters.jsp?action=" + action, "post", function(loader, response){
        // display error message if request failed, otherwise refresh semester page
        var data = JSON.parse(response);
        if(data.action == "inserted" || data.action == "updated"){
            loadSemesters(function(){
                $("body").loadingModal("hide");
                dhtmlx.message({
                    type: "save-message",
                    text: "Your course has been successfully saved!"
                });
                selectCourse(data.tid);
            });
        }else if(data.action == "deleted"){
            loadSemesters(function(){
                $("body").loadingModal("hide");
                dhtmlx.message({
                    type: "delete-message",
                    text: "Your course has been successfully deleted!"
                });
            });
        }else{
            $("body").loadingModal("hide");
            swal({
                icon: "error",
                title: "Server Error",
                text: "Please try again later."
            });
        }
    });
}

function selectSemester(id){
    $("table#stms_semesters_list tr.stms_semester_row[data-semester-id=" + id + "]").click();
}

function selectCourse(id){
    $("table#stms_semesters_list tr.stms_course_row[data-course-id=" + id + "]").click();
}