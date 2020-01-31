package org.dinsyaopin.chatbot;

import org.dinsyaopin.chatbot.model.Message;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

public class BotStompSessionHandler implements StompSessionHandler {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        Message message = new Message(System.getProperty("login"), "Hi! I'm habr bot! I have some reading stuff for ya!");
        session.send("/app/message", message);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println(1);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println(2);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println(1);
    }
}
