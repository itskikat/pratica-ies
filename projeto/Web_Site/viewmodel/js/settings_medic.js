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
        url: "http://192.168.160.217:8080/api/professionals/"+ id,
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

        $("#profilePic").attr("src",'data:image/gif;base64,'+ user['image']);
        console.log(user)
        $("#changeUserMed").fadeOut();
        $("#changeEmaMed").fadeOut();
        $("#changePassMed").fadeOut();
        $("#changeSpecMed").fadeOut();
        $("#changePlaceMed").fadeOut();
        // Add content to HTML
        $("#userFullName").text(user['fullname']);
        $("#userId").text(user['username']);
        $("#userEmail").text(user['email']);
        $("#userSpeciality").text(user['speciality']);
        $("#userWorkplace").text(user['workplace']);
        

        $("#changeUsernameMed").click(function(){
            $("#changeUserMed").fadeToggle("slow");
        })
        $("#changeEmailMed").click(function(){
            $("#changeEmaMed").fadeToggle("slow");
        })
        $("#changePasswordMed").click(function(){
            $("#changePassMed").fadeToggle("slow");
        })
        $("#changeWorkplaceMed").click(function(){
            $("#changePlaceMed").fadeToggle("slow");
        })
        $("#changeSpecialityMed").click(function(){
            $("#changeSpecMed").fadeToggle("slow");
        })

        $("#saveSettingsMed").click(function(){
            var username = $("#usernameSetMed").val();
            var email = $("#emailSetMed").val();
            var pass1 = $("#passSetMed").val();
            var pass2 = $("#passSetMed2").val();
            var speciality = $("#specialityMed").val();
            var workplace = $("#workplaceMed").val();
            console.log(username)
            console.log(email)
            console.log(pass1)
            console.log(pass2)
            
            user['password'] = null;

            if(pass1 != pass2){
                $("#setErrorMed").text("The passwords don't match!");
                $("#setErrorMed").fadeIn();
                return;
            }
            if(username != ''){
                user['username'] = username;
            }
            if(email != ''){
                user['email'] = email;
            }
            if(pass1 != ''){
                console.log("entrou");
                user['password'] = pass1;
            }
            if(speciality != ''){
                user['speciality'] = speciality;
            }
            if(workplace != ''){
                user['workplace'] = workplace;
            }

            editPro(user)
            document.cookie='access_token= & role= & id= ;';
            window.location.replace('index.html'); 
        })
    })
});

//PUT Patient
function editPro(data){
    console.log(data)
    $.ajax({
        type: "PUT",
        url: "http://192.168.160.217:8080/api/professionals/"+id,
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

function goToPatient(){
    localStorage.setItem('currentPatient', localStorage.getItem("mPatient"));
    window.location.reload();
}

function changePhoto(){
    var data = new FormData();
    
    
    var fileInput = document.getElementById('fileSet');
    var file = fileInput.files[0];
    data.append("file", file);
    console.log(data.entries());

    $.ajax({
        type: 'POST',
        url: "http://192.168.160.217:8080/api/professionals/" + id + "/picture",
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
        window.location.replace('settings_medic.html'); 
        
    });
}