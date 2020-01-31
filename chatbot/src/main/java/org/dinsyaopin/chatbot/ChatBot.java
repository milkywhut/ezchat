package org.dinsyaopin.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import org.dinsyaopin.chatbot.handlers.OnConnect;
import org.dinsyaopin.chatbot.handlers.OnGetMessage;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class ChatBot {

    public static final String URL = "ws://{ip}:{port}/{endpoint}";
    public static final String IP = System.getProperty("ip");
    public static final String PORT = System.getProperty("port");
    public static final String ENDPOINT = System.getProperty("endpoint");
    public static final String LOGIN = System.getProperty("login");

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient client = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler onConnect = new OnConnect();
        StompHeaders headers = new StompHeaders();
        headers.setLogin(LOGIN);
        StompSession session = stompClient.connect(URL, (WebSocketHttpHeaders) null, headers, onConnect, IP, PORT,
                ENDPOINT).get();
        StompSessionHandler onGetMessage = new OnGetMessage();
        session.subscribe("/chat/message", onGetMessage);
        new Scanner(System.in).nextLine();
    }
}
