$(document).ready(function () {

    userLogin = JSON.parse(localStorage.getItem('login'));

    if (userLogin['type']== "medic"){
        $('#cstatus').text("Dashboard");
        $('#cstatus').attr('href','medic_fp.html');
    }
    else{
        $('#cstatus').text("Clinic Status");
        $('#cstatus').attr('href','utente.html');
    }
    // Add content to HTML
    $("#userName").text(userLogin['email']);
    $("#userId").text(userLogin['username']);
    $("#userEmail").text(userLogin['name']);
    
    // Functionality not implemented yet
    $(".notImplemented").click(function () {
        alert("Sorry, but this functionality has not been implemented yet! :(");
    });

});