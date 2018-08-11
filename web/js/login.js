// Set-up the page layout when the full HTML document has been downloaded
dhtmlxEvent(window, 'load', function(){

    var stms_login_form = new dhtmlXForm("stms_login_form", [
        {type: "settings", position: "label-left", labelWidth: 80, inputWidth: 260},
        {type: "input", name: "user_email", label: "Email:", required: true},
        {type: "password", name: "user_password", label: "Password:", required: true},
        {type: "button", name: "login_btn", value: "Log In", className: "stms_login_button", width: 100, offsetTop: 15}
    ]);

});