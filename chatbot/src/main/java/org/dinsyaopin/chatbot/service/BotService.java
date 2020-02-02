package org.dinsyaopin.chatbot.service;

import java.util.Random;

import org.dinsyaopin.chatbot.repository.BotRepository;

public class BotService {

    private static BotService INSTANCE;
    private BotRepository botRepository = BotRepository.getInstance();
    private static int ACTUAL_MAX_POSTFIX_OF_POST = 486372;
    private static final String URL_PREFIX = "https://habr.com/ru/post/";

    private BotService() {

    }

    public static BotService getInstance() {
        if (INSTANCE == null) {
            synchronized (BotService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BotService();
                }
            }
        }
        return INSTANCE;
    }

    public String getLink(String login) {
        return botRepository.getLink(login);
    }

    public String generateLink(String login) {
        for (; ; ) {
            Random random = new Random();
            int actualPostNumber = random.nextInt(ACTUAL_MAX_POSTFIX_OF_POST);
            StringBuilder linkBuilder = new StringBuilder()
                    .append("<a href=\"")
                    .append(URL_PREFIX)
                    .append(actualPostNumber)
                    .append("/\">")
                    .append("post")
                    .append("</a>");
            String link = linkBuilder.toString();
            if (!link.equals(getLink(login))) {
                botRepository.setLinkForUser(link, login);
                return link;
            }
        }
    }
}
