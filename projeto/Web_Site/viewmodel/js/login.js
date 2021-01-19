$(document).ready(function () {
    // If user is logged in already, redirect to app

    if(document.cookie!=''){
        cookie_array= document.cookie.split("&");
        console.log(document.cookie)
        var temp = cookie_array[1].trim();
        user_rule = temp.substring("role=".length,temp.length);
        if (user_rule == 'Patient'){
            window.location.replace('utente.html');
            return; 
        }
        if (user_rule == 'Professional'){
            window.location.replace('medic_fp.html');
            return; 
        }  
    }
    // Login
    $("#loginButton").click(function(){
        // Validate user data
        if ($("#loginEmail").val().trim()=="" || $("#loginPassword").val().trim()=="") {
            $("#loginError").text("Please enter your data to sign in.");
            $("#loginError").fadeIn();
            return;
        }
        $.ajax({
            url: "http://192.168.160.217:8080/api/login?username=" + $("#loginEmail").val().trim() + "&password=" + $("#loginPassword").val().trim(),
            headers:{"Access-Control-Allow-Origin":"http://192.168.160.217"},
            statusCode: {
                500: function(xhr){
                    $("#loginError").text("There was an error connecting to the server, please try again!");
                    $("#loginError").fadeIn();
                    return;
                },
                403: function(xhr){
                    $("#loginError").text("Invalid Credentials!");
                    $("#loginError").fadeIn();
                    return;
                }
            }
        }).then(function(user) {
            $("#loginError").removeClass("d-none");
            $("#loginError").hide();
            jwt_value = user['token'];
            role_value = user['role'];
            id = user['id'];
            document.cookie='access_token='+jwt_value+'&'+ 'role='+role_value+'&'+'id='+id+';';
            console.log(document.cookie)
            if (user['role']=="Patient"){
                window.location.replace("utente.html");
            }
            else{
                window.location.replace("medic_fp.html");
            }
        });
    });

});


function submit(user,registered){
    if (registered) {
        if (document.cookie.includes("Patient")){
            window.location.replace("utente.html");
        }
        else{
            window.location.replace("medic_fp.html");
        }
        
    } else {
        $("#loginError").text("The credentials inserted are invalid!");
        $("#loginError").fadeIn();
    }

}
