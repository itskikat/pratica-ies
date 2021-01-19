userLogin = JSON.parse(localStorage.getItem('login'));

if (localStorage.getItem('type')=="professional"){
    var stompClient = null;

    var socket = new SockJS('http://192.168.160.217:8080/chat');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {

        console.log('Connected: ' + frame);
        stompClient.subscribe('/healthstatus/messages', function(messageOutput) {   
            message= messageOutput.body.split(":");
            if (message[1]== userLogin["id"]){
                alert("hello");
            }
        });
    });
}
