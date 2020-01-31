package org.dinsyaopin.chatbot;

import org.dinsyaopin.chatbot.model.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ChatBot {

    private BotService botService = BotService.getInstance();

    private static final String URL = "ws://{ip}:{port}/{endpoint}";
    private static final String IP = System.getProperty("ip");
    private static final String PORT = System.getProperty("port");
    private static final String ENDPOINT = System.getProperty("endpoint");
    private static final String LOGIN = System.getProperty("login");

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ChatBot bot = new ChatBot();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient client = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new BotStompSessionHandler();
        StompHeaders headers = new StompHeaders();
        headers.setLogin(LOGIN);
        StompSession session = stompClient
                .connect(URL, (WebSocketHttpHeaders) null, headers, sessionHandler, IP, PORT, ENDPOINT).get();
        session.subscribe("/chat/message", new StompSessionHandlerAdapter() {

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message message = (Message) payload;
                if (notFromBot(message)) {
                    String habrLink = bot.getLink(message);
                    session.send("/app/message", habrLink);
                }
            }

            private boolean notFromBot(Message message) {
                return !LOGIN.equals(message.getFrom());
            }

            /**
             * Could be implemented if you want direct request to the bot.
             *
             * @param message message
             * @return true if addressed to bot or else.
             */
            @Deprecated
            private boolean addressedToMe(Message message) {
                return "/bot".equals(message.getMessage());
            }
        });
        new Scanner(System.in).nextLine();
    }

    private String getLink(Message message) {
        return botService.getLink(message);
    }
}
