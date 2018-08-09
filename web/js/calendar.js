// Set-up the page layout when the full HTML document has been downloaded
dhtmlxEvent(window, 'load', function(){

    // Initiate sidebar
    var sidebar = new dhtmlXSideBar({

        parent:         document.body,
        skin:           "material",
        template:       "icons_text",
        icons_path:     "media/icons/",
        width:          100,
        header:         false,
        autohide:       false,
        items: [
            {
                id:         "p1_calendar",
                text:       "Calendar",
                icon:       "calendar_icon.png",
                selected:   true
            }
        ]

    });

    sidebar.cells("p1_calendar").attachScheduler(new Date(), 'month', 'stms_scheduler', scheduler);

    // Initiate scheduler
    //scheduler.init('stms_scheduler', new Date(), 'month');
});