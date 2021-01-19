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