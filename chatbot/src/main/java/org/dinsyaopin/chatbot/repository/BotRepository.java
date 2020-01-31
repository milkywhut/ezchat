package org.dinsyaopin.chatbot.repository;

import org.dinsyaopin.chatbot.model.Message;

public class BotRepository {

    private static BotRepository INSTANCE;

    private BotRepository() {

    }

    public static BotRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (BotRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BotRepository();
                }
            }
        }
        return INSTANCE;
    }

    public String getLink(Message message) {
        return "habr link";
    }
}
