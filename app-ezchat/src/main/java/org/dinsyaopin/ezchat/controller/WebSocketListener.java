package org.dinsyaopin.ezchat.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dinsyaopin.ezchat.model.User;
import org.dinsyaopin.ezchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

import static org.dinsyaopin.ezchat.model.Type.CONNECTED;
import static org.dinsyaopin.ezchat.model.Type.DISCONNECTED;

/**
 * This class is necessary for handling activity of the sessions.
 */
@Component
@Slf4j
public class WebSocketListener {

    private UserService userService;
    private SimpMessageSendingOperations messageSendingOperations;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private void setMessageSendingOperations(SimpMessageSendingOperations messageSendingOperations) {
        this.messageSendingOperations = messageSendingOperations;
    }

    /**
     * Handles session connection event.
     *
     * @param event event of session connection
     */
    @EventListener
    public void handleConnection(SessionConnectedEvent event) {
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        GenericMessage connectHeader =
                (GenericMessage) stompAccessor.getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
        Map<String, List<String>> nativeHeaders =
                (Map<String, List<String>>) connectHeader.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
        String login = "";
        String firstName = "";
        String lastName = "";
        try {
            assert nativeHeaders != null;
            login = nativeHeaders.get("login").get(0);
            firstName = nativeHeaders.get("firstName").get(0);
            lastName = nativeHeaders.get("lastName").get(0);
        } catch (NullPointerException e) {
            log.error("Some user property is null. ", e);
        }
        String sessionId = stompAccessor.getSessionId();
        log.info("New session by user <{}> with sessionId <{}>", login, sessionId);
        User userConnected = new User(login, sessionId, firstName, lastName, CONNECTED);
        long userSessions =
                userService.getActiveUsers().parallelStream()
                        .map(User::getLogin)
                        .filter(login::equals)
                        .count();
        if (userSessions == 0) {
            messageSendingOperations.convertAndSend("/users/user", userConnected);
        }
        userService.add(userConnected);
    }

    /**
     * Handles session disconnection event.
     *
     * @param event event of session connection
     */
    @EventListener
    public void handleDisconnection(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        User userDisconnected = userService.getBySessionId(sessionId).get(0);
        userService.remove(userDisconnected);
        userDisconnected.setType(DISCONNECTED);
        String login = userDisconnected.getLogin();
        log.info("End of the session <{}> by user <{}>", sessionId, login);
        long userSessionsNumber = userService.countNumberOfUserSessions(login);
        if (userSessionsNumber == 0) {
            messageSendingOperations.convertAndSend("/users/user", userDisconnected);
        }
    }
}
