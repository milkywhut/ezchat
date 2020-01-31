package org.dinsyaopin.ezchat.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.dinsyaopin.ezchat.model.Type;
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
        String login;
        try {
            login = Objects.requireNonNull(nativeHeaders).get("login").get(0);
        } catch (NullPointerException e) {
            log.error("Login should be set to connect to the chat.", e);
            throw new NullPointerException();
        }
        String sessionId = stompAccessor.getSessionId();
        log.info("New session by user <{}> with sessionId <{}>", login, sessionId);
        User user = new User(login, sessionId);
        long userSessions =
                userService.getActiveUsers().parallelStream().map(User::getLogin).filter(login::equals).count();
        if (userSessions == 0) {
            messageSendingOperations.convertAndSend("/users/user", new User(login, Type.CONNECTED));
        }
        userService.add(user);
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
        Optional<User> user =
                userService.getActiveUsers().parallelStream().filter((f) -> f.getSessionId().equals(sessionId)).findAny();
        if (user.isPresent()) {
            String login = user.get().getLogin();
            log.info("End of the session <{}> by user <{}>", sessionId, login);
            userService.remove(user.get());
            long userSessions =
                    userService.getActiveUsers().parallelStream().map(User::getLogin).filter(login::equals).count();
            if (userSessions == 0) {
                messageSendingOperations.convertAndSend("/users/user", new User(login, Type.DISCONNECTED));
            }
        }
    }
}
