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
    console.log("connected : " + frame);
    stompClient.subscribe('/chat/messages', onSubscribe);
}

function onSubscribe(response) {
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

function sendMessage() {
    let text = $("#message_input_value").val();
    if (text !== null && text !== "") {
        stompClient.send("/app/message", {}, JSON.stringify({
            'message': text,
            'login': user.login,
            'password': user.password
        }));
    }
}