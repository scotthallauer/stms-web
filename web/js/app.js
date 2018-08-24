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
    scheduler.config.scroll_hour = 5;
    scheduler.config.include_end_by = true;
    scheduler.config.edit_on_create = true;
    scheduler.config.details_on_create = true;
    scheduler.config.details_on_dblclick = true;
    scheduler.config.time_step = 15;
    scheduler.locale.labels.section_courses = "Course";
    scheduler.locale.labels.section_types = "Type";

    scheduler.config.lightbox.sections = [
        {name: "courses", height: 30, type: "combo", map_to: "course_id", filtering: false, script_path: "./ajax/connect_course_combo.jsp"},
        {name: "types", height: 30, type: "combo", map_to: "event_type", filtering: false, script_path: "./ajax/connect_type_combo.jsp"},
        {name: "recurring", height: 115, type: "recurring", map_to: "rec_type", button: "recurring"},
        {name: "time", height: 72, type: "time", map_to: "auto"}
    ];

    stms_sidebar.cells("p1_calendar").attachScheduler(new Date(), 'week');

    // Add a create button to the top right of the calendar
    $("div.dhx_cal_navline").append("<div class='dhx_cal_create_button' aria-label='Create' role='button'>Create</div>");
    $("div.dhx_cal_create_button").click(function () {
        scheduler.addEventNow({
            text: "New Session",
            start_date: moment().startOf("hour").toDate(),
            end_date: moment().startOf("hour").add(1, "hour").toDate()
        });
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

    scheduler.attachEvent("onBeforeDrag", function(id){
        var event = scheduler.getEvent(id);
        if(event != null && event.readonly){
            return false;
        }else{
            return true;
        }
    });

    // This method does two things:
    // (1) Updates the client-side version of the calendar event with the right details when the user saves it
    // (2) Enables custom session types - if the course session "type" is null, then the user has entered a custom value
    //     this method fetches that value and correctly assigns it as the combo box value so that it gets submitted with the form
    //     ...it's a bit of a workaround because DHTMLX does not handle this like it's supposed to :(
    scheduler.attachEvent("onEventSave", function(id, ev, is_new){
        var type = $("div.dhx_cal_light div.dhx_wrap_section:nth-of-type(2) input[name=types]").val().toLowerCase();
            type = type.charAt(0).toUpperCase() + type.substr(1);
        var course_name = $("div.dhx_cal_light div.dhx_wrap_section:nth-of-type(1) input[type=text]").val();
        course_name = course_name.substr(course_name.indexOf("-")+1);
        ev.text = course_name + " - " + type;
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
    //scheduler.load("./ajax/load_calendar.xml", "xml");

    /*
    $(".dhx_cal_container").loadingModal({
        position: "auto",
        color: "#fff",
        opacity: "0.7",
        backgroundColor: "rgb(0,0,0)",
        animation: "doubleBounce"
    });
    $(".dhx_cal_container").loadingModal("show");

    $.ajax({
        url: ""
    })
    */

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

    // when window resizes, set the two columns to 50% width
    stms_app_layout.attachEvent("onResizeFinish", function(){
        var totalWidth = 0;
        totalWidth = stms_task_layout.cells("a").getWidth();
        totalWidth += stms_task_layout.cells("b").getWidth();
        stms_task_layout.cells("a").setWidth(totalWidth/2);
        stms_task_layout.cells("b").setWidth(totalWidth/2);
    });

    stms_task_layout.cells("a").attachObject("stms_tasks");

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