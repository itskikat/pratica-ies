function switchSignupAssistente() {

    document.getElementById("left-form").style.display = "block";
    document.getElementById("left-cover").style.display = "none";
    document.getElementById("right-form").style.display = "none";
    document.getElementById("right-cover").style.display = "block"; 
}

function switchSignupPaciente() {
    document.getElementById("right-form").style.display = "block";
    document.getElementById("right-cover").style.display = "none";
    document.getElementById("left-form").style.display = "none";
    document.getElementById("left-cover").style.display = "block";

}
//Patient Register
$("#registerPatient").click(function(){
    $("#registerErrorPat").removeClass("d-none");
    $("#registerErrorPat").hide();
    var data_post={}
    
    var name = $("#registerName").val();
    var username = $("#registerUsername").val();
    var email = $("#registerEmail").val();
    var age = $("#registerAge").val();
    var gender = $("#registerGender").val();
    var weight = $("#registerWeight").val();
    var height = $("#registerHeight").val();
    var pass1 = $("#registerPass").val();
    var pass2 = $("#registerPass2").val();
    var professional = $("#registerProf").val(); 

    if(name == "" || email == "" || age == ""|| username==""){
        $("#registerErrorPat").text("Fill all the fields!");
        $("#registerErrorPat").fadeIn();
        return;
    }

    if(pass1 != pass2){
        $("#registerErrorPat").text("The passwords don't match!");
        $("#registerErrorPat").fadeIn();
        return;
    }

    data_post["fullname"]=name;
    data_post["username"]=username;
    data_post["email"]=email;
    data_post["age"]=age;
    data_post["password"]=pass1;
    data_post["gender"]=gender;
    data_post["weight"]=parseFloat(weight);
    data_post["height"]=parseFloat(height);
    data_post["lastcheck"]= new Date();
    
    var data ={};
    data["pemail"]= professional;
    data["patient"]= data_post;

    
    $.ajax({
        type:"POST",
        url: "http://192.168.160.217:8080/api/patients",
        headers:{"Access-Control-Allow-Origin":"http://192.168.160.217"},
        data: JSON.stringify(data),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        statusCode: {
            500: function(xhr){
                console.log(xhr);
                $("#registerErrorPat").text("Username already exists!");
                $("#registerErrorPat").fadeIn();
                return;
            },
            404: function(xhr){
                $("#registerErrorPat").text("Professional email does not exist");
                $("#registerErrorPat").fadeIn();
                return;
            },
            422: function(xhr){
                var err = JSON.parse(xhr.responseText);
                $("#loginError").text(err.Message);
                $("#loginError").fadeIn();
                return;
            }
        }

    }).then(function(user) {
        alert("Registed Successfully!");
            window.location.replace("index.html");
    });

  });

$("#registerProfessional").click(function(){
    $("#registerErrorPro").removeClass("d-none");
    $("#registerErrorPro").hide();
    var data_post={}
    
    var name = $("#regName").val();
    var username = $("#regUsername").val();
    var email = $("#regEmail").val();
    var age = $("#regAge").val();
    var gender = $("#regGender").val();
    var pass1 = $("#regPass").val();
    var pass2 = $("#regPass2").val();
    var workplace = $("#regWorkplace").val();
    var speciality = $("#regSpeciality").val();
    
    data_post["fullname"]=name;
    data_post["username"]=username;
    data_post["email"]=email;
    data_post["age"]=age;
    data_post["password"]=pass1;
    data_post["gender"]=gender;
    data_post["workplace"]=workplace;
    data_post["speciality"]=speciality;

    if(name == "" || email == "" || age == "" || workplace == "" || speciality == "" || pass1 == "" || username==""){
        $("#registerErrorPro").text("Fill all the fields!");
        $("#registerErrorPro").fadeIn();
        return;
    }

    if(pass1 != pass2){
        $("#registerErrorPro").text("The passwords don't match!");
        $("#registerErrorPro").fadeIn();
        return;
    }

    $.ajax({
        type:"POST",
        url: "http://192.168.160.217:8080/api/professionals",
        headers:{"Access-Control-Allow-Origin":"http://192.168.160.217"},
        data: JSON.stringify(data_post),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        statusCode: {
            500: function(xhr){
                console.log(xhr);
                $("#registerErrorPat").text("Username already exists!");
                $("#registerErrorPat").fadeIn();
                return;
            },
            404: function(xhr){
                $("#registerErrorPat").text("Professional email does not exist");
                $("#registerErrorPat").fadeIn();
                return;
            },
            422: function(xhr){
                var err = JSON.parse(xhr.responseText);
                $("#loginError").text(err.Message);
                $("#loginError").fadeIn();
                return;
            }
        }

    }).then(function(user) {
        alert("Registed Successfully!");
            window.location.replace("index.html");
    });       
        
  });
