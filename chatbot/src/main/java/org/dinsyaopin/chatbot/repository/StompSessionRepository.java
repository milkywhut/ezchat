package org.dinsyaopin.chatbot.repository;

import org.springframework.messaging.simp.stomp.StompSession;

public class StompSessionRepository {

    private static StompSessionRepository INSTANCE;
    private StompSession stompSession;

    public static StompSessionRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (StompSessionRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StompSessionRepository();
                }
            }
        }
        return INSTANCE;
    }

    public StompSession get() {
        return stompSession;
    }

    public void setSession(StompSession stompSession) {
        this.stompSession = stompSession;
    }
}
