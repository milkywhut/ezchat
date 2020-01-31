package org.dinsyaopin.chatbot.repository;

import java.util.List;

import org.dinsyaopin.chatbot.model.UserLink;
import org.dinsyaopin.chatbot.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class BotRepository {

    private static BotRepository INSTANCE;
    SessionFactory sessionFactory = HibernateSessionFactoryUtil.getSessionFactory();

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

    public String getLink(String login) {
        Session session = sessionFactory.openSession();
        List<String> userLinkList =
                (List<String>) session.createQuery("select link from UserLink where chatUser='" + login + "'").list();
        if (userLinkList.isEmpty()) {
            return "";
        }
        return userLinkList.get(0);
    }

    public void setLinkForUser(String link, String login) {
        UserLink userLink = new UserLink(link, login);
        Session session = sessionFactory.openSession();
        List<UserLink> userLinkDb =
                (List<UserLink>) session.createQuery("from UserLink where chatUser='" + login + "'").list();
        if (userLinkDb.size() == 0) {
            Transaction transaction = session.beginTransaction();
            session.save(userLink);
            transaction.commit();
        } else {
            userLinkDb.get(0).setLink(link);
            Transaction transaction = session.beginTransaction();
            session.update(userLinkDb.get(0));
            transaction.commit();
        }
        session.close();
    }
}
