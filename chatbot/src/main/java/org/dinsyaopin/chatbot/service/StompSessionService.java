package org.dinsyaopin.chatbot.service;

import org.dinsyaopin.chatbot.repository.StompSessionRepository;
import org.springframework.messaging.simp.stomp.StompSession;

public class StompSessionService {

    private static StompSessionService INSTANCE;
    private StompSessionRepository stompSessionRepository = StompSessionRepository.getInstance();

    private StompSessionService() {

    }

    public static StompSessionService getInstance() {
        if (INSTANCE == null) {
            synchronized (StompSessionService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StompSessionService();
                }
            }
        }
        return INSTANCE;
    }

    public StompSession get() {
        return stompSessionRepository.get();
    }

    public void setSession(StompSession stompSession) {
        stompSessionRepository.setSession(stompSession);
    }
}
