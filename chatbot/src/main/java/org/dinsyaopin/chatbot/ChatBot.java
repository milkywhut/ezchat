package org.dinsyaopin.chatbot;

import org.dinsyaopin.chatbot.model.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
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
                .connect(URL, (WebSocketHttpHeaders) null, headers, new StompSessionHandlerAdapter() {
                    @Override
                    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                        Message message = new Message(System.getProperty("login"), "Hi! I'm habr bot! I have some reading stuff for ya!");
                        session.send("/app/message", message);
                    }

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return Message.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        super.handleFrame(headers, payload);
                    }

                    @Override
                    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                        super.handleException(session, command, headers, payload, exception);
                    }

                    @Override
                    public void handleTransportError(StompSession session, Throwable exception) {
                        super.handleTransportError(session, exception);
                    }
                }, IP, PORT, ENDPOINT).get();
        session.subscribe("/chat/message", new StompSessionHandlerAdapter() {

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message message = (Message) payload;
                if (notFromBot(message)) {
                    String habrLink = bot.getLink(message);
                    session.send("/app/message", habrLink);
                }
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                super.afterConnected(session, connectedHeaders);
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                super.handleException(session, command, headers, payload, exception);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                super.handleTransportError(session, exception);
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
