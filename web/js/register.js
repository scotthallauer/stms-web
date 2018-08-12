dhtmlxEvent(window, 'load', function(){

    var stms_register_form = new dhtmlXForm("stms_register_form", [
        {type: "settings", position: "label-left", labelWidth: 135, inputWidth: 260},
        {type: "input", name: "user_name", label: "Name:", required: true},
        {type: "input", name: "user_email", label: "Email:", required: true, validate: "ValidEmail"},
        {type: "password", name: "user_password_1", label: "Password:", required: true},
        {type: "password", name: "user_password_2", label: "Confirm Password:", required: true},
        {type: "button", name: "register_btn", value: "Create Account", className: "stms_register_button", width: 150, offsetTop: 15}
    ]);

});