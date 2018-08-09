// Set-up the page layout when the full HTML document has been downloaded
dhtmlxEvent(window, 'load', function(){

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
                id:         "a",
                text:       "Scheduler"
            }
        ]

    });

    var stms_sidebar = stms_layout.cells("a").attachSidebar({

        parent:         document.body,
        skin:           "material",
        template:       "icons_text",
        icons_path:     "media/icons/",
        width:          90,
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
                text:       "Assignments",
                icon:       "assignment_icon.png"
            }

        ]

    });

    stms_sidebar.cells("p1_calendar").attachScheduler(new Date(), 'month');
});