dhtmlxEvent(window, 'load', function(){

    var stms_login_form = new dhtmlXForm("stms_login_dhx_form", [
        {type: "settings", position: "label-left", labelWidth: "80", inputWidth: "260"},
        {type: "input", name: "user_email", label: "Email:", validate: "ValidEmail", required: true},
        {type: "password", name: "user_password", label: "Password:", required: true},
        {type: "button", name: "submit", value: "Log In", className: "stms_login_button", width: 100, offsetTop: 15}
    ]);

    stms_login_form.attachEvent("onButtonClick", function(name){
        submit_form();
    });

    stms_login_form.attachEvent("onEnter", function(name){
        submit_form();
    });

    stms_login_form.attachEvent("onFocus", function(name){
        stms_login_form.setValidateCss(name, true);
    });

    stms_login_form.attachEvent("onBeforeValidate", function(id){
        stms_login_form.forEachItem(function(name){
            stms_login_form.setItemValue(name, $("[name=" + name + "]").val());
        })
    });

    stms_login_form.attachEvent("onAfterValidate", function(status){
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
        stms_login_form.send("./ajax/process_login.jsp", "post", function(loader, response){
            // when we get the AJAX response, hide the loading screen
            $("body").loadingModal("hide");
            // display error message if login failed, otherwise redirect user to app
            switch(parseInt(response)){
                // login successful
                case 0:
                    window.location = "/";
                    break;
                // login failed - account not activated
                case 1:
                    swal({
                        icon: "warning",
                        title: "Inactive Account",
                        text: "Please click the activation link we sent to your email."
                    });
                    break;
                // login failed - incorrect credentials
                case 2:
                    swal({
                        icon: "error",
                        title: "Invalid Login",
                        text: "Please try again."
                    });
                    break;
                // login failed - server error
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