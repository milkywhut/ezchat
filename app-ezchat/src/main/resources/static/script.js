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
    //
    stompClient.subscribe("/user/users", onGetAllActiveUsers); //in user controller
    stompClient.send("/app/users", {}, user.login);

    stompClient.subscribe("/chat/message", onGetMessage);
    stompClient.subscribe("/users/user", onUserConnectOrDisconnect); //in listener

    stompClient.subscribe("/user/chat/messages", onCurrentUserConnect);
    stompClient.send("/app/messages", {}, user.login);
}


function onGetAllActiveUsers(response) {
    let userLogins = JSON.parse(response.body);
    $("#users").empty();
    for (let i = 0; i < userLogins.length; i++) {
        $("#users").append("<div class='user_active'>" + userLogins[i] + "</div>");
    }
}

function onGetMessage(response) {
    let data = JSON.parse(response.body);
    draw("left", data);
}

function onUserConnectOrDisconnect(response) {
    let user = JSON.parse(response.body);
    if (user.type === 'CONNECTED') {
        console.log(user.login + 'connected');
        $("#users").append("<div class='user_active'>" + user.login + "</div>");
    } else if (user.type === 'DISCONNECTED') {
        console.log(user.login + ' disconnected');
        let $users = $(".user_active");
        let $disconnectedUser = $users.filter((i, element) => {
            return element.innerHTML === user.login;
        });
        $disconnectedUser.remove();
    }
}

function onCurrentUserConnect(response) {
    let messages = JSON.parse(response.body);
    for (let i = 0; i < messages.length; i++) {
        draw("left", messages[i]);
    }
}

function draw(side, message) {
    console.log("drawing...");
    let $message = $($('.message_template').clone().html());
    $message.addClass(side).find('.user').html(message.from);
    $message.addClass(side).find('.text').html(message.message);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);
}

function sendMessage() {
    let messageInputValue = document.getElementById("message_input_value");
    let text = messageInputValue.value;
    if (text !== null && text !== "") {
        stompClient.send("/app/message", {}, JSON.stringify({
            'message': text,
            'from': user.login
        }));
    }
    messageInputValue.value = "";
}