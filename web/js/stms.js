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

    stms_sidebar.cells("p1_calendar").attachScheduler(new Date(), 'month');
});