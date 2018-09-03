dhtmlxEvent(window, 'load', function(){

    var stms_register_form = new dhtmlXForm("stms_register_dhx_form", [
        {type: "settings", position: "label-left", labelWidth: 135, inputWidth: 260},
        {type: "input", name: "user_fname", label: "First Name:", required: true},
        {type: "input", name: "user_lname", label: "Last Name:", required: true},
        {type: "input", name: "user_email", label: "Email:", required: true, validate: "ValidEmail"},
        {type: "password", name: "user_password_1", label: "Password:", required: true, validate: "ValidPassword"},
        {type: "password", name: "user_password_2", label: "Confirm Password:", required: true, validate: "ValidPassword"},
        {type: "button", name: "register_btn", value: "Create Account", className: "stms_register_button", width: 150, offsetTop: 15}
    ]);

    var stms_register_popup = new dhtmlXPopup({
        form: stms_register_form,
        id: ["user_password_1", "user_password_2"]
    });

    stms_register_popup.attachHTML("At least 8 characters");

    stms_register_form.attachEvent("onButtonClick", function(name){
        submit_form();
    });

    stms_register_form.attachEvent("onEnter", function(name){
        submit_form();
    });

    stms_register_form.attachEvent("onInputChange", function(name, value, form){
        if(name == "user_password_1" || name == "user_password_2"){
            if($("[name=" + name + "]").val().length >= 8){
                stms_register_popup.hide();
            }else{
                stms_register_popup.show(name);
            }
        }
    });

    stms_register_form.attachEvent("onFocus", function(name){
        stms_register_form.setValidateCss(name, true);
        if(name == "user_password_1" || name == "user_password_2") {
            if ($("[name=" + name + "]").val().length < 8) {
                stms_register_popup.show(name);
            }
        }
    });

    stms_register_form.attachEvent("onBlur", function(name){
        stms_register_popup.hide();
    });

    stms_register_form.attachEvent("onBeforeValidate", function(id){
        stms_register_form.forEachItem(function(name){
            stms_register_form.setItemValue(name, $("[name=" + name + "]").val());
        })
    });

    stms_register_form.attachEvent("onAfterValidate", function(status){
        // if the form passes validation, display a loading screen while we wait for the AJAX response
        if(status){
            $("body").loadingModal({
                position: "auto",
                color: "#fff",
                opacity: "0.7",
                backgroundColor: "rgb(0,0,0)",
                animation: "doubleBounce"
            });
            $("body").loadingModal("show");
        }
    })

    function submit_form(){
        stms_register_form.send("./ajax/process_register.jsp", "post", function(loader, response){
            // when we get the AJAX response, hide the loading screen
            $("body").loadingModal("hide");
            // display error message if registration failed, otherwise redirect user to app
            switch(parseInt(response)){
                // registration successful
                case 0:
                    swal({
                        icon: "success",
                        title: "Account Created",
                        text: "You can now log in to Monthly Mentor."
                    }).then(function(value){
                        window.location = "/login.jsp";
                    });
                    break;
                // registration failed - account with same email already exists
                case 1:
                    swal({
                        icon: "error",
                        title: "Account Exists",
                        text: "An account with this email already exists."
                    });
                    break;
                // registration failed - invalid account details
                case 2:
                    swal({
                        icon: "error",
                        title: "Invalid Registration",
                        text: "Please try again."
                    });
                    break;
                // registration failed - server error
                case 3:
                    swal({
                        icon: "error",
                        title: "Server Error",
                        text: "Please try again later."
                    });
                    break;
            }
        });
    }

});

function ValidPassword(data){
    if($("[name=user_password_1]").val().length < 8){
        return false;
    }
    if($("[name=user_password_1]").val() == $("[name=user_password_2]").val()){
        return true;
    }else{
        return false;
    }
}