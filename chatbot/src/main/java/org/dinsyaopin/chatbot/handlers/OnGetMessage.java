package org.dinsyaopin.chatbot.handlers;

import java.lang.reflect.Type;

import org.dinsyaopin.chatbot.ChatBot;
import org.dinsyaopin.chatbot.model.Message;
import org.dinsyaopin.chatbot.service.BotService;
import org.dinsyaopin.chatbot.service.StompSessionService;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnGetMessage implements StompSessionHandler {

    private StompSessionService stompSessionService = StompSessionService.getInstance();

    private BotService botService = BotService.getInstance();

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message message = (Message) payload;
        if (isNotFromBot(message)) {
            String habrLink = botService.getLink(message);
            Message messageFromBot = new Message(ChatBot.LOGIN, habrLink);
            StompSession session = stompSessionService.get();
            session.send("/app/message", messageFromBot);
        }
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
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

    private boolean isNotFromBot(Message message) {
        return !ChatBot.LOGIN.equals(message.getFrom());
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
}
