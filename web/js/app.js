// Set-up the page layout when the full HTML document has been downloaded
dhtmlxEvent(window, 'load', function(){

    // Outer page layout
    var stms_layout = new dhtmlXLayoutObject({

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
    stms_layout.attachHeader("stms_header");

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
    var stms_sidebar = stms_layout.cells("a").attachSidebar({

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
                id:         "p2_assignments",
                text:       "Tasks",
                icon:       "assignment_icon.png"
            }

        ]

    });

    scheduler.config.xml_date = "%Y-%m-%d %H:%i:%s";
    scheduler.config.api_date = "%Y-%m-%d %H:%i:%s";
    scheduler.config.repeat_date = "%d/%m/%Y";
    scheduler.config.scroll_hour = 5;
    scheduler.config.include_end_by = true;
    scheduler.config.edit_on_create = true;
    scheduler.config.details_on_create = true;
    scheduler.config.details_on_dblclick = true;

    stms_sidebar.cells("p1_calendar").attachScheduler(new Date(), 'week');

    // Add a create button to the top right of the calendar
    $("div.dhx_cal_navline").append("<div class='dhx_cal_create_button' aria-label='Create' role='button'>Create</div>");
    $("div.dhx_cal_create_button").click(function () {
        scheduler.addEventNow({
            start_date: moment().startOf("hour").toDate(),
            end_date: moment().startOf("hour").add(1, "hour").toDate()
        });
    })

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

});