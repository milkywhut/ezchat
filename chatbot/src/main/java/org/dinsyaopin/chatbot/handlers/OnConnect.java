package org.dinsyaopin.chatbot.handlers;

import java.lang.reflect.Type;

import org.dinsyaopin.chatbot.model.Message;
import org.dinsyaopin.chatbot.service.StompSessionService;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnConnect implements StompSessionHandler {

    private StompSessionService stompSessionService = StompSessionService.getInstance();

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("Bot connected with session {}", session);
        stompSessionService.setSession(session);
        Message message = new Message(System.getProperty("login"), "Hi! I'm habr bot! I have some reading stuff for "
                + "ya!");
        session.send("/app/message", message);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
                                Throwable exception) {
        log.error("[websocket] handle exception {}", exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        if (exception instanceof ConnectionLostException) {
            log.warn("[websocket] handle connection close {}", exception.getMessage());
        } else {
            log.error("[websocket] handle error {}", exception.getMessage());
        }
        try {
            session.disconnect();
        } catch (Exception e) {
            log.warn("can not disconnect from session with error {}", e);
        }
    }
}


