let stompClient = null;
let user;

function connect() {
    user = {
        login: $("#username").val(),
        password: $("#password").val()
    };
    let socket = new SockJS("/chat-messaging");//for stomp endpoint in web socket config
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
        console.log("connected : " + frame);
        stompClient.subscribe('/chat/messages', response => {
            let data = JSON.parse(response.body);
            console.log(data);
            draw("left", data);
        });
    });
    $("#authentication").css("display", "none");
    $("#chat_window").css("display", "block");
}

function draw(side, message) {
    console.log("drawing...");
    let $message = $($('.message_template').clone().html());
    $message.addClass(side).find('#user').html(message.login);
    $message.addClass(side).find('.text').html(message.message);
    $('.messages').append($message);
    return setTimeout(function () {
        return $message.addClass('appeared');
    }, 0);
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
}

function sendMessage() {
    stompClient.send("/app/message", {}, JSON.stringify({
        'message': $("#message_input_value").val(),
        'login': user.login,
        'password': user.password
    }));
}