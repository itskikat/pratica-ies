var jwt;
var id;

$(document).ready(function () {
    cookie_array= document.cookie.split("&");
    var temp = cookie_array[0].trim();
    jwt = temp.substring("access_token=".length,temp.length);
    var temp = cookie_array[2].trim();
    id = temp.substring("id=".length,temp.length);

    $("#logOut").click(function(){
        document.cookie='access_token= & role= & id= ;';
        window.location.replace('index.html'); 
    })

    $.ajax({
        url: "http://192.168.160.217:8080/api/patients/"+ id,
        headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
        statusCode: {
            500: function(xhr){
                return;
            },
            403: function(xhr){
                return;
            }
        }
    }).then(function(user) {
        console.log(user)
        $("#changeUser").fadeOut();
        $("#changeEma").fadeOut();
        $("#changePass").fadeOut();

        // Add content to HTML
        $("#userFullName").text(user['fullname']);
        $("#userId").text(user['username']);
        $("#userEmail").text(user['email']);
        $("#profilePic").attr("src",'data:image/gif;base64,'+ user['image']);


        $("#changeUsername").click(function(){
            $("#changeUser").fadeToggle("slow");
        })
        $("#changeEmail").click(function(){
            $("#changeEma").fadeToggle("slow");
        })
        $("#changePassword").click(function(){
            $("#changePass").fadeToggle("slow");
        })

        $("#saveSettings").click(function(){
            var username = $("#usernameSet").val();
            var email = $("#emailSet").val();
            var pass1 = $("#passSet").val();
            var pass2 = $("#passSet2").val();
            console.log(username)
            console.log(email)
            console.log(pass1)
            console.log(pass2)

            user['password'] = null;

            if(pass1 != pass2){
                $("#setError").text("The passwords don't match!");
                $("#setError").fadeIn();
                return;
            }
            if(username != ''){
                user['username'] = username;
            }
            if(email != ''){
                user['email'] = email;
            }
            if(pass1 != ''){
                user['password'] = pass1;
            }

            editPatient(user)
            document.cookie='access_token= & role= & id= ;';
            window.location.replace('index.html'); 
        })
    })
});

//PUT Patient
function editPatient(data){
    $.ajax({
        type: "PUT",
        url: "http://192.168.160.217:8080/api/patients/"+id,
        headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json; charset=utf-8",

        statusCode: {
            500: function(xhr){
                console.log(xhr);
                alert("Username already exists!");
                return;
            },
            404: function(xhr){
                alert("Professional email does not exist");
                return;
            },
            422: function(xhr){
                alert("Error");
                return;
            }
        }
      }).then(function(data){
        alert("Saved successfully! Please login again");
        
    });
}

function changePhoto(){
        var data = new FormData();
        
        
        var fileInput = document.getElementById('fileSet');
        var file = fileInput.files[0];
        data.append("file", file);
        console.log(data.entries());

        $.ajax({
            type: 'POST',
            url: "http://192.168.160.217:8080/api/patients/" + id + "/picture",
            headers:{"Access-Control-Allow-Origin":"http://192.168.160.217","Authorization":"Bearer "+ jwt},
            data: data,
            cache: false,
            contentType: false,
            //contentType: "multipart/form-data",
            processData: false,
            statusCode: {
                500: function(xhr){
                    console.log(xhr);
                    alert("Error");
                    return;
                },
                404: function(xhr){
                    alert("Error");
                    return;
                },
                422: function(xhr){
                    alert("Error");
                    return;
                }
            }
            
        }).then(function(data){
            alert("Saved successfully! Please login again");
            window.location.replace('settings.html'); 
            
        });
}