let stompClient = null;
let user;

function connect() {
    user = {
        login: $("#login").val(),
        firstName: $("#firstName").val(),
        lastName: $("#lastName").val(),
        password: $("#password").val()
    };
    let socket = new SockJS("/chat-messaging");//for stomp endpoint in web socket config
    stompClient = Stomp.over(socket);
    let headers = {
        login: user.login,
        passcode: user.password,
        firstName: user.firstName,
        lastName: user.lastName
    }
    stompClient.connect(headers, onConnect);
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
    debugger;
    let users = JSON.parse(response.body);
    $("#users").empty();
    for (let i = 0; i < users.length; i++) {
        let user = users[i];
        let userMask = `${user.firstName} ${user.lastName}@${user.login}`;
        $("#users").append("<div class='user_active'>" + userMask + "</div>");
    }
}

function onGetMessage(response) {
    debugger;
    let data = JSON.parse(response.body);
    draw("left", data);
}

function onUserConnectOrDisconnect(response) {
    debugger;
    let user = JSON.parse(response.body);
    let userMask = `${user.firstName} ${user.lastName}@${user.login}`;
    if (user.type === 'CONNECTED') {
        console.log(user.login + 'connected');
        $("#users").append(`<div class='user_active'>${userMask}</div>`);
    } else if (user.type === 'DISCONNECTED') {
        console.log(user.login + ' disconnected');
        let $users = $(".user_active");
        let $disconnectedUser = $users.filter((i, element) => {
            return element.innerHTML === `${userMask}`;
        });
        $disconnectedUser.remove();
    }
}

function onCurrentUserConnect(response) {
    debugger;
    let messages = JSON.parse(response.body);
    if (messages !== []) {
        for (let i = 0; i < messages.length; i++) {
            draw("left", messages[i]);
        }
    }
}

function draw(side, message) {
    debugger;
    console.log("drawing...");
    let $message = $($('.message_template').clone().html());
    //$message.addClass(side).find('.user').html(message.from);
    if (notFromBot(message)) {
        $message.addClass(side).find('.user_text').html(message.firstName + " " + message.lastName);
    } else {
        $message.addClass(side).find('.user_text').html(message.from);
    }
    $message.addClass(side).find('.text').html(message.message);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);
}

function notFromBot(message) {
    return message.firstName !== null && message.lastName !== null
        && message.firstName !== "" && message.lastName !== "";
}

function sendMessage() {
    let messageInputValue = document.getElementById("message_input_value");
    let text = messageInputValue.value;
    if (text !== null && text !== "") {
        stompClient.send("/app/message", {}, JSON.stringify({
            message: text,
            from: user.login,
            firstName: user.firstName,
            lastName: user.lastName
        }));
    }
    messageInputValue.value = "";
}