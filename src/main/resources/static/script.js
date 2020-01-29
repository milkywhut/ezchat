let stompClient = null;
let user;

function connect() {
    user = {
        login: $("#username").val(),
        password: $("#password").val()
    };
    let socket = new SockJS("/chat-messaging");//for stomp endpoint in web socket config
    stompClient = Stomp.over(socket);
    stompClient.connect(user.login, user.password, onConnect);
}

function onConnect(frame) {
    $("#authentication").css("display", "none");
    $("#chat_window").css("display", "block");
    $("#users").css("display", "block");
    stompClient.subscribe("/user/users", onGetAllActiveUsers);
    stompClient.send("/app/users", {}, user.login);
    stompClient.subscribe('/chat/messages', onGetMessage);
    stompClient.subscribe('/users/user', onUserConnectOrDisconnect);
}

function onGetAllActiveUsers(response) {
    let userLogins = JSON.parse(response.body);
    $("#users").empty();
    for (let i = 0; i < userLogins.length; i++) {
        $("#users").append("<div class='user'>" + userLogins[i] + "</div>");
    }
}

function onGetMessage(response) {
    let data = JSON.parse(response.body);
    draw("left", data);
}

function draw(side, message) {
    console.log("drawing...");
    let $message = $($('.message_template').clone().html());
    $message.addClass(side).find('.user').html(message.login);
    $message.addClass(side).find('.text').html(message.message);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);
}

function onUserConnectOrDisconnect(response) {
    let user = JSON.parse(response.body);
    if (user.type === 'CONNECTED') {
        console.log(user.login + 'connected');
        $("#users").append("<div class='user'>" + user.login + "</div>");
    } else if (user.type === 'DISCONNECTED') {
        console.log(user.login + ' disconnected');
        $(".user").filter(() => $(this).text() === user.login).remove();
    }
}

function sendMessage() {
    let text = $("#message_input_value").val();
    if (text !== null && text !== "") {
        stompClient.send("/app/message", {}, JSON.stringify({// /app - application destination
            'message': text,
            'login': user.login,
            'password': user.password
        }));
    }
}